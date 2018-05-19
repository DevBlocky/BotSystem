package com.botsystem.console.commands.command;

import com.botsystem.Main;
import com.botsystem.console.commands.ConsoleCommand;

public class ConsoleRestartCommand extends ConsoleCommand {

	private String cmd;
	
	public ConsoleRestartCommand(String cmd) {
		this.cmd = cmd;
	}
	
	@Override
	public void onInvoke(String[] arguments) {
		Main.destroyInstance();
		Main.createInstance();
		return;
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
