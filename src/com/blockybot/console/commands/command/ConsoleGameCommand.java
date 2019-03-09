package com.blockybot.console.commands.command;

import com.blockybot.console.commands.ConsoleCommand;
import com.blockybot.modules.display.DisplayModule;

public class ConsoleGameCommand extends ConsoleCommand {

    private String cmd;

    public ConsoleGameCommand(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void onInvoke(String[] arguments) {
        DisplayModule confModule = parent.getBot().getModule(DisplayModule.class);

        if (arguments.length < 1) {
            confModule.setGame(null);
            confModule.queueUpdateNow();
            System.out.println("Set the game to config default");
            return;
        }

        String newGame = String.join(" ", arguments);

        confModule.setGame(newGame);
        confModule.queueUpdateNow();
        System.out.println("Set the bot's game successfully");
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getDesc() {
        return "Sets the bot's game, displayed on the player card";
    }
}
