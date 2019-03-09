package com.blockybot;

import com.blockybot.console.commands.ConsoleCommand;
import com.blockybot.console.commands.command.*;
import com.blockybot.core.BlockyBotModule;
import com.blockybot.core.BotBottle;
import com.blockybot.exceptions.ExceptionHelper;
import com.blockybot.modules.adminlog.AdminLogModule;
import com.blockybot.modules.commands.BotCommand;
import com.blockybot.modules.commands.BotCommandsModule;
import com.blockybot.modules.commands.command.*;
import com.blockybot.modules.display.DisplayModule;
import com.blockybot.modules.filter.FilterModule;
import com.blockybot.modules.monitor.MonitorModule;
import com.blockybot.modules.noeveryone.NoEveryoneModule;
import com.blockybot.modules.permissions.PermissionsModule;
import com.blockybot.modules.pingpong.PingPongModule;
import com.blockybot.modules.report.ReportModule;
import com.blockybot.modules.welcome.WelcomeModule;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Main class for startup, startup in main(String[]) method
 *
 * @author BlockBa5her (C) BlockBa5her
 */
public class Main {
	private static JSONObject config;
	private final static String configFile = "bot-conf.json";

	public static void reloadConfig() {
		try {
			// reading the file
			File fr = new File(configFile); // get file obj
			FileInputStream fis = new FileInputStream(fr); // create input stream from file
			ByteBuffer bf = ByteBuffer.allocate((int) fr.length()); // new byte buffer with fr length
			fis.read(bf.array()); // reading file into buffer
			fis.close(); // close the stream
			String str = new String(bf.array(), "UTF-8"); // get raw string out of data

			// parsing JSON
			config = new JSONObject(str);
			Debug.trace("loaded config into json buffer");
		} catch (FileNotFoundException e) {
			ExceptionHelper.throwException(e);
		} catch (IOException e) {
			ExceptionHelper.throwException(e);
		}
	}

	public static JSONObject getConf() {
		return config;
	}

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
	 * Thread that is executed whenever the program is about to be shutdown
	 */
	private static final Thread shutdownThread = new Thread(() -> {
		if (bottle != null) {
			Debug.trace("disposing bottle for program shutdown");
			destroyInstance();
			Debug.trace("done disposing bottle");
		}
		Debug.trace("ending program...");
	});

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
	 * 
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
		} catch (ParseException e) { // if parse exception
			ExceptionHelper.throwException(e); // throw for the exception handler to take
		}
	}

	private static void displayCliHelp() {
		StringBuilder output = new StringBuilder("blockybot.jar command arguments: \n");

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
	 * 
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

		reloadConfig();

		// DEBUG SHIT
		RestAction.setPassContext(true);
		RestAction.DEFAULT_FAILURE = Throwable::printStackTrace;
		createInstance();

		// adding shutdown thread
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}

	public boolean isInstanceRunning() {
		return botThread != null;
	}

	private static AdminLogModule setupAdminLog(AdminLogModule log) {
		log.addLoggedEvent(GuildMemberJoinEvent.class, (_e) -> {
			GuildMemberJoinEvent e = (GuildMemberJoinEvent) _e;

			JSONObject baseObj = new JSONObject();
			baseObj.put("nick", e.getUser().getName());
			baseObj.put("discrim", e.getUser().getDiscriminator());
			baseObj.put("id", e.getUser().getId());

			return baseObj;
		});
		log.addLoggedEvent(GuildMemberLeaveEvent.class, (_e) -> {
			GuildMemberLeaveEvent e = (GuildMemberLeaveEvent) _e;

			JSONObject baseObj = new JSONObject();
			baseObj.put("nick", e.getUser().getName());
			baseObj.put("discrim", e.getUser().getDiscriminator());
			baseObj.put("id", e.getUser().getId());

			return baseObj;
		});
		log.addLoggedEvent(MessageReceivedEvent.class, (_e) -> {
			MessageReceivedEvent e = (MessageReceivedEvent) _e;

			if (e.getAuthor().isBot())
				return null;

			JSONObject baseObj = new JSONObject();

			JSONObject userObj = new JSONObject();
			userObj.put("id", e.getAuthor().getId());
			userObj.put("nick", e.getAuthor().getName());
			userObj.put("discrim", e.getAuthor().getDiscriminator());
			baseObj.put("user-info", userObj);

			JSONObject messageObj = new JSONObject();
			messageObj.put("id", e.getMessage().getId());
			messageObj.put("text", e.getMessage().getContentRaw().replaceAll("```", "'''"));
			messageObj.put("channel-id", e.getMessage().getChannel().getId());
			messageObj.put("channel-name", e.getMessage().getChannel().getName());
			baseObj.put("message-info", messageObj);

			return baseObj;
		});
		log.addLoggedEvent(MessageDeleteEvent.class, (_e) -> {
			MessageDeleteEvent e = (MessageDeleteEvent) _e;

			JSONObject baseObj = new JSONObject();

			JSONObject channelObj = new JSONObject();
			channelObj.put("id", e.getChannel().getId());
			channelObj.put("name", e.getChannel().getName());
			baseObj.put("channel-info", channelObj);

			JSONObject messageObj = new JSONObject();
			messageObj.put("id", e.getMessageId());
			baseObj.put("message-info", messageObj);

			return baseObj;
		});

		return log;
	}

	public static void createInstance() {
		if (botThread != null)
			throw new RuntimeException("instance already running");

		final ConsoleCommand[] CONSOLE_COMMANDS = { new ConsoleHelpCommand("help"), new ConsoleUptimeCommand("uptime"),
				new ConsoleSayCommand("say"), new ConsoleStatusCommand("status"), new ConsoleGameCommand("game"),
				new ConsoleNicknameCommand("nickname"), new ConsoleTestCommand("test"),
				new ConsoleShutdownCommand("shutdown"), new ConsoleRestartCommand("restart") };

		final BlockyBotModule[] MODULES = {
				// "core" modules
				new DisplayModule(), new PermissionsModule(), new MonitorModule(),

				// non-required modules (these interact with core modules though)
				new PingPongModule(), new BotCommandsModule(new BotCommand[] {
						// essential core commands
						new HelpCommand("help"), new CommandsCommand("commands"),

						// user commands
						new UptimeCommand("uptime"), new DsInstallCommand("dsinstall"),
						new MonitorCommand("monitor"),

						// moderator commands
						new RoleInfoCommand("roleinfo"), new KickCommand("kick"),
						new PruneCommand("prune"),

						// staff commands
						new BanCommand("ban"), new BanIdCommand("banid"),
						new StatusCommand("status"), new GameCommand("game"),
						new NicknameCommand("nickname"), new SaveCommand("save", "saves"),
						new RestartCommand("restart"), new ShutdownCommand("shutdown") }),
				new NoEveryoneModule(), new ReportModule(), new WelcomeModule(),
				setupAdminLog(new AdminLogModule()), new FilterModule() };

		botThread = new Thread(() -> {
			bottle = new BotBottle(MODULES, CONSOLE_COMMANDS);
			bottle.start();
		});
		botThread.setName("BlockyBot Main");
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
