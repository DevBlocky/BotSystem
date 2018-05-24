package com.botsystem.extensions;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;

/**
 * Discord Embed wrapper for BotSystem
 * 
 * @author BlockBa5her
 *
 */
public class BotSystemEmbed {
    /**
     * The builder to be wrapped
     */
    private EmbedBuilder emb;

    /**
     * create the embed builder
     */
    public BotSystemEmbed() {
        emb = new EmbedBuilder();
    }

    /**
     * Adds a field to the embed
     * 
     * @param field
     *            The field to add
     * @return This
     */
    public BotSystemEmbed addField(MessageEmbed.Field field) {
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
    public BotSystemEmbed setTitle(String title) {
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
    public BotSystemEmbed setTitle(String title, String url) {
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
    public BotSystemEmbed setDescription(CharSequence desc) {
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
    public BotSystemEmbed appendDescription(CharSequence aDesc) {
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
    public BotSystemEmbed createBlankField(boolean inline) {
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
     * Adds the BotSystem information to the embed
     */
    private void addBotSystemInfo() {
        emb
                // sets the author to BotSystem and the url to the BotSystem icon
                .setAuthor("BotSystem", null,
                        "https://cdn.discordapp.com/avatars/371181987391602690/e7b63af6cee7054f7a5deb4e488fe695.png")
                // setting the timestamp to now
                .setTimestamp(Instant.now())
                // adding copyright symbol to footer
                .setFooter("© BlockBa5her", null)
                // setting color to a baby blue (because of DS)
                .setColor(new Color(63, 135, 255));
    }

    /**
     * Builds the embed
     * 
     * @return The message embed from the built
     */
    public MessageEmbed build() {
        addBotSystemInfo(); // adding the required BotSystem info

        return emb.build(); // returning the build
    }
}
