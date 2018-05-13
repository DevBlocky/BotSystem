package com.botsystem.modules.report;

import com.botsystem.core.BotSystemModule;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.permissions.PermissionsModule;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Module for BotSystem detecting when the report reaction as added to a message then handling it
 * @author BlockBa5her
 *
 */
public class ReportModule extends BotSystemModule {

	/**
	 * The last reported message
	 */
    private String lastReportedMessageId = "";

    /**
     * The method invoked when there is a given report
     * @param msg The message that has been reported
     * @param reac The reaction used to report it
     */
    private void onReport(Message msg, MessageReaction reac) {

        lastReportedMessageId = msg.getId(); // get message id

        List<User> users = reac.getUsers().complete(); // get reaction users
        StringBuilder userNames = new StringBuilder(); // user names
        for (User usr : users) { // for every user in user reactions
            if (userNames.length() == 0) { // if element 0
                userNames.append("<@").append(usr.getId()).append(">"); // regular append user
            } else { // otherwise
                userNames.append(", <@").append(usr.getId()).append(">"); // append user with comma before it
            }

            BotSystemEmbed dmEmb = new BotSystemEmbed(); // create new embed
            // add information fields
            dmEmb.addField(new MessageEmbed.Field("Info", "" +
                    "We have sent your report for our moderators to review", false));
            dmEmb.addField(new MessageEmbed.Field("Policy", "" +
                    "If you are seen abusing or spamming the report system, " +
                    "you will be removed from the DispatchSystem server either temp or perm.", false));

            // send the embed in DMs with user
            usr.openPrivateChannel().complete().sendMessage(dmEmb.build()).complete();
        }

        // get the mods from the guild
        List<Member> mods = getMods(msg.getGuild());
        // for every mod in guild
        for (Member mod : mods) {
        	// new embed
            BotSystemEmbed modEmb = new BotSystemEmbed();

            // adding embed information fields
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

            // send embed to mod in DM
            mod.getUser().openPrivateChannel().complete().sendMessage(modEmb.build()).queue();
        }

        // remove all reactions
        for (User u : reac.getUsers()) {
            reac.removeReaction(u).complete();
        }
    }
    
    /**
     * The event handle for the GuildMessageReactionAddEvent
     * @param event The event for the handle
     */
    private void onGuildReaction(GuildMessageReactionAddEvent event) {
    	// getting reaction and message
        MessageReaction r = event.getReaction();
        Message m = event.getChannel().getMessageById(event.getMessageId()).complete();
        
        // if is report reaction and wasn't last reported message
        if (r.getReactionEmote().getName().equals("report") && !m.getId().equals(lastReportedMessageId)) {
        	onReport(m, event.getReaction()); // invoke the onReport event handler
        }
    }

    @Override
    public void onStart() {
        super.onStart(); 

        // add event handlers
        bot.addEvent(GuildMessageReactionAddEvent.class, this::onGuildReaction);
    }

    /**
     * Gets all of the moderators in a certain guild
     * @param g The guild to get all of the moderators in
     * @return The list of moderators in the server
     */
    private List<Member> getMods(Guild g) {
        PermissionsModule perms = bot.getModule(PermissionsModule.class); // get perms module
        List<Member> tmp = new LinkedList<>(); // new temp list

        for (Member m : g.getMembers()) { // for every member
            if (perms.checkUserPerm("moderator", m)) { // if member is moderator
                tmp.add(m); // add moderator to tmp list
            }
        }

        return tmp; // return the temporary list
    }
}
