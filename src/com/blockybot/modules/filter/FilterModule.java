package com.blockybot.modules.filter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.blockybot.Debug;
import com.blockybot.core.BlockyBotModule;
import com.blockybot.modules.adminlog.AdminLogModule;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class FilterModule extends BlockyBotModule {
	private final String filterFileName = "filter.txt";
	private List<Pattern> filters;
	
	private void onMsg(MessageReceivedEvent e) {
		if (e.getAuthor().isBot()) {return;}
		for (Pattern f : filters) {
			final boolean match = f.matcher(e.getMessage().getContentDisplay()).find();
			if (match) {
				e.getMessage().delete().queue();
				PrivateChannel dm = e.getMessage().getAuthor().openPrivateChannel().complete();	
				
				dm.sendMessage("You message has been deleted because it violates our text chat filter, if you believe this is an error please contact BlockBa5her").queue();
				
				JSONObject data = new JSONObject();
				data.put("reason", "filter-match");
				data.put("matches", f.toString());
				
				JSONObject msgObj = new JSONObject();
				data.put("message", msgObj);

				JSONObject userObj = new JSONObject();
				userObj.put("id", e.getMessage().getAuthor().getId());
				userObj.put("nick", e.getMessage().getAuthor().getName());
				userObj.put("discrim", e.getMessage().getAuthor().getDiscriminator());
				msgObj.put("user-info", userObj);

				JSONObject messageObj = new JSONObject();
				messageObj.put("id", e.getMessage().getId());
				messageObj.put("text", e.getMessage().getContentRaw().replaceAll("```", "'''"));
				messageObj.put("channel-id", e.getMessage().getChannel().getId());
				messageObj.put("channel-name", e.getMessage().getChannel().getName());
				msgObj.put("message-info", messageObj);

				bot.getModule(AdminLogModule.class).log("Module: `FilterModule`", data);
			}
		}
	}
	
	private LinkedList<Pattern> setupFilters(List<String> lines) {
		LinkedList<Pattern> p = new LinkedList<>();
		
		for (String l : lines) {
			p.add(Pattern.compile(l));
		}
		
		return p;
	}
	
	public void onStart() {
		super.onStart();
		
		List<String> f = new LinkedList<>();
		try {
			FileReader fr = new FileReader(filterFileName);
			BufferedReader br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
				f.add(line);
			br.close();
		} catch (FileNotFoundException e) {
			Debug.trace("File " + filterFileName + " was not found, aborting");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		filters = setupFilters(f);
		
		bot.addEvent(MessageReceivedEvent.class, this::onMsg);
	}
}
