package com.botsystem.console.commands;

import com.botsystem.Debug;
import com.botsystem.core.BotSystem;

import java.util.*;

/**
 * A thread based system for getting inputted console commands
 *
 * @author BlockBa5her
 * (C) BlockBa5her
 */
public class ConsoleCommands extends Thread implements Iterable<ConsoleCommand> {

    /**
     * Information about a ConsoleCommand, used for callbacks n such
     */
    private class CommandInvokeInformation {
        /**
         * The arguments given with the command
         */
        String[] arguments;
        /**
         * The found command given with the "invokeCmd"
         */
        ConsoleCommand commandToInvoke;

        /**
         * Initializes the "CommandInvokeInformation" class
         * @param arguments The command arguments
         * @param cmd The ConsoleCommand that goes with everything
         */
        public CommandInvokeInformation(String[] arguments, ConsoleCommand cmd) {
            this.arguments = arguments;
            commandToInvoke = cmd;
        }
    }

    private LinkedList<ConsoleCommand> commands;
    /**
     * Used for finding the next line of the console
     */
    private Scanner scan;
    private BotSystem bot;

    /**
     * Initializes the ConsoleCommands class, with the bot
     * @param bot The bot to use the commands with
     */
    public ConsoleCommands(BotSystem bot) {
        this.bot = bot;
        commands = new LinkedList<>();
        scan = new Scanner(System.in); // create a scanner with the console input stream
    }

    /**
     * Getter for the BotSystem API
     * @return Returning BotSystem API
     */
    public BotSystem getBot() {
        return bot;
    }

    /**
     * Getter for the ConsoleCommands
     * @return All of the ConsoleCommand in the class
     */
    public ConsoleCommand[] getCommands() {
        return commands.toArray(new ConsoleCommand[0]);
    }

    /**
     * Initializes ConsoleCommands with preset ConsoleCommand[]
     * @param bot The bot to use the commands with
     * @param initialCommands The commands to add at the start
     */
    public ConsoleCommands(BotSystem bot, ConsoleCommand[] initialCommands) {
        this(bot);
        addCommandRange(Arrays.asList(initialCommands)); // adding the range of commands
    }

    /**
     * Adds a console command to total commands
     * @param cmd The command to add
     */
    public void addCommand(ConsoleCommand cmd) {
        cmd.setParent(this); // setting the command parent
        commands.add(cmd); // adding it to list of commands
    }

    /**
     * Adds a range of commands to the total commands
     * @param cmds The commands to add
     */
    public void addCommandRange(Iterable<ConsoleCommand> cmds) {
        for (ConsoleCommand cmd : cmds) // for ever command
            addCommand(cmd); // call the addCommand method above
    }

    /**
     * Get all of the commands
     * @return The iterator for the ConsoleCommands
     */
    @Override
    public Iterator<ConsoleCommand> iterator() {
        return commands.iterator();
    }

    /**
     * A loop for the thread containing the essentials for the commands
     */
    @Override
    public void run() {
        while (true) {
        	String input = null;
            try {
            	input = scan.nextLine(); // waits thread until next line
            } catch (NoSuchElementException e) { // usually occurs when process exit
            	Debug.trace("ConsoleCommands safe console reader shutdown enabled");
            	break;
            }

            CommandInvokeInformation invokeInfo = parseCmd(input); // parse the command with parseCmd()

            if (invokeInfo != null) { // if command exists in list of commands
                invokeInfo.commandToInvoke.onInvoke(invokeInfo.arguments); // invoke command with arguments
            } else { // otherwise
                System.out.println("Command not found!"); // print out saying the command wasn't found
            }
        }
    }

    /**
     * Parsed a given string to find the command
     * @param raw The raw string to parse
     * @return Command Information with what parsed data it found
     */
    private CommandInvokeInformation parseCmd(String raw) {
        raw = raw.trim(); // trimmy boi
        List<String> split = new LinkedList<>(Arrays.asList(raw.split(" "))); // basically splitting the string
        String cmd = split.remove(0); // finding the invokeCmd

        for (ConsoleCommand command : commands) { // for every command
            if (command.getCmd().equals(cmd.toLowerCase())) { // if the commandCmd is equal to the given cmd
                return new CommandInvokeInformation(split.toArray(new String[0]), command); // return the parsed information
            }
        }

        return null; // return null if not found
    }
}
