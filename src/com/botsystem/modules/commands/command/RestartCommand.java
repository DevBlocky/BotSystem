package com.botsystem.modules.commands.command;

import com.botsystem.Main;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;

public class RestartCommand extends BotCommand {

	private String cmd;
	private String reqPerm;
	
	public RestartCommand(String cmd, String reqPerm) {
		this.cmd = cmd;
		this.reqPerm = reqPerm;
	}
	
	@Override
	public void onInvoke(Message m, String[] args) {
		BotSystemEmbed emb = new BotSystemEmbed();
		
		emb.setTitle("Restarting...");
		emb.setTitle("The bot's core and modules will now be restarted");
		
		m.getChannel().sendMessage(emb.build()).complete();
		
		
		Thread t = new Thread(() -> {
			Main.destroyInstance();
			Main.createInstance();
		});
		t.setName("BotSystem Restart Thread");
		t.start();
	}

	@Override
	public String getDesc() {
		return "Restarts the bot and reconnects to discord API";
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
