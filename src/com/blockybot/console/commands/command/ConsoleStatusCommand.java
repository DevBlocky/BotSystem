package com.blockybot.console.commands.command;

import com.blockybot.console.commands.ConsoleCommand;
import com.blockybot.extensions.Utils;
import com.blockybot.modules.display.DisplayModule;

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
        DisplayModule confModule = parent.getBot().getModule(DisplayModule.class);

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
