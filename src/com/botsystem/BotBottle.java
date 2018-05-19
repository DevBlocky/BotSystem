package com.botsystem;

import com.botsystem.console.commands.ConsoleCommand;
import com.botsystem.console.commands.ConsoleCommands;
import com.botsystem.core.BotSystem;
import com.botsystem.core.BotSystemModule;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;

/**
 * A class to hold valuable bot information
 * @author BlockBa5her
 *
 */
public class BotBottle {
	private BotSystem bot;
	private ConsoleCommands commands;
	
	private BotSystemModule[] modules;
	private ConsoleCommand[] consoleCommands;
	
	private boolean botShutdown = false;

	/**
	 * Package-private initializer to create bottle
	 */
	BotBottle(BotSystemModule[] modules, ConsoleCommand[] consoleCommands) {
		Debug.trace("creating instance of new bot bottle");
		
		this.modules = modules;
		this.consoleCommands = consoleCommands;
		init();
		
		Debug.trace("new instance created");
	}
	
	private void init() {
		try {
			Thread.sleep(1500); // ghost-time period
		} catch (InterruptedException e1) {}
		
		bot = new BotSystem(Main.CONFIG.getString("token"));
		bot.addModuleRange(modules);
		bot.login();
		
		bot.addEvent(ReadyEvent.class, e -> {
			Debug.trace("initializing console commands, and command modules");
			commands = new ConsoleCommands(bot, consoleCommands);
			commands.start();
			Debug.trace("console commands started");
		});
	}
	
	public void dispose() {
		bot.addEvent(ShutdownEvent.class, e -> {
			botShutdown = true;
		});
		
		bot.logout();
		
		while (!botShutdown) {
			Thread.yield();
		}
		
		commands.interrupt();
		
		bot = null;
		commands = null;
		consoleCommands = null;
		modules = null;
		
		Debug.trace("disposed current bot bottle");
	}
	
	public BotSystem getBot() {
		return bot;
	}

	public boolean isBotShutdown() {
		return botShutdown;
	}
}
