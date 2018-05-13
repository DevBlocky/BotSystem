package com.botsystem.modules.commands.command;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommandsModule;
import com.botsystem.modules.commands.BotCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class CommandsCommand extends BotCommand {
    private String reqPerm;
    private String cmd;

    public CommandsCommand(String cmd, String reqPerm) {
        this.reqPerm = reqPerm;
        this.cmd = cmd;
    }
    
    private Map<String, List<BotCommand>> sortCmdsByReqPerm() {
    	BotCommand[] cmds = parent.getCommands();
    	Map<String, List<BotCommand>> tmp = new LinkedHashMap<>();
    	
    	for (BotCommand cmd : cmds) {
    		String reqPerm = cmd.getRequiredPerm();
    		if (!tmp.containsKey(reqPerm))
    			tmp.put(reqPerm, new LinkedList<>());
    		
    		tmp.get(reqPerm).add(cmd);
    	}
    	
    	return tmp;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        Map<String, List<BotCommand>> sort = sortCmdsByReqPerm();

        BotSystemEmbed emb = new BotSystemEmbed();
        
        for (String key : sort.keySet()) {
        	String perm = Character.toUpperCase(key.charAt(0)) + key.substring(1, key.length()); // capitalize first letter
        	StringBuilder permCmdOutput = new StringBuilder(); // output variable
        	
        	for (BotCommand cmd : sort.get(key)) {
        		permCmdOutput.append("`" + BotCommandsModule.HEADER + cmd.getCmd() + "` - " + cmd.getDesc() + "\n");
        	}
        	
        	emb.addField(new MessageEmbed.Field(perm + " Commands", permCmdOutput.toString(), false));
        }

        if (emb.isEmpty())
        	emb.setTitle("BotSystem Commands"); // making sure of no crash on build
        
        m.getChannel().sendMessage(emb.build()).queue();
    }

    @Override
    public String getDesc() {
        return "Displays all of the commands of BotSystem (this command)";
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
