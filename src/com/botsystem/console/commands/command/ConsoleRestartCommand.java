package com.botsystem.console.commands.command;

import com.botsystem.Main;
import com.botsystem.console.commands.ConsoleCommand;
import com.botsystem.extensions.Utils;

/**
 * Command for restarting the bot's core
 * @author BlockBa5her
 *
 */
public class ConsoleRestartCommand extends ConsoleCommand {

	private String cmd;
	
	/**
	 * Creates instance of the command
	 * @param cmd
	 */
	public ConsoleRestartCommand(String cmd) {
		this.cmd = cmd;
	}
	
	@Override
	public void onInvoke(String[] arguments) {
		// destroy and create instance on a new thread
		Utils.createTimeout(() -> {
			Main.destroyInstance();
			Main.createInstance();
		}, 0, "BotSystem Restart Thread");
	}

	@Override
	public String getCmd() {
		return cmd;
	}

	@Override
	public String getDesc() {
		return "Restarts bot's core";
	}

}
