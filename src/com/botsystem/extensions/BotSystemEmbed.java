package com.botsystem.extensions;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;

public class BotSystemEmbed {
    private EmbedBuilder emb;

    public BotSystemEmbed() {
        emb = new EmbedBuilder();
    }

    public BotSystemEmbed addField(MessageEmbed.Field field) {
        emb.addField(field);
        return this;
    }
    public BotSystemEmbed setTitle(String title) {
        emb.setTitle(title);
        return this;
    }
    public BotSystemEmbed setTitle(String title, String url) {
        emb.setTitle(title, url);
        return this;
    }
    public BotSystemEmbed setDescription(CharSequence desc) {
        emb.setDescription(desc);
        return this;
    }
    public BotSystemEmbed appendDescription(CharSequence aDesc) {
        emb.appendDescription(aDesc);
        return this;
    }
    public BotSystemEmbed createBlankField(boolean inline) {
        emb.addBlankField(inline);
        return this;
    }
    public boolean isEmpty() {
        return emb.isEmpty();
    }

    private void addBotSystemInfo() {
        emb
                .setAuthor("BotSystem", null, "https://cdn.discordapp.com/avatars/371181987391602690/e7b63af6cee7054f7a5deb4e488fe695.png")
                .setTimestamp(Instant.now())
                .setFooter("© BlockBa5her", null)
                .setColor(new Color(63, 135, 255));
    }

    public MessageEmbed build() {
        addBotSystemInfo();

        return emb.build();
    }
}
