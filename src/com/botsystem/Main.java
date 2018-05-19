package com.botsystem;

import com.botsystem.console.commands.ConsoleCommand;
import com.botsystem.console.commands.command.ConsoleGameCommand;
import com.botsystem.console.commands.command.ConsoleHelpCommand;
import com.botsystem.console.commands.command.ConsoleNicknameCommand;
import com.botsystem.console.commands.command.ConsoleRestartCommand;
import com.botsystem.console.commands.command.ConsoleSayCommand;
import com.botsystem.console.commands.command.ConsoleShutdownCommand;
import com.botsystem.console.commands.command.ConsoleStatusCommand;
import com.botsystem.console.commands.command.ConsoleTestCommand;
import com.botsystem.console.commands.command.ConsoleUptimeCommand;
import com.botsystem.core.BotSystemModule;
import com.botsystem.exceptions.ExceptionHelper;
import com.botsystem.extensions.Pair;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.commands.BotCommandsModule;
import com.botsystem.modules.commands.command.BanCommand;
import com.botsystem.modules.commands.command.CommandsCommand;
import com.botsystem.modules.commands.command.GameCommand;
import com.botsystem.modules.commands.command.HelpCommand;
import com.botsystem.modules.commands.command.InstallHelpCommand;
import com.botsystem.modules.commands.command.KickCommand;
import com.botsystem.modules.commands.command.MonitorCommand;
import com.botsystem.modules.commands.command.NicknameCommand;
import com.botsystem.modules.commands.command.PruneCommand;
import com.botsystem.modules.commands.command.RestartCommand;
import com.botsystem.modules.commands.command.RoleInfoCommand;
import com.botsystem.modules.commands.command.SaveCommand;
import com.botsystem.modules.commands.command.ShutdownCommand;
import com.botsystem.modules.commands.command.StatusCommand;
import com.botsystem.modules.commands.command.UptimeCommand;
import com.botsystem.modules.display.DisplayModule;
import com.botsystem.modules.monitor.MonitorModule;
import com.botsystem.modules.noeveryone.NoEveryoneModule;
import com.botsystem.modules.permissions.PermissionsModule;
import com.botsystem.modules.pingpong.PingPongModule;
import com.botsystem.modules.report.ReportModule;
import com.botsystem.modules.suggestions.SuggestionsModule;
import com.botsystem.modules.welcome.WelcomeModule;

import net.dv8tion.jda.core.requests.RestAction;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

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
    
    private static BotBottle bottle;
    private static Thread botThread;

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
    
    private static void displayCliHelp() {
        StringBuilder output = new StringBuilder("botsystem.jar command arguments: \n");

        for (Option opt : COMMAND_LINE.getOptions()) { // going through CLI options

            output.append("    -").append(opt.getOpt()); // adding option to output
            if (opt.getLongOpt() != null) // if has long option
                output.append(", --").append(opt.getLongOpt()); // add long option to output

            output.append(" - ").append(opt.getDescription()).append("\n"); // adding desc and new line
        }

        System.out.println(output); // printing out
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
        	displayCliHelp();
            return; // returning before bot creation
        }

        CONFIG.configParse(); // parsing the .json config

        setPermissions(); // setting permissions from the config

        // DEBUG SHIT
        RestAction.setPassContext(true);
        RestAction.DEFAULT_FAILURE = Throwable::printStackTrace;

        createInstance();
    }
    
    public boolean isInstanceRunning() {
    	return botThread != null;
    }
    
    public static void createInstance() {
    	
    	if (botThread != null)
    		throw new RuntimeException("instance already running");
    	
        final ConsoleCommand[] CONSOLE_COMMANDS = {
                new ConsoleHelpCommand("help"),
                new ConsoleUptimeCommand("uptime"),
                new ConsoleSayCommand("say"),
                new ConsoleStatusCommand("status"),
                new ConsoleGameCommand("game"),
                new ConsoleNicknameCommand("nickname"),
                new ConsoleTestCommand("test"),
                new ConsoleShutdownCommand("shutdown"),
                new ConsoleRestartCommand("restart")
        };
        
        final BotSystemModule[] MODULES = {
                // "core" modules
                new DisplayModule(),
                new PermissionsModule(permissions),
                new MonitorModule(),

                // non-required modules (these interact with core modules though)
                new PingPongModule(),
                new BotCommandsModule(new BotCommand[] {
                        // essential core commands
                        new HelpCommand("help", "user"),
                        new CommandsCommand("commands", "user"),

                        // user commands
                        new UptimeCommand("uptime", "user"),
                        new InstallHelpCommand("installhelp", "user"),
                        new MonitorCommand("monitor", "user"),

                        // moderator commands
                        new RoleInfoCommand("roleinfo", "moderator"),
                        new KickCommand("kick", "moderator", "moderator"),
                        new PruneCommand("prune", "moderator"), 

                        // staff commands
                        new BanCommand("ban", "staff", "staff"),
                        new StatusCommand("status", "staff"),
                        new GameCommand("game", "staff"),
                        new NicknameCommand("nickname", "staff"),
                        new SaveCommand("save", "staff", "saves"),
                        new RestartCommand("restart", "staff"),
                        new ShutdownCommand("shutdown", "staff")
                }),
                new NoEveryoneModule("moderator"),
                new ReportModule(),
                new WelcomeModule(),
                new SuggestionsModule()
        };
    	
    	botThread = new Thread(() -> {
    		bottle = new BotBottle(MODULES, CONSOLE_COMMANDS);
    		bottle.start();
    	});
    	botThread.setName("BotSystem Main");
    	botThread.start();
    }
    public static void destroyInstance() {
    	if (botThread == null)
    		throw new RuntimeException("instance not running");
    	
		bottle.dispose();
    	bottle = null;
    	botThread.interrupt();
    	botThread = null;
    }
}
