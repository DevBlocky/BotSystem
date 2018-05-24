package com.botsystem.core;

import com.botsystem.Debug;
import com.botsystem.exceptions.ExceptionHelper;
import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.hooks.EventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BotSystemEventHandler implements EventListener {

    private Map<Type, ArrayList<BotSystemEventCallback>> events;

    BotSystemEventHandler() {
        this.events = new HashMap<>();
    }

    public void add(Type t, BotSystemEventCallback cb) {
        if (events.containsKey(t)) {
            events.get(t).add(cb);
        } else {
            ArrayList<BotSystemEventCallback> cbs = new ArrayList<>();
            cbs.add(cb);
            events.put(t, cbs);
        }
    }

    @Override
    public void onEvent(Event event) {
        Debug.trace("received event for " + event.getClass().getSimpleName());

        for (Type internalType : events.keySet()) {
            if (event.getClass() == internalType) {
                ArrayList<BotSystemEventCallback> cbs = events.get(internalType);
                cbs.forEach(x -> {
                    try {
                        x.callback(event);
                    } catch (IllegalStateException e) {
                        String eStr = ExceptionHelper.getFullExceptionString(e);
                        System.err.println("JDA threw a IllegalStateException:\n");
                        System.err.println(eStr);
                    } catch (Exception e) {
                        Debug.trace("exception on event, running throw thread");
                        ExceptionHelper.createExceptionThrowThread(e);
                    }
                });
                break;
            }
        }
    }
}
