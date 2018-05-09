package com.botsystem;

import com.botsystem.console.commands.ConsoleCommand;
import com.botsystem.console.commands.ConsoleCommands;
import com.botsystem.console.commands.command.*;
import com.botsystem.core.BotSystem;
import com.botsystem.core.BotSystemModule;
import com.botsystem.exceptions.ExceptionHelper;
import com.botsystem.extensions.Pair;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.commands.BotCommandsModule;
import com.botsystem.modules.commands.command.*;
import com.botsystem.modules.config.ConfigModule;
import com.botsystem.modules.noeveryone.NoEveryoneModule;
import com.botsystem.modules.permissions.PermissionsModule;
import com.botsystem.modules.pingpong.PingPongModule;
import com.botsystem.modules.report.ReportModule;
import com.botsystem.modules.suggestions.SuggestionsModule;
import com.botsystem.modules.welcome.WelcomeModule;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.requests.RestAction;
import org.apache.commons.cli.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;

/**
 * Main class for startup, startup in main(String[]) method
 *
 * @author BlockBa5her
 * (C) BlockBa5her
 */
public class Main {

    /**
     * The BotConfig object, for the entire project
     */
    public static final BotConfig CONFIG = new BotConfig("bot-conf.json");

    /**
     * Permissions (setup in setPermissions())
     */
    private static final LinkedList<Pair<String, String>> permissions =
            new LinkedList<>();

    /**
     * Parser for given commands
     */
    private static final CommandLineParser cliParser = new DefaultParser();
    /**
     * The CommandLine arguments
     */
    public static CommandLine COMMAND_LINE;

    /**
     * ConsoleCommands, for user input and such
     */
    private static ConsoleCommands consoleCommands;

    /**
     * Sets the global thread exception handler
     */
    private static void setExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> { // add default thread exception catch
            System.err.println("Uncaught exception has caused program to be unstable...");
            System.err.println();

            System.err.println("Thread: \"" + t.getName() + "\""); // logging the thread that it happened on
            System.err.println(ExceptionHelper.getFullExceptionString(e)); // printing the full exception

            System.exit(3); // exiting with code "3"
        });
    }

    /**
     * Parses the CLI options using org.apache.cli
     * @param cliArgs The command arguments to parse
     */
    private static void parseCliOptions(String[] cliArgs) {
        try {
            Options o = new Options(); // cli options

            // add options
            Option debugOpt = new Option("d", "debug", false, "turns on application debug logging");
            Option helpOpt = new Option("help", false, "displays this");
            // adding options
            o.addOption(debugOpt);
            o.addOption(helpOpt);

            // parsing the options given in startup
            COMMAND_LINE = cliParser.parse(o, cliArgs);
        }
        catch (ParseException e) { // if parse exception
            ExceptionHelper.throwException(e); // throw for the exception handler to take
        }
    }

    /**
     * Sets the permissions from the .json config file
     */
    private static void setPermissions() {
        JSONArray p = CONFIG.getArray("permissions"); // getting permissions from json config
        for (Object obj : p) {
            JSONObject jObj = (JSONObject)obj; // getting object
            permissions.add(new Pair<>((String) jObj.get("name"), (String) jObj.get("id"))); // adding permission
            Debug.trace("adding permissions: " + jObj.get("name") + " " + jObj.get("id")); // tracing information
        }
    }

    /**
     * Main method for the entire bot
     * @param args The specified CLI arguments
     */
    public static void main(String[] args) {
        // call before events
        setExceptionHandler();
        parseCliOptions(args);

        // if has options "help", show and return
        if (COMMAND_LINE.hasOption("help")) {
            StringBuilder output = new StringBuilder("botsystem.jar command arguments: \n");

            for (Option opt : COMMAND_LINE.getOptions()) { // going through CLI options

                output.append("    -").append(opt.getOpt()); // adding option to output
                if (opt.getLongOpt() != null) // if has long option
                    output.append(", --").append(opt.getLongOpt()); // add long option to output

                output.append(" - ").append(opt.getDescription()).append("\n"); // adding desc and new line
            }

            System.out.println(output); // printing out
            return; // returning before bot creation
        }

        CONFIG.configParse(); // parsing the .json config

        setPermissions(); // setting permissions from the config

        // DEBUG SHIT
        RestAction.setPassContext(true);
        RestAction.DEFAULT_FAILURE = Throwable::printStackTrace;

        // getting the token from config
        final String TOKEN = CONFIG.getString("token");

        BotSystem bot = new BotSystem(TOKEN); // creating the bot

        // adding modules
        bot.addModuleRange(new BotSystemModule[] {
                // "core" modules
                new ConfigModule(),
                new PermissionsModule(permissions),

                // non-required modules (these interact with core modules though)
                new PingPongModule(),
                new BotCommandsModule(new BotCommand[] {
                        // essential core commands
                        new HelpCommand("help", "user"),
                        new CommandsCommand("commands", "user"),

                        // user commands
                        new UptimeCommand("uptime", "user"),
                        new InstallHelpCommand("installhelp", "user"),

                        // moderator commands
                        new RoleInfoCommand("roleinfo", "moderator"),
                        new KickCommand("kick", "moderator", "moderator"),

                        // staff commands
                        new BanCommand("ban", "staff", "staff"),
                        new StatusCommand("status", "staff"),
                        new GameCommand("game", "staff"),
                        new NicknameCommand("nickname", "staff"),
                        new SaveCommand("save", "staff", "saves")
                }),
                new NoEveryoneModule("moderator"),
                new ReportModule(),
                new WelcomeModule(),
                new SuggestionsModule()
        });

        // when the bot is ready
        bot.addEvent(ReadyEvent.class, e -> {
            Debug.trace("initializing console commands, and command modules");
            // setup and add console commands
            consoleCommands = new ConsoleCommands(bot, new ConsoleCommand[] {
                    new ConsoleHelpCommand("help"),
                    new ConsoleUptimeCommand("uptime"),
                    new ConsoleSayCommand("say"),
                    new ConsoleStatusCommand("status"),
                    new ConsoleGameCommand("game"),
                    new ConsoleNicknameCommand("nickname"),
                    new ConsoleTestCommand("test"),
            });
            consoleCommands.start();
        });

        try {
            bot.login(); // tryna start bot
        } catch (Exception e) { // if exception
            System.err.println("Whoops! Looks like there was a problem with the bot...\n");
            System.err.println(ExceptionHelper.getFullExceptionString(e));
            System.exit(2); // exit with code "2"
        }
    }
}
