package com.botsystem.modules.commands.command;

import com.botsystem.Main;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.extensions.Utils;
import com.botsystem.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;

/**
 * BotCommand to shutdown the bot
 * 
 * @author BlockBa5her
 *
 */
public class ShutdownCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    /**
     * Creates an instance of the "shutdown" command
     * 
     * @param cmd
     *            The command to invoke with
     * @param reqPerm
     *            The required permission to invoke
     */
    public ShutdownCommand(String cmd, String reqPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        // create embed
        BotSystemEmbed emb = new BotSystemEmbed();

        // setting embed info
        emb.setTitle("Shutting Down...");
        emb.setDescription("The bot is now currently in the process of being shutdown");

        // send the emb to the channel
        m.getChannel().sendMessage(emb.build()).complete();

        // shutting down in new thread
        Utils.createTimeout(Main::destroyInstance, 0, "BotSystem Shutdown Thread");
    }

    @Override
    public String getDesc() {
        return "Shuts down the bot cleanly";
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
