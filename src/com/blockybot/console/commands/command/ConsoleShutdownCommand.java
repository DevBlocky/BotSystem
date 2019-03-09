package com.blockybot.console.commands.command;

import com.blockybot.Main;
import com.blockybot.console.commands.ConsoleCommand;
import com.blockybot.extensions.Utils;

/**
 * A command for shutting down the bot
 *
 * @author BlockBa5her (C) BlockBa5her
 */
public class ConsoleShutdownCommand extends ConsoleCommand {

    private String cmd;

    /**
     * Creates an instance of the command
     * 
     * @param cmd
     */
    public ConsoleShutdownCommand(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void onInvoke(String[] arguments) {
        // destroy the instance in a thread
        Utils.createTimeout(Main::destroyInstance, 0, "BlockyBot Shutdown Thread");
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getDesc() {
        // TODO Auto-generated method stub
        return "Shuts down the bot, and closes the program";
    }

}
