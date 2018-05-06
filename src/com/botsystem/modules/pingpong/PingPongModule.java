package com.botsystem.modules.pingpong;

import com.botsystem.Debug;
import com.botsystem.core.BotSystemModule;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PingPongModule extends BotSystemModule {

    @Override
    public void onStart() {
        super.onStart();

        bot.addEvent(MessageReceivedEvent.class, event -> {
            MessageReceivedEvent r = (MessageReceivedEvent) event;

            String msg = r.getMessage().getContentDisplay();
            if (msg.toLowerCase().startsWith("ping") && msg.length() < 8) {
                Debug.trace("user " + r.getAuthor().getName() + " pinged, ponging.");
                r.getChannel().sendMessage("Pong!").submit();
            }
        });
    }
}
