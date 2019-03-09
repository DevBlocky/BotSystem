package com.blockybot.extensions;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;

/**
 * Discord Embed wrapper for BlockyBot
 * 
 * @author BlockBa5her
 *
 */
public class BlockyBotEmbed {
    /**
     * The builder to be wrapped
     */
    private EmbedBuilder emb;

    /**
     * create the embed builder
     */
    public BlockyBotEmbed() {
        emb = new EmbedBuilder();
    }

    /**
     * Adds a field to the embed
     * 
     * @param field
     *            The field to add
     * @return This
     */
    public BlockyBotEmbed addField(MessageEmbed.Field field) {
        emb.addField(field);
        return this;
    }

    /**
     * Sets the title to the embed
     * 
     * @param title
     *            Embed title text
     * @return This
     */
    public BlockyBotEmbed setTitle(String title) {
        emb.setTitle(title);
        return this;
    }

    /**
     * Sets the title and the URL of the title
     * 
     * @param title
     *            Embed title text
     * @param url
     *            Link of the title
     * @return This
     */
    public BlockyBotEmbed setTitle(String title, String url) {
        emb.setTitle(title, url);
        return this;
    }

    /**
     * Sets the embed's description
     * 
     * @param desc
     *            Embed description text
     * @return This
     */
    public BlockyBotEmbed setDescription(CharSequence desc) {
        emb.setDescription(desc);
        return this;
    }

    /**
     * Appends text to the description of the embed
     * 
     * @param aDesc
     *            The text to append
     * @return This
     */
    public BlockyBotEmbed appendDescription(CharSequence aDesc) {
        emb.appendDescription(aDesc);
        return this;
    }

    /**
     * Creates a blank field in the embed
     * 
     * @param inline
     *            To create it as an inline field
     * @return This
     */
    public BlockyBotEmbed createBlankField(boolean inline) {
        emb.addBlankField(inline);
        return this;
    }

    /**
     * Check if the embed is empty
     * 
     * @return Whether the embed is empty
     */
    public boolean isEmpty() {
        return emb.isEmpty();
    }

    /**
     * Adds the BlockyBot information to the embed
     */
    private void addUserInfo() {
        emb
                // sets the author to BlockyBot and the url to the BlockyBot icon
                .setAuthor("BlockyBot", null,
                        "https://cdn.discordapp.com/avatars/442215890591678464/bd44cdf547795d4b121df709692554ca.png")
                // setting the timestamp to now
                .setTimestamp(Instant.now())
                // adding copyright symbol to footer
                .setFooter("© BlockBa5her", null)
                // setting color to a yellow
                .setColor(new Color(241, 196, 15));
    }

    /**
     * Builds the embed
     * 
     * @return The message embed from the built
     */
    public MessageEmbed build() {
    	addUserInfo(); // adding the required BlockyBot info

        return emb.build(); // returning the build
    }
}
