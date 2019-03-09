package com.blockybot.modules.adminlog;

import org.json.JSONObject;

import net.dv8tion.jda.core.events.Event;

public interface EventToJSON {
    public JSONObject get(Event event);
}
