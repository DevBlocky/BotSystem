package com.botsystem.console.commands.command;

import com.botsystem.console.commands.ConsoleCommand;
import com.botsystem.extensions.Utils;

import java.time.Instant;

/**
 * A command used for showing the uptime for the bot
 *
 * @author BlockBa5her
 * (C) BlockBa5her
 */
public class ConsoleUptimeCommand extends ConsoleCommand {

    private String cmd;
    private Instant startUptime;

    public ConsoleUptimeCommand(String cmd) {
        this.cmd = cmd;
        this.startUptime = Instant.now();
    }

    @Override
    public void onInvoke(String[] arguments) {
        long[] uptime = Utils.calcUptime(startUptime);

        String out =
                uptime[0] + " milliseconds, " +
                uptime[1] + " seconds, " +
                uptime[2] + " minutes, " +
                uptime[3] + " hours, " +
                uptime[4] + " days, and " +
                uptime[5] + " weeks";

        System.out.println(out);
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getDesc() {
        return "Shows the uptime for the bot: [millis, seconds, minutes, hours, days, weeks]";
    }
}
