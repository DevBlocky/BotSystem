package com.blockybot.modules.pingpong;

import com.blockybot.Debug;
import com.blockybot.core.BlockyBotModule;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Module for BlockyBot with no purpose other that for testing and making sure
 * the bot is well When ever you type "ping" into chat, it replies with "Pong!"
 * 
 * @author BlockBa5her
 *
 */
public class PingPongModule extends BlockyBotModule {

    /**
     * Event handle for the MessageReceivedEvent
     * 
     * @param event
     *            The event to be used in the handle
     */
    private void onGuildMessageAdd(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentDisplay(); // get display contents
        if (msg.toLowerCase().startsWith("ping") && msg.length() < 8) { // if message is a ping message
            Debug.trace("user " + event.getAuthor().getName() + " pinged, ponging."); // debug
            event.getChannel().sendMessage("Pong!").submit(); // pong back
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // add event handlers
        bot.addEvent(MessageReceivedEvent.class, this::onGuildMessageAdd);
    }
}
