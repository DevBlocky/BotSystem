package com.blockybot.modules.adminlog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import com.blockybot.core.BlockyBotModule;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;

public class AdminLogModule extends BlockyBotModule {
    private String logChannelId;
    private HashMap<Class<? extends Event>, EventToJSON> map;
    
    public AdminLogModule() {
        map = new HashMap<Class<? extends Event>, EventToJSON>();
    }
    
    public void addLoggedEvent(Class<? extends Event> eventType, EventToJSON userFromEvent) {
        map.put(eventType, userFromEvent);
    }
    
    public void log(String header, JSONObject obj) {
        TextChannel logCh = bot.getAPI().getTextChannelById(logChannelId);
        String send = header + " ```json\n" + obj.toString() + "```";
        
        if (send.length() <= 2000)
        	logCh.sendMessage(send).queue();
    }
    
    private void onEvent(Event event) {
        JSONObject info = map.get(event.getClass()).get(event);
        if (info == null)
            return;
        
        log("EventTrigger: `" + event.getClass().getSimpleName() + "`", info);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        logChannelId = this.config.getString("channel");
        
        Iterator<Map.Entry<Class<? extends Event>, EventToJSON>> it = 
                map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Class<? extends Event>, EventToJSON> pair = it.next();
            
            bot.addEvent(pair.getKey(), this::onEvent);
        }
    }
}
