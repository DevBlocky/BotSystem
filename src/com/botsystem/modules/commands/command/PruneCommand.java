package com.botsystem.modules.commands.command;

import java.util.LinkedList;
import java.util.List;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.extensions.Utils;
import com.botsystem.modules.commands.BotCommand;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.requests.RestAction;

/**
 * A command to prune messages from the chat
 * @author BlockBa5her
 *
 */
public class PruneCommand extends BotCommand {
	
	/**
	 * The max number of messages deleted at once
	 */
	private final int MSG_DELETE_LIMIT = 75;
	
	private String cmd;
	private String reqPerm;
	
	/**
	 * Creates an instance of the PruneCommand class
	 * @param cmd The command to invoke it with
	 * @param reqPerm The required permission to invoke it with
	 */
	public PruneCommand(String cmd, String reqPerm) {
		this.cmd = cmd;
		this.reqPerm = reqPerm;
	}

	@Override
	public void onInvoke(Message m, String[] args) {
		// create new embed instance
		BotSystemEmbed emb = new BotSystemEmbed();
		
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
		MessageChannel c = m.getChannel();
		m.delete().complete(); // deleting initial message
		
		// get channel history up to limit
		List<Message> history = c.getIterableHistory().limit(count).complete();
		// rest action queue for messages being deleted
		List<RestAction<?>> queue = new LinkedList<>();
		// for every msg
		for (Message historyM : history) {
			RestAction<Void> x = historyM.delete(); // create RestAction for msg
			queue.add(x); // add to the queue var
			x.queue(); // queue for happening in discord
		}
		
		// create a new thread (so that it doesn't block)
		new Thread(() -> {
			// for every rest action in queue
			for (RestAction<?> x : queue) {
				// try to delete msg without error
				try {
					x.complete(); // waiting for everything to complete
				} catch (ErrorResponseException e) { // basically if message already deleted
					// cancel
				}
			}
			
			// after everything deleted send msg
			emb.setTitle("Messages Deleted");
			emb.setDescription("`" + count + "` messages have been deleted from this channel");
			Message newM = c.sendMessage(emb.build()).complete();
			
			// after 3 seconds, delete msg previously sent
			Utils.createTimeout(() -> {
				newM.delete().queue();
			}, 3000);
		}).start();
	}

	@Override
	public String getDesc() {
		return "Clears the channel with the specified amount of messages";
	}

	@Override
	public String getRequiredPerm() {
		return reqPerm;
	}

	@Override
	public String getCmd() {
		return cmd;
	}

}
