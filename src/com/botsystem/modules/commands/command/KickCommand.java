package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.permissions.PermissionsModule;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.utils.PermissionUtil;

/**
 * Command for bot to kick a user
 * @author BlockBa5her
 *
 */
public class KickCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    private String noKickPerm;

    /**
     * Creates an instance of the "kick" command
     * 
     * @param cmd        The text to invoke the command with
     * @param reqPerm    The required user permission to invoke the command
     * @param noKickPerm Same and above perms cannot be banned
     */
    public KickCommand(String cmd, String reqPerm, String noKickPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
        this.noKickPerm = noKickPerm;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
    	// creating embed
        BotSystemEmbed emb = new BotSystemEmbed();
        
        // getting permissions module
        PermissionsModule permissions =
                this.parent.getBot().getModule(PermissionsModule.class);

        // if not mentioning anyone
        if (m.getMentionedMembers().size() == 0) {
        	// say so and return
            emb.setTitle("No Users"); 
            emb.setDescription("No users were mentioned to kick");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }
        
        // getting the member to ban
        Member mToKick = m.getMentionedMembers().get(0);

        // getting the guild
        Guild g = m.getGuild();
        // checking if bot has kick permission
        if (!g.getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
        	// if not, say so and return
            emb.setTitle("Not Enough Permissions");
            emb.setDescription("I don't have the permission to kick");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        // checking if has internal permission to kick
        if (permissions.checkUserPerm(this.noKickPerm, mToKick)) {
        	// if not, say so and return
            emb.setTitle("Internal Permission Error");
            emb.setDescription("You don't have permissions to kick people with the permission level `" + permissions
                    .getMemberHighestPerm(mToKick).getKey() + "`");

            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        // is higher on discord permission hierarchy
        if (PermissionUtil.canInteract(g.getSelfMember(), mToKick)) {
            g.getController().kick(mToKick, "Kicked by BotSystem").submit(); // kick with BotSystem
            
            // setup embed
            emb.setTitle("User Kicked");
            emb.setDescription("User " + mToKick.getUser().getName() + " has been kicked from this server");
        } else {
        	// if can't touch them
        	
        	// setup embed
            emb.setTitle("Invalid Ban User");
            emb.setDescription("The user " + mToKick.getUser().getName() + " cannot be kicked because they have higher" +
                    " permissions than the bot");
        }

        // send embed
        m.getChannel().sendMessage(emb.build()).submit();
    }

    /**
     * @return The description of the command
     */
    @Override
    public String getDesc() {
        return "Kicks the user mentioned in the message";
    }

    /**
     * @return The required permission for the command
     */
    @Override
    public String getRequiredPerm() {
        return reqPerm;
    }

    /**
     * @return The command invoke text for this command
     */
    @Override
    public String getCmd() {
        return cmd;
    }
}
