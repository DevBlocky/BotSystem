package com.blockybot.modules.commands.command;

import com.blockybot.Main;
import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 * A bot command to display helpful information about the bot
 * 
 * @author BlockBa5her
 *
 */
public class HelpCommand extends BotCommand {
    /**
     * Creates an Instance of the "help" command
     * 
     * @param cmd
     *            The command to invoke with
     */
    public HelpCommand(String cmd) {
        super(cmd);
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        // create embed
        BlockyBotEmbed emb = new BlockyBotEmbed();

        /*
         * Below is basically the help command
         * 
         * The "help" command just displays the bot's information Because no operations
         * are taking place, it's a very simple command
         */

        emb.setTitle("BlockyBot Help");

        emb.addField(new MessageEmbed.Field("Information", ""
                + "BlockyBot is a simple bot built for the Blocky Script discord server.\n\n"
                + "Note: This is a **WIP** (Work in progress), so not all features may be available at the moment", false));
        emb.addField(
                new MessageEmbed.Field("Commands", "" + "Use `^commands` to view all of BlockyBot's commands", false));
        emb.addField(new MessageEmbed.Field("Version", "" + Main.getConf().getString("version"), false));

        m.getChannel().sendMessage(emb.build()).queue();
    }

    @Override
    public String getDesc() {
        return "Displays technical information about BlockyBot";
    }
}
