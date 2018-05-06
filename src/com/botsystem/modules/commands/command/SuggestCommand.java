package com.botsystem.modules.commands.command;

import com.botsystem.Main;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

public class SuggestCommand extends BotCommand {
	
	private String cmd;
	private String reqPerm;
	
	public SuggestCommand(String cmd, String reqPerm) {
		this.cmd = cmd;
		this.reqPerm = reqPerm;
	}
	
	private TextChannel getSuggestionChannel() {
		String cId = Main.conf.getString("suggest-channel");
		
		Guild[] guilds = parent.getBot().getGuilds();
		for (Guild g : guilds) {
			for (TextChannel c : g.getTextChannels()) {
				if (c.getId().equals(cId)) {
					return c;
				}
			}
		}
		
		return null;
	}

	@Override
	public void onInvoke(Message m, String[] args) {
		BotSystemEmbed emb = new BotSystemEmbed();
		
		if (args.length == 0) {
			emb.setTitle("No Suggestion Specified");
			emb.setDescription("You did not supply a suggestion text, you must add a suggestion text after the command");
			m.getChannel().sendMessage(emb.build()).queue();
			return;
		}
		
		String suggestion = String.join(" ", args);
		TextChannel c = getSuggestionChannel();
		
		// \u2705 = :white_check_mark:
		// \u274C = :x:
		
		emb.addField(new MessageEmbed.Field("From", m.getAuthor().getAsMention(), false));
		emb.addField(new MessageEmbed.Field("Suggestion", suggestion + "\n", false));
		emb.createBlankField(false);
		emb.addField(new MessageEmbed.Field("Community Answer", ""
				+ "\u2705 means \"I like the idea\"\n"
				+ "\u274C means \"Don't do this\"", 
				false));
		Message newM = c.sendMessage(emb.build()).complete();
		
		newM.addReaction("\u2705").queue();
		newM.addReaction("\u274C").queue();
	}

	@Override
	public String getDesc() {
		return "Adds your suggestion to the suggestions channel";
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
