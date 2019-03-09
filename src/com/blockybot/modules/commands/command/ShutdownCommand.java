package com.blockybot.modules.commands.command;

import com.blockybot.Main;
import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.extensions.Utils;
import com.blockybot.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;

/**
 * BotCommand to shutdown the bot
 * 
 * @author BlockBa5her
 *
 */
public class ShutdownCommand extends BotCommand {
    /**
     * Creates an instance of the "shutdown" command
     * 
     * @param cmd
     *            The command to invoke with
     */
    public ShutdownCommand(String cmd) {
        super(cmd);
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        // create embed
        BlockyBotEmbed emb = new BlockyBotEmbed();

        // setting embed info
        emb.setTitle("Shutting Down...");
        emb.setDescription("The bot is now currently in the process of being shutdown");

        // send the emb to the channel
        m.getChannel().sendMessage(emb.build()).complete();

        // shutting down in new thread
        Utils.createTimeout(Main::destroyInstance, 0, "BlockyBot Shutdown Thread");
    }

    @Override
    public String getDesc() {
        return "Shuts down the bot cleanly";
    }
}
