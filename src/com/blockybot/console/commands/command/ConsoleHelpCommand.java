package com.blockybot.console.commands.command;

import com.blockybot.console.commands.ConsoleCommand;

public class ConsoleHelpCommand extends ConsoleCommand {

    private String cmd;

    public ConsoleHelpCommand(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void onInvoke(String[] arguments) {
        String out = "Console Commands:\n";
        for (ConsoleCommand c : parent.getCommands()) {
            out += c.getCmd() + " - " + c.getDesc() + "\n";
        }

        System.out.print(out);
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getDesc() {
        return "Displays all of the available console commands (this command)";
    }
}
