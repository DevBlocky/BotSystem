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

/**
 * Module for BotSystem that makes a channel into a suggestion channel
 * 
 * @author BlockBa5her
 *
 */
public class SuggestionsModule extends BotSystemModule {

    /**
     * The EmbedBuilder wrapper for a suggestion embed
     * 
     * @author BlockBa5her
     *
     */
    private static class SuggestionEmbed {
        private EmbedBuilder emb; // the base discord builder
        private User suggester; // the suggester for the embed

        /**
         * Creates an instance of the SuggestionEmbed wrapper
         * 
         * @param suggester
         *            The suggester in the suggestion
         */
        private SuggestionEmbed(User suggester) {
            emb = new EmbedBuilder(); // creating embed builder
            this.suggester = suggester; // setting suggester
        }

        /**
         * Adds a field to the EmbedBuilder for build
         * 
         * @param field
         *            The field to add in the Embed
         * @return Returns current object
         */
        public SuggestionEmbed addField(MessageEmbed.Field field) {
            emb.addField(field); // adding field
            return this; // return this for stringed command (em.addField().addField())
        }

        /**
         * Builds the embed Also adds required suggestion syntax
         * 
         * @return
         */
        public MessageEmbed build() {
            emb.setAuthor(suggester.getName(), null, suggester.getAvatarUrl()) // setting author name and pic url
                    .setTimestamp(Instant.now()) // setting timestamp to now
                    .setFooter("Voice your opinion using the reactions below", null) // Adding footer to alert users of
                                                                                     // what to do
                    .setColor(new Color(255, 65, 65)); // setting color to a red

            return emb.build(); // building and returning the embed after everything
        }
    }

    @Override
    public void onStart() {
        // on the event of MessageRecieved
        bot.addEvent(MessageReceivedEvent.class, e -> {

            if (e.getAuthor().isBot()) // if it's a bot saying something return
                return;

            // if the channel id is equal to the config's suggestion channel id
            if (e.getChannel().getId().equals(Main.CONFIG.getString("suggest-channel"))) {
                makeSuggestion(e.getMessage()); // make a suggestion using method below
            }
        });
    }

    /**
     * Adds the suggestion syntax from the given message
     * 
     * @param m
     *            The message of the suggestion
     */
    private void makeSuggestion(Message m) {
        String msgContent = m.getContentRaw();

        SuggestionEmbed emb = new SuggestionEmbed(m.getAuthor()); // creating the suggestion embed from the author
        m.delete().complete(); // deleting the message sent for the suggestion
        emb.addField(new MessageEmbed.Field("Suggestion", msgContent, false)); // adding the suggestion field with
                                                                               // information

        // creating, sending, and getting the message
        Message newM = m.getChannel().sendMessage(emb.build()).complete();

        // adds voting reactions
        newM.addReaction("\u2705").queue();
        newM.addReaction("\u274C").queue();
    }
}
