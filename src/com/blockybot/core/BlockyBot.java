package com.blockybot.core;

import com.blockybot.Debug;
import com.blockybot.Main;
import com.blockybot.exceptions.ExceptionHelper;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.Event;
import javax.security.auth.login.LoginException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BlockyBot implements Runnable {

    private String token;
    private JDABuilder builder;
    private JDA bot;
    private BlockyBotEventHandler eventHandler;
    private Thread runThread;

    private Map<BlockyBotModule, Boolean> modules;

    public BlockyBot(String botToken) {
        token = botToken;
        modules = new HashMap<>();
        builder = new JDABuilder(AccountType.BOT).setToken(token);
        eventHandler = new BlockyBotEventHandler();
    }

    public void login() {
        if (bot == null) {
            runThread = new Thread(this);
            runThread.setName("BlockyBot");
            builder.addEventListener(eventHandler);
            
            try {
                bot = builder.buildBlocking();
                runThread.start();
                Debug.trace("logged in successfully");
            } catch (LoginException e) {
                ExceptionHelper.throwException(e);
            } catch (InterruptedException e) {
                ExceptionHelper.throwException(e);
            }
            
            initialize();
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

    public <T extends Event> void addEvent(Class<T> type, Consumer<T> callback) {
        eventHandler.add(type, callback);
    }

    public <T extends Event> void removeEvent(Consumer<T> callback) {
        eventHandler.remove(callback);
    }

    private boolean alreadyHasModuleType(Class<?> type) {
        for (BlockyBotModule m : getModules())
            if (m.getClass().equals(type))
                return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T extends BlockyBotModule> T getModule(Class<T> type) {
        for (BlockyBotModule m : getModules()) {
            if (m.getClass().equals(type)) {
                return (T) m;
            }
        }
        return null;
    }
    public <T extends BlockyBotModule> JSONObject getModuleConfig(Class<T> type) {
    	String moduleName = getModule(type).getName();
    	JSONObject moduleConfigs = Main.getConf().getJSONObject("modules");
    	return moduleConfigs.has(moduleName) ? moduleConfigs.getJSONObject(moduleName) : null;
    }

    public BlockyBotModule[] getModules() {
        return modules.keySet().toArray(new BlockyBotModule[0]);
    }

    public void addModule(BlockyBotModule module) {
        if (!alreadyHasModuleType(module.getClass())) {
            if (bot == null) {
                modules.put(module, false);
            } else
                throw new RuntimeException("cannot add module after bot is initialized");
        } else
            throw new RuntimeException("module with same type already");
    }

    public void addModuleRange(BlockyBotModule[] modules) {
        for (BlockyBotModule m : modules) {
            this.addModule(m);
        }
    }

    private void startAllModules() {
    	
        for (BlockyBotModule m : getModules()) {
        	m.setConfig(this, getModuleConfig(m.getClass()));
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

            for (BlockyBotModule m : getModules()) {
                boolean active = modules.get(m);
                if (active) {
                	m.setConfig(this, getModuleConfig(m.getClass()));
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

        Debug.trace("done with initialization");
    }
}
