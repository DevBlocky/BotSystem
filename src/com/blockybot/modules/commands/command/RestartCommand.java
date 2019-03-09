package com.blockybot.modules.commands.command;

import com.blockybot.Main;
import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.extensions.Utils;
import com.blockybot.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;

/**
 * A command to restart the bot's core
 * 
 * @author BlockBa5her
 *
 */
public class RestartCommand extends BotCommand {
    /**
     * Creates an instance of the "restart" command
     * 
     * @param cmd
     *            The command to invoke with
     */
    public RestartCommand(String cmd) {
        super(cmd);
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        // creating embed
        BlockyBotEmbed emb = new BlockyBotEmbed();

        // setting embed info
        emb.setTitle("Restarting...");
        emb.setTitle("The bot's core and modules will now be restarted");

        // sending embed to channel
        m.getChannel().sendMessage(emb.build()).complete();

        // create a thread to restart the bot on
        Utils.createTimeout(() -> {
            Main.destroyInstance();
            Main.createInstance();
        }, 0, "BlockyBot Restart Thread");
    }

    @Override
    public String getDesc() {
        return "Restarts the bot and reconnects to discord API";
    }
}
