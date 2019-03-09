package com.blockybot.core;

import com.blockybot.Debug;
import com.blockybot.Main;
import com.blockybot.console.commands.ConsoleCommand;
import com.blockybot.console.commands.ConsoleCommands;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;

/**
 * A class to hold disposable bot information
 * 
 * @author BlockBa5her
 *
 */
public class BotBottle {
    // main bot
    private BlockyBot bot;
    // console commands class
    private ConsoleCommands commands;

    // BlockyBot modules
    private BlockyBotModule[] modules;
    // all console commands
    private ConsoleCommand[] consoleCommands;

    /**
     * Package-private initializer to create bottle
     * 
     * @param modules
     *            The modules for BlockyBot
     * @param consoleCommands
     *            The console commands to add
     */
    public BotBottle(BlockyBotModule[] modules, ConsoleCommand[] consoleCommands) {
        Debug.trace("creating instance of new bot bottle");

        this.modules = modules;
        this.consoleCommands = consoleCommands;

        Debug.trace("new instance created");
    }

    /**
     * Starts the instance of the bottle
     */
    public void start() {
        try {
            Thread.sleep(1500); // ghost-time period
        } catch (InterruptedException e1) {
        }

        // creating bot
        bot = new BlockyBot(Main.getConf().getString("token"));
        // adding modules
        bot.addModuleRange(modules);

        // add ready event to bot
        bot.addEvent(ReadyEvent.class, e -> {
            // creating console commands class
            Debug.trace("initializing console commands, and command modules");
            commands = new ConsoleCommands(bot, consoleCommands);
            // starting console commands
            commands.start();
            Debug.trace("console commands started");
        });
        
        // login the bot
        bot.login();
    }

    boolean botShutdown = false;

    /**
     * Disposes and stops all items in bottle
     */
    public void dispose() {
        // add shutdown event to know when bot is fully shutdown
        bot.addEvent(ShutdownEvent.class, e -> {
            botShutdown = true;
        });

        // logout the bot
        bot.logout();

        // wait till bot is fully logged out
        while (!botShutdown) {
            Thread.yield();
        }

        // stop console commands
        if (commands != null) {
            commands.interrupt();
        }

        // nullify everything
        bot = null;
        commands = null;
        consoleCommands = null;
        modules = null;

        Debug.trace("disposed current bot bottle");
    }

    /**
     * Get's the bot instance
     * 
     * @return The BlockyBot bot
     */
    public BlockyBot getBot() {
        return bot;
    }
}
