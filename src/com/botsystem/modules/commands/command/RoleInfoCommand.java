package com.botsystem.modules.commands.command;

import com.botsystem.Debug;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

/**
 * A command to display info about server roles
 * 
 * @author BlockBa5her
 *
 */
public class RoleInfoCommand extends BotCommand {

    private String reqPerm;
    private String cmd;

    /**
     * Creates an Instance of the "roleinfo" command
     * 
     * @param cmd
     *            The command to invoke with
     * @param reqPerm
     *            The required permission to invoke
     */
    public RoleInfoCommand(String cmd, String reqPerm) {
        this.reqPerm = reqPerm;
        this.cmd = cmd;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        Guild g = m.getGuild(); // get guild from msg
        BotSystemEmbed emb = new BotSystemEmbed(); // create embed
        StringBuilder output = new StringBuilder(); // create string builder

        List<Role> roles = g.getRoles();
        for (Role r : roles) {
            output.append("name: `" + r.getName() + "`, id: `" + r.getId() + "`\n"); // append to string builder
        }

        // setup embed
        emb.setTitle("Server Role Information");
        emb.addField(new MessageEmbed.Field("Roles", output.toString(), false));

        // send embed msg and display roles
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
