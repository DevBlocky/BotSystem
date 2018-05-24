package com.botsystem.modules.commands.command;

import com.botsystem.Main;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.extensions.Utils;
import com.botsystem.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;

/**
 * A command to restart the bot's core
 * 
 * @author BlockBa5her
 *
 */
public class RestartCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    /**
     * Creates an instance of the "restart" command
     * 
     * @param cmd
     *            The command to invoke with
     * @param reqPerm
     *            The required permission to invoke
     */
    public RestartCommand(String cmd, String reqPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        // creating embed
        BotSystemEmbed emb = new BotSystemEmbed();

        // setting embed info
        emb.setTitle("Restarting...");
        emb.setTitle("The bot's core and modules will now be restarted");

        // sending embed to channel
        m.getChannel().sendMessage(emb.build()).complete();

        // create a thread to restart the bot on
        Utils.createTimeout(() -> {
            Main.destroyInstance();
            Main.createInstance();
        }, 0, "BotSystem Restart Thread");
    }

    @Override
    public String getDesc() {
        return "Restarts the bot and reconnects to discord API";
    }

    @Override
    public String getRequiredPerm() {
        return reqPerm;
    }

    @Override
    public String getCmd() {
        return cmd;
    }

}
