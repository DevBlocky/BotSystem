package com.botsystem.console.commands.command;

import com.botsystem.Main;
import com.botsystem.console.commands.ConsoleCommand;

public class ConsoleShutdownCommand extends ConsoleCommand {

	private String cmd;
	
	public ConsoleShutdownCommand(String cmd) {
		this.cmd = cmd;
	}
	
	@Override
	public void onInvoke(String[] arguments) {
		Main.destroyInstance();
	}

	@Override
	public String getCmd() {
		return cmd;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return "Shuts down the bot, and closes the program";
	}

}
