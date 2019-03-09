package com.blockybot.console.commands.command;

import com.blockybot.Main;
import com.blockybot.console.commands.ConsoleCommand;
import com.blockybot.extensions.Utils;

/**
 * Command for restarting the bot's core
 * 
 * @author BlockBa5her
 *
 */
public class ConsoleRestartCommand extends ConsoleCommand {

    private String cmd;

    /**
     * Creates instance of the command
     * 
     * @param cmd
     */
    public ConsoleRestartCommand(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void onInvoke(String[] arguments) {
        // destroy and create instance on a new thread
        Utils.createTimeout(() -> {
            Main.destroyInstance();
            Main.createInstance();
        }, 0, "BlockyBot Restart Thread");
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getDesc() {
        return "Restarts bot's core";
    }

}
