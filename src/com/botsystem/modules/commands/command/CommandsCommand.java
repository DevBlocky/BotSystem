package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommandsModule;
import com.botsystem.modules.commands.BotCommand;
import net.dv8tion.jda.core.entities.Message;

public class CommandsCommand extends BotCommand {
    private String reqPerm;
    private String cmd;

    public CommandsCommand(String cmd, String reqPerm) {
        this.reqPerm = reqPerm;
        this.cmd = cmd;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BotCommand[] commands = parent.getCommands();

        BotSystemEmbed emb = new BotSystemEmbed();

        String output = "";
        for (BotCommand c : commands) {
            output += "`" + BotCommandsModule.HEADER + c.getCmd() + "` - " + c.getDesc() + "\n";
        }

        emb.setTitle("BotSystem Commands");
        emb.setDescription(output);

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
