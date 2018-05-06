package com.botsystem.console.commands.command;

import com.botsystem.console.commands.ConsoleCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A command for putting a specified message into a specified chat
 *
 * @author BlockBa5her
 * (C) BlockBa5her
 */
public class ConsoleSayCommand extends ConsoleCommand {

    private String cmd;

    public ConsoleSayCommand(String cmd) {
        this.cmd = cmd;
    }

    public TextChannel findFirstChannelWithName(String name) {
        for (Guild g : parent.getBot().getGuilds()) {
            for (TextChannel c : g.getTextChannels()) {
                if (c.getName().equals(name.toLowerCase())) {
                    return c;
                }
            }
        }

        return null;
    }

    @Override
    public void onInvoke(String[] arguments) {
        if (arguments.length < 2) {
            System.out.println("Not enough arguments were given");
            return;
        }

        List<String> args = new LinkedList<>(Arrays.asList(arguments));
        String cName = args.remove(0);

        TextChannel c = findFirstChannelWithName(cName);

        if (c == null) {
            System.out.println("That channel doesn't exist!");
            return;
        }

        String msg = String.join(" ", args);
        c.sendMessage(msg).queue();
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getDesc() {
        return "Puts a specified message into a specified chat";
    }
}
