package com.botsystem.modules.commands.command;

import com.botsystem.Main;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;

public class ShutdownCommand extends BotCommand {

	private String cmd;
	private String reqPerm;
	
	public ShutdownCommand(String cmd, String reqPerm) {
		this.cmd = cmd;
		this.reqPerm = reqPerm;
	}
	
	@Override
	public void onInvoke(Message m, String[] args) {
		BotSystemEmbed emb = new BotSystemEmbed();
		
		emb.setTitle("Shutting Down...");
		emb.setDescription("The bot is now currently in the process of being shutdown");
		
		Thread t = new Thread(() -> {
			Main.destroyInstance();
		});
		t.setName("BotSystem Shutdown Thread");
		t.start();
	}

	@Override
	public String getDesc() {
		return "Shuts down the bot cleanly";
	}

	@Override
	public String getRequiredPerm() {
		return reqPerm;
	}

	@Override
	public String getCmd() {
		return cmd;
	}

}
