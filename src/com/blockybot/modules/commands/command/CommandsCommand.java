package com.blockybot.modules.commands.command;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.modules.commands.BotCommand;
import com.blockybot.modules.commands.BotCommandsModule;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 * Command for bot to display all commands of BlockyBot
 * 
 * @author BlockBa5her
 *
 */
public class CommandsCommand extends BotCommand {
    /**
     * Creates an Instance of the "commands" command
     * 
     * @param cmd
     *            The command to invoke with
     */
    public CommandsCommand(String cmd) {
        super(cmd);
    }

    /**
     * Converts BlockyBot Commands into a list sorted by permissions
     * 
     * @return
     */
    private Map<String, List<BotCommand>> sortCmdsByReqPerm() {
        BotCommand[] cmds = parent.getCommands(); // get all commands
        Map<String, List<BotCommand>> tmp = new LinkedHashMap<>(); // create tmp map

        for (BotCommand cmd : cmds) { // for every command
            String reqPerm = cmd.getRequiredPerm(); // get required permission
            if (!tmp.containsKey(reqPerm)) // if map doesn't contain permission
                tmp.put(reqPerm, new LinkedList<>()); // set key to a new list

            tmp.get(reqPerm).add(cmd); // add command to list by using the permission
        }

        return tmp; // returning temporary map
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        Map<String, List<BotCommand>> sort = sortCmdsByReqPerm(); // getting sorted commands

        BlockyBotEmbed emb = new BlockyBotEmbed(); // new embed builder

        for (String key : sort.keySet()) { // for every key in the map
            String perm = Character.toUpperCase(key.charAt(0)) + key.substring(1, key.length()); // capitalize first
                                                                                                 // letter
            StringBuilder permCmdOutput = new StringBuilder(); // new string builder for later

            for (BotCommand cmd : sort.get(key)) { // for every command in the key
                // append to string builder: `^COMMAND` - DESCRIPTION
                permCmdOutput.append("`" + parent.getBot().getModuleConfig(BotCommandsModule.class).getString("header") + cmd.getCmd() + "` - " + cmd.getDesc() + "\n");
            }

            // adding a field with string builder, and capitalized permission
            emb.addField(new MessageEmbed.Field(perm + " Commands", permCmdOutput.toString(), false));
        }

        if (emb.isEmpty())
            emb.setTitle("BlockyBot Commands"); // making sure of no crash on build

        m.getChannel().sendMessage(emb.build()).queue(); // sending embed msg
    }

    @Override
    public String getDesc() {
        return "Displays all of the commands of BlockyBot (this command)";
    }
}
