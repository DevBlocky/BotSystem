package com.botsystem.modules.commands.command;

import com.botsystem.Debug;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class RoleInfoCommand extends BotCommand {

    private String reqPerm;
    private String cmd;

    public RoleInfoCommand(String cmd, String reqPerm) {
        this.reqPerm = reqPerm;
        this.cmd = cmd;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        Guild g = m.getGuild();
        BotSystemEmbed emb = new BotSystemEmbed();
        String output = "";

        List<Role> roles = g.getRoles();
        for (Role r : roles) {
            output += "name: `" + r.getName() + "`, id: `" + r.getId() + "`\n";
        }

        emb.setTitle("Server Role Information");
        emb.addField(new MessageEmbed.Field("Roles", output, false));

        m.getChannel().sendMessage(emb.build()).queue();
        Debug.trace("displayed roles to user from invoked command");
    }

    @Override
    public String getDesc() {
        return "Shows all of the roles for the server, including their IDs";
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
