package com.blockybot.modules.welcome;

import java.util.regex.Pattern;

import com.blockybot.Debug;
import com.blockybot.core.BlockyBotModule;
import com.blockybot.extensions.BlockyBotEmbed;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;

/**
 * Module for BlockyBot to add join/leave messages in channel
 * 
 * @author BlockBa5her
 *
 */
public class WelcomeModule extends BlockyBotModule {

    private static final String WEBSITE_REGEX = "(https?://)?\\w+.\\w{1,5}/(\\w+/?)+";
    
    /**
     * Gets the welcome channel from specified guilds
     * 
     * @param guilds
     *            Guilds to search the welcome channel for
     * @return The welcome channel (null if not found)
     */
    private TextChannel getWelcomeChannel(Guild[] guilds) {
        for (Guild g : guilds) { // for every guild to search
            for (TextChannel t : g.getTextChannels()) { // for every text channel in guild
                if (t.getId().equals(this.config.getString("channel"))) { // if channel id is equal to welcome
                                                                                  // channel id
                    return t; // return the channel
                }
            }
        }
        return null; // return null if nothing found
    }

    /**
     * Send a general welcome message
     * 
     * @param title
     *            The title of the message
     * @param desc
     *            The description of the message
     */
    private void sendWelcomeMsg(String title, String desc) {
        TextChannel c = getWelcomeChannel(bot.getGuilds()); // gets the welcome channel

        // sending embed to welcome channel with information
        c.sendMessage(new BlockyBotEmbed().setTitle(title).setDescription(desc).build()).queue();
    }
    
    private void failWelcomeWithMsg(User user, String msg) {
        PrivateChannel ch = user.openPrivateChannel().complete();
        ch.sendMessage(msg).queue();
    }

    /**
     * The handle for the GuildMemberJoinEvent
     * 
     * @param event
     *            The event for the handle
     */
    private void onUserJoin(GuildMemberJoinEvent event) {
        User usr = event.getUser(); // get the user from event
        
        boolean websiteFound = Pattern.compile(WEBSITE_REGEX).matcher(usr.getName()).find();
        if (websiteFound) {
            failWelcomeWithMsg(usr, "Your username has been flagged because we think there is a URL in the username\n"
                                  + "If you believe this to be an error, please contact BlockBa5her");
            Debug.trace("message flagged for URL");
            return;
        }
        
        Debug.trace("welcoming " + usr.getAsMention()); // debug trace
        sendWelcomeMsg("User Joined", "User `" + usr.getName() + "` joined. Welcome him!"); // send welcome msg
    }

    /**
     * The handle for the GuildMemberLeaveEvent
     * 
     * @param event
     *            The event for the handle
     */
    private void onUserLeave(GuildMemberLeaveEvent event) {
        User usr = event.getUser(); // get the user from event
        
        boolean websiteFound = Pattern.compile(WEBSITE_REGEX).matcher(usr.getName()).find();
        if (websiteFound) {
            failWelcomeWithMsg(usr, "Your username has been flagged because we think there is a URL in the username\n"
                                  + "If you believe this to be an error, please contact BlockBa5her");
            Debug.trace("message flagged for URL");
            return;
        }
        
        Debug.trace("unwelcoming " + usr.getAsMention()); // debug trace
        sendWelcomeMsg("User Left", "User `" + usr.getName() + "` left. So sad :cry:"); // send welcome msg
    }

    @Override
    public void onStart() {
        super.onStart();

        // add event handlers
        bot.addEvent(GuildMemberJoinEvent.class, this::onUserJoin);
        bot.addEvent(GuildMemberLeaveEvent.class, this::onUserLeave);
    }
}
