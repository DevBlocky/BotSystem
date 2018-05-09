package com.botsystem.core;

import com.botsystem.Debug;
import com.botsystem.exceptions.ExceptionHelper;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BotSystem implements Runnable {

    private String token;
    private JDABuilder builder;
    private JDA bot;
    private BotSystemEventHandler eventHandler;
    private Thread runThread;

    private Map<BotSystemModule, Boolean> modules;

    public BotSystem(String botToken) {
        token = botToken;
        modules = new HashMap<>();
        builder = new JDABuilder(AccountType.BOT).setToken(token);
        eventHandler = new BotSystemEventHandler();

        eventHandler.add(ReadyEvent.class, event -> initialize());
    }

    public void login() {
        if (bot == null) {
            try {
                bot = builder.buildAsync();
            } catch (LoginException e) {
                ExceptionHelper.throwException(e);
            }/* catch (InterruptedException e) {
                ExceptionHelper.throwException(e);
            }*/

            bot.addEventListener(eventHandler);
            runThread = new Thread(this);
            runThread.start();
        }
    }

    public void logout() {
        if (bot != null) {
        	runThread.interrupt();
        	
            bot.shutdownNow();
            bot = null;
            Debug.trace("shutdown successful");
        }
    }

    @SuppressWarnings("unchecked")
	public <T extends Event> void addEvent(Class<T> type, Consumer<T> callback) {
        eventHandler.add(type, e -> {
        	callback.accept((T)e);
        });
    }

    private boolean alreadyHasModuleType(Class<?> type) {
        for (BotSystemModule m : getModules())
            if (m.getClass().equals(type))
                return true;
        return false;
    }

    @SuppressWarnings("unchecked")
	public <T extends BotSystemModule> T getModule(Class<T> type) {
        for (BotSystemModule m : getModules()) {
            if (m.getClass().equals(type)) {
                return (T) m;
            }
        }
        return null;
    }

    public BotSystemModule[] getModules() {
        return modules.keySet().toArray(new BotSystemModule[0]);
    }

    public void addModule(BotSystemModule module) {
        if (!alreadyHasModuleType(module.getClass())) {
            if (bot == null) {
                modules.put(module, false);
            } else
                throw new RuntimeException("cannot add module after bot is initialized");
        } else
            throw new RuntimeException("module with same type already");
    }

    public void addModuleRange(BotSystemModule[] modules) {
        for (BotSystemModule m : modules) {
            this.addModule(m);
        }
    }

    private void startAllModules() {
        for (BotSystemModule m : getModules()) {
            m.setBotSystem(this);
            m.onStart();
            modules.put(m, true);
        }
    }

    public void setGame(String game) {
        bot.getPresence().setGame(Game.playing(game));
    }

    public void setStatus(String status) {
        OnlineStatus s = OnlineStatus.valueOf(status.toUpperCase());
        bot.getPresence().setStatus(s);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Debug.trace("moduleTickThread interrupted... quitting thread.");
                break;
            }

            for (BotSystemModule m : getModules()) {
                boolean active = modules.get(m);
                if (active) {
                    m.setBotSystem(this);
                    m.onTick();
                }
            }
        }
    }

    public Guild[] getGuilds() {
        return bot.getGuilds().toArray(new Guild[0]);
    }
    
    public JDA getAPI() {
    	return bot;
    }

    private void initialize() {
        Debug.trace("initializing");

        Debug.trace("setting bot features");
        bot.setAutoReconnect(true);
        startAllModules();

        runThread.setName("BotSystem");

        Debug.trace("done with initialization");
    }
}
