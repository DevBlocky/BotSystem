package com.blockybot.modules.commands.command;

import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.extensions.Utils;
import com.blockybot.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.time.Instant;

public class UptimeCommand extends BotCommand {
    private Instant startUptime;
    
    public UptimeCommand(String cmd) {
        super(cmd);
        startUptime = Instant.now();
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BlockyBotEmbed emb = new BlockyBotEmbed();

        emb.setTitle("BlockyBot Uptime");

        long[] uptime = Utils.calcUptime(startUptime);

        emb.addField(new MessageEmbed.Field("Milliseconds", Long.toString(uptime[0]), true));
        emb.addField(new MessageEmbed.Field("Seconds", Long.toString(uptime[1]), true));
        emb.addField(new MessageEmbed.Field("Minutes", Long.toString(uptime[2]), true));
        emb.addField(new MessageEmbed.Field("Hours", Long.toString(uptime[3]), true));
        emb.addField(new MessageEmbed.Field("Days", Long.toString(uptime[4]), true));
        emb.addField(new MessageEmbed.Field("Weeks", Long.toString(uptime[5]), true));

        m.getChannel().sendMessage(emb.build()).queue();
    }

    @Override
    public String getDesc() {
        return "Shows the current uptime of the bot";
    }
}
