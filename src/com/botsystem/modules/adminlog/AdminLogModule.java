package com.botsystem.modules.adminlog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import com.botsystem.Main;
import com.botsystem.core.BotSystemModule;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;

public class AdminLogModule extends BotSystemModule {
    private String logChannelId;
    private HashMap<Class<? extends Event>, EventToJSON> map;
    
    public AdminLogModule() {
        map = new HashMap<Class<? extends Event>, EventToJSON>();
    }
    
    public void addLoggedEvent(Class<? extends Event> eventType, EventToJSON userFromEvent) {
        map.put(eventType, userFromEvent);
    }
    
    private void onEvent(Event event) {
        JSONObject info = map.get(event.getClass()).get(event);
        if (info == null)
            return;
        
        TextChannel logCh = bot.getAPI().getTextChannelById(logChannelId);
        String send = "EventTrigger: `" + event.getClass().getSimpleName() + 
                "` ```\n" + info.toString() + "\n```";
        
        logCh.sendMessage(send).queue();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        logChannelId = Main.CONFIG.getString("log-channel");
        
        Iterator<Map.Entry<Class<? extends Event>, EventToJSON>> it = 
                map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Class<? extends Event>, EventToJSON> pair = it.next();
            
            bot.addEvent(pair.getKey(), this::onEvent);
        }
    }
    
    
}
