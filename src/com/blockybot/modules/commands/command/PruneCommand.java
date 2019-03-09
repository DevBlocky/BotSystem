package com.blockybot.modules.commands.command;

import java.util.List;

import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * A command to prune messages from the chat
 * 
 * @author BlockBa5her
 *
 */
public class PruneCommand extends BotCommand {

    /**
     * The max number of messages deleted at once
     */
    private final int MSG_DELETE_LIMIT = 100;

    /**
     * Creates an instance of the PruneCommand class
     * 
     * @param cmd
     *            The command to invoke it with
     */
    public PruneCommand(String cmd) {
        super(cmd);
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        // create new embed instance
        BlockyBotEmbed emb = new BlockyBotEmbed();

        // if no arguments
        if (args.length == 0) {
            // say so and return
            emb.setTitle("Invalid Arguments");
            emb.setDescription("You must specify the amount of messages to delete");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        // get count string
        String countStr = args[0];
        int _count = 0;
        try {
            // find the count by parsing
            _count = Integer.parseUnsignedInt(countStr);
        } catch (NumberFormatException e) { // if not in a valid format
            // say so and return
            emb.setTitle("Invalid Format");
            emb.setDescription("The given argument was not a valid positive number");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }
        // create final for block scope
        final int count = _count;

        // if count is greater than max message delete
        if (count > MSG_DELETE_LIMIT) {
            // say so and return
            emb.setTitle("Maximum Message Delete Count");
            emb.setDescription("You have set the maximum delete count over `" + MSG_DELETE_LIMIT + "`."
                    + " We limit this so that discord doesn't think we are a DDOS attack or something :joy:");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        // get the channel before delete
        TextChannel c = m.getTextChannel();
        m.delete().complete(); // deleting initial message

        // get channel history up to limit
        List<Message> history = c.getIterableHistory().limit(count).complete();
        if (history.size() == 1)
            history.get(0).delete().queue();
        else
            c.deleteMessages(history).queue();
            
    }

    @Override
    public String getDesc() {
        return "Clears the channel with the specified amount of messages";
    }
}
