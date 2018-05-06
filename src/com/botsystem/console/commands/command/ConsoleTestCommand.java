package com.botsystem.console.commands.command;

import com.botsystem.console.commands.ConsoleCommand;

/**
 * A command used for testing
 *
 * @author BlockBa5her
 * (C) BlockBa5her
 */
public class ConsoleTestCommand extends ConsoleCommand {

    private String cmd;

    public ConsoleTestCommand(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void onInvoke(String[] arguments) {
        System.out.println("Ping back!");
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getDesc() {
        return "Used for testing";
    }
}
