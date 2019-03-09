package com.blockybot.modules.commands;

import com.blockybot.Debug;
import com.blockybot.core.BlockyBot;
import com.blockybot.core.BlockyBotModule;
import com.blockybot.modules.permissions.PermissionsModule;
import com.sun.istack.internal.NotNull;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.LinkedList;

import org.json.JSONObject;

import java.util.Arrays;

public class BotCommandsModule extends BlockyBotModule {
    private LinkedList<BotCommand> commands;
    private PermissionsModule permissions;

    /**
     * Creates an instance of the BotCommandsModule class
     */
    public BotCommandsModule() {
        commands = new LinkedList<>(); // creating commands list
    }

    /**
     * Creates an instance of the BotCommandsModule class with predefined commands
     * 
     * @param commands
     *            The predefined commands for the class
     */
    public BotCommandsModule(@NotNull BotCommand[] commands) {
        this(); // calling constructor above
        this.commands = new LinkedList<BotCommand>(Arrays.asList(commands));
    }

    private void parseMsg(Message msg) {

        if (msg.getAuthor().isBot()) // if bot, return
            return;

        Debug.trace("parsing message " + msg.getId());

        if (msg.isFromType(ChannelType.PRIVATE)) { // if dm, return
            msg.getChannel().sendMessage("Please don't DM this account.").queue();
            return;
        }

        String rawMsg = msg.getContentDisplay(); // getting the raw msg
        LinkedList<String> split = new LinkedList<>(Arrays.asList(rawMsg.split(" "))); // splitting the message into a
                                                                                       // list

        String cmd = split.remove().toLowerCase().trim(); // removing first element to get command

        for (BotCommand c : commands) { // for every command
            if (cmd.equals(config.getString("header") + c.getCmd())) { // if cmd is equal to command
                if (permissions.checkUserPerm(c.getRequiredPerm(), msg.getMember())) { // if user has required
                                                                                       // permissions
                    Debug.trace("command " + c.getCmd() + " invoked by " + msg.getAuthor().getName());
                    c.onInvoke(msg, split.toArray(new String[0])); // invoke the command
                } else { // if not enough perms
                    Debug.trace("command " + c.getCmd() + " invoked by " + msg.getAuthor().getName() + " passed "
                            + "because not enough perms");
                    msg.getChannel().sendMessage("You don't have enough perms to execute that! Required perm: `" + c // adding
                                                                                                                     // message
                            .getRequiredPerm() + "`").queue(); // send message
                }
            }
        }
    }

    /**
     * Gets the BlockyBot object (API)
     * 
     * @return BlockyBot API
     */
    public BlockyBot getBot() {
        return this.bot;
    }

    /**
     * Gets all the commands in the module
     * 
     * @return The module commands
     */
    public BotCommand[] getCommands() {
        return this.commands.toArray(new BotCommand[0]);
    }

    /**
     * Invoked on module start
     */
    @Override
    public void onStart() {
        super.onStart();
        
        LinkedList<BotCommand> commands = new LinkedList<BotCommand>();
        for (BotCommand c : this.commands) {
        	JSONObject p = config.getJSONObject("command-perms");
            c.setModule(this, p.has(c.getCmd()) ? p.getString(c.getCmd()) : p.getString("default")); // setting the command module to this
            commands.add(c); // adding the command to list of commands
        }
        this.commands = commands;

        permissions = bot.getModule(PermissionsModule.class); // getting perms module

        bot.addEvent(MessageReceivedEvent.class, event -> { // adding message event
            MessageReceivedEvent r = (MessageReceivedEvent) event; // getting event info

            parseMsg(r.getMessage()); // parsing the message
        });
    }
}
