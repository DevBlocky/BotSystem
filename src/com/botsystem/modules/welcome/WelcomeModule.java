package com.botsystem.modules.welcome;

import com.botsystem.Debug;
import com.botsystem.Main;
import com.botsystem.core.BotSystemModule;
import com.botsystem.extensions.BotSystemEmbed;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;

public class WelcomeModule extends BotSystemModule {

    private static TextChannel getWelcomeChannel(Guild[] guilds) {
        for (Guild g : guilds) {
            for (TextChannel t : g.getTextChannels()) {
                if (t.getId().equals(Main.conf.getString("welcome-channel"))) {
                    return t;
                }
            }
        }
        return null;
    }

    private void sendWelcomeMsg(String title, String desc) {
        TextChannel c = getWelcomeChannel(bot.getGuilds());

        c.sendMessage(new BotSystemEmbed().setTitle(title).setDescription(desc).build()).queue();
    }

    private void onUserJoin(User usr) {
        Debug.trace("welcoming " + usr.getAsMention());
        sendWelcomeMsg("User Joined", "User `" + usr.getName() + "` joined. Welcome him!");
    }

    private void onUserLeave(User usr) {
        Debug.trace("unwelcoming " + usr.getAsMention());
        sendWelcomeMsg("User Left", "User `" + usr.getName() + "` left. So sad :cry:");
    }

    @Override
    public void onStart() {
        super.onStart();

        bot.addEvent(GuildMemberJoinEvent.class, e -> {
            GuildMemberJoinEvent je = (GuildMemberJoinEvent) e;

            onUserJoin(je.getUser());
        });
        bot.addEvent(GuildMemberLeaveEvent.class, e -> {
            GuildMemberLeaveEvent le = (GuildMemberLeaveEvent) e;

            onUserLeave(le.getUser());
        });
    }
}
