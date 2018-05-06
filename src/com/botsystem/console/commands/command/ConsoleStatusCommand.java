package com.botsystem.console.commands.command;

import com.botsystem.console.commands.ConsoleCommand;
import com.botsystem.extensions.Utils;
import com.botsystem.modules.config.ConfigModule;

import java.util.Map;

public class ConsoleStatusCommand extends ConsoleCommand {

    private String cmd;
    private Map<String, Utils.StatusInfo> possible;

    public ConsoleStatusCommand(String cmd) {
        this.cmd = cmd;
        possible = Utils.getPossibleStatuses();
    }

    @Override
    public void onInvoke(String[] arguments) {
        ConfigModule confModule = parent.getBot().getModule(ConfigModule.class);

        if (arguments.length < 1) {
            confModule.setStatus(null);
            confModule.queueUpdateNow();
            System.out.println("Set the status to config default");
            return;
        }

        String newStatus = arguments[0].toLowerCase();

        if (possible.containsKey(newStatus)) {
            confModule.setStatus(possible.get(newStatus).getInternal());
            confModule.queueUpdateNow();
            System.out.println("Set the bot's status to " + newStatus);
        } else {
            System.out.println("Invalid status given");
        }
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getDesc() {
        return "Sets the bot's online status, from the given input";
    }
}
