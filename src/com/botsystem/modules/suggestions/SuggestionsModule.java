package com.botsystem.modules.suggestions;

import java.awt.Color;
import java.time.Instant;

import com.botsystem.Main;
import com.botsystem.core.BotSystemModule;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
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
	
	@Override
	public void onStart() {
		bot.addEvent(MessageReceivedEvent.class, e -> {
			MessageReceivedEvent mre = (MessageReceivedEvent)e;
			
			if (mre.getAuthor().isBot())
				return;
			
			if (mre.getChannel().getId().equals(Main.CONFIG.getString("suggest-channel"))) {
				makeSuggestion(mre.getMessage());
			}
		});
	}
	
	private void makeSuggestion(Message m) {
		String msgContent = m.getContentRaw();
		
		SuggestionEmbed emb = new SuggestionEmbed(m.getAuthor());
		m.delete().complete();
		emb.addField(new MessageEmbed.Field("Suggestion", msgContent, false));
		// emb.addField(new MessageEmbed.Field("Voting", "Voice your opinion using the reactions below", false));
		
		Message newM = m.getChannel().sendMessage(emb.build()).complete();
		
		newM.addReaction("\u2705").queue();
		newM.addReaction("\u274C").queue();
	}
}
