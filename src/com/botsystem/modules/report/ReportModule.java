package com.botsystem.modules.report;

import com.botsystem.core.BotSystemModule;
import com.botsystem.exceptions.ExceptionHelper;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.permissions.PermissionsModule;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReportModule extends BotSystemModule {

    private String lastReportedMessageId = "";

    private void onReport(Message msg, MessageReaction reac)
            throws InterruptedException, ExecutionException {

        lastReportedMessageId = msg.getId();

        List<User> users = reac.getUsers().submit().get();
        StringBuilder userNames = new StringBuilder();
        for (User usr : users) {
            if (userNames.length() == 0) {
                userNames.append("<@").append(usr.getId()).append(">");
            } else {
                userNames.append(", <@").append(usr.getId()).append(">");
            }

            BotSystemEmbed dmEmb = new BotSystemEmbed();
            dmEmb.addField(new MessageEmbed.Field("Info", "" +
                    "We have sent your report for our moderators to review", false));
            dmEmb.addField(new MessageEmbed.Field("Policy", "" +
                    "If you are seen abusing or spamming the report system, " +
                    "you will be removed from the DispatchSystem server either temp or perm.", false));

            usr.openPrivateChannel().submit().get().sendMessage(dmEmb.build()).submit().get();
        }

        List<Member> mods = getMods(msg.getGuild());
        for (Member mod : mods) {
            BotSystemEmbed modEmb = new BotSystemEmbed();

            modEmb.addField(new MessageEmbed.Field("Information", "" +
                    "This is an automated response from the bot about a user's message report", false));
            modEmb.addField(new MessageEmbed.Field("Message Content",
                    msg.getContentDisplay(), false));
            modEmb.addField(new MessageEmbed.Field("Message Channel", "" +
                    "<#" + msg.getChannel().getId() + ">", false));
            modEmb.addField(new MessageEmbed.Field("Message Sender", "" +
                    "<@" + msg.getAuthor().getId() + ">", false));
            modEmb.addField(new MessageEmbed.Field("Reporter(s)",
                    userNames.toString(), false));

            mod.getUser().openPrivateChannel().submit().get().sendMessage(modEmb.build()).queue();
        }

        for (User u : reac.getUsers()) {
            reac.removeReaction(u).complete();
        }
    }

    @Override
    public void onStart() {

        super.onStart();

        bot.addEvent(GuildMessageReactionAddEvent.class, e -> {
            GuildMessageReactionAddEvent ge = (GuildMessageReactionAddEvent) e;

            MessageReaction r = ge.getReaction();
            Message m = ge.getChannel().getMessageById(ge.getMessageId()).complete();
            if (r.getReactionEmote().getName().equals("report") && !m.getId().equals(lastReportedMessageId)) {
                try {
                    onReport(m, ge.getReaction());
                } catch (Exception e1) {
                    ExceptionHelper.throwException(e1);
                }
            }
        });
    }

    private List<Member> getMods(Guild g) {
        PermissionsModule perms = bot.getModule(PermissionsModule.class);
        List<Member> tmp = new LinkedList<>();

        for (Member m : g.getMembers()) {
            if (perms.checkUserPerm("moderator", m)) {
                tmp.add(m);
            }
        }

        return tmp;
    }
}
