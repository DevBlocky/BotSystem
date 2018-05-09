package com.botsystem.modules.suggestions;

import java.awt.Color;
import java.time.Instant;

import com.botsystem.Main;
import com.botsystem.core.BotSystemModule;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SuggestionsModule extends BotSystemModule {
	
	private static class SuggestionEmbed {
		private EmbedBuilder emb;
		private User suggester;
		
		private SuggestionEmbed(User suggester) {
			emb = new EmbedBuilder();
			this.suggester = suggester;
		}
		
		public SuggestionEmbed addField(MessageEmbed.Field field) {
			emb.addField(field);
			return this;
		}
		
		public MessageEmbed build() {
			emb
					.setAuthor(suggester.getName(), null, suggester.getAvatarUrl())
	                .setTimestamp(Instant.now())
	                .setFooter("Voice your opinion using the reactions below", null)
					.setColor(new Color(255, 65, 65));
			
			return emb.build();
		}
	}
	
	private TextChannel getSuggestionChannel() {
		String cId = Main.CONFIG.getString("suggest-channel");
		
		Guild[] guilds = bot.getGuilds();
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
	public void onStart() {
		bot.addEvent(MessageReceivedEvent.class, e -> {
			MessageReceivedEvent mre = (MessageReceivedEvent)e;
			
			if (mre.getAuthor().isBot())
				return;
			
			TextChannel c = getSuggestionChannel();
			if (mre.getChannel().getId().equals(c.getId())) {
				makeSuggestion(mre.getMessage(), c);
			}
		});
	}
	
	private void makeSuggestion(Message m, TextChannel postChannel) {
		String msgContent = m.getContentRaw();
		
		SuggestionEmbed emb = new SuggestionEmbed(m.getAuthor());
		m.delete().queue();
		emb.addField(new MessageEmbed.Field("Suggestion", msgContent, false));
		// emb.addField(new MessageEmbed.Field("Voting", "Voice your opinion using the reactions below", false));
		
		Message newM = postChannel.sendMessage(emb.build()).complete();
		
		newM.addReaction("\u2705").queue();
		newM.addReaction("\u274C").queue();
	}
}
