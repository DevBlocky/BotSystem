package com.blockybot.core;

import com.blockybot.Debug;
import com.blockybot.exceptions.ExceptionHelper;

import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Handles events for easy adding and removing
 * @author BlockBa5her
 *
 */
public class BlockyBotEventHandler implements EventListener {

    /**
     * All of the events, that is stored in a map
     */
    private Map<Class<?>, ArrayList<Consumer<? extends Event>>> events;
    
    /**
     * Creates an instance of the event handler
     */
    BlockyBotEventHandler() {
        this.events = new HashMap<>();
    }

    /**
     * Adds and event to the event handler
     * @param t The type of the event
     * @param cb The callback of the event
     */
    public <T extends Event> void add(Class<T> t, Consumer<T> cb) {
        if (events.containsKey(t)) { // if there is already that type of event
            events.get(t).add(cb); // adds it to the already existing ArrayList
        } else { // if there is not a type of that event
            ArrayList<Consumer<? extends Event>> cbs = new ArrayList<>(); // create a new array list
            cbs.add(cb); // add the callback to the array list
            events.put(t, cbs); // adds the array list to the map
        }
    }

    /**
     * Removes an event from the event handler
     * @param cb The callback to remove
     */
    public <T extends Event> void remove(Consumer<T> cb) {
        for (ArrayList<Consumer<? extends Event>> x : events.values()) { // for every array list in the map of values
            for (int i = 0; i < x.size(); i++) { // loop through size
                Consumer<? extends Event> ce = x.get(i); // get item from index
                if (cb.equals(ce)) { // if the callback equals the stored callback
                    x.remove(i); // remove it from the array list
                    break;
                }
            }
        }
    }

    /**
     * An override for handling events
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(Event event) {
        Debug.trace("received event for " + event.getClass().getSimpleName());

        // for each internal type
        for (Class<?> internalType : events.keySet()) {
            if (event.getClass() == internalType) { // if the internal type is the same as the given event
                ArrayList<Consumer<? extends Event>> cbs = events.get(internalType); // get the array list from the map
                cbs.forEach(x -> { // for everything in the events
                    try {
                        @SuppressWarnings("rawtypes")
                        Consumer raw = (Consumer) x; // convert to raw, so that can "accept"
                        raw.accept(event); // invoke the consumer
                    } catch (IllegalStateException e) { // if an illegal state (it's from JDA)
                        String eStr = ExceptionHelper.getFullExceptionString(e); // stack trace
                        System.err.println("JDA threw a IllegalStateException:\n"); // print
                        System.err.println(eStr); // print
                    } catch (Exception e) { // regular exception
                        Debug.trace("exception on event, running throw thread"); // trace
                        ExceptionHelper.createExceptionThrowThread(e); // throw from program
                    }
                });
                break; // break from everything
            }
        }
    }
}
