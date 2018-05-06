package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.permissions.PermissionsModule;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.utils.PermissionUtil;

public class BanCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    private String noBanPerm;

    /**
     * Creates an instance of the "ban" command
     *
     * @param cmd       The text to invoke the command with
     * @param reqPerm   The required user permission to invoke the command
     * @param noBanPerm Same and above perms cannot be banned
     */
    public BanCommand(String cmd, String reqPerm, String noBanPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
        this.noBanPerm = noBanPerm;
    }

    /**
     * The event when the command is invoked
     *
     * @param m    The message the command is invoked with
     * @param args The arguments of the message
     */
    @Override
    public void onInvoke(Message m, String[] args) {
        BotSystemEmbed emb = new BotSystemEmbed();
        PermissionsModule permissions =
                this.parent.getBot().getModule(PermissionsModule.class);

        if (m.getMentionedMembers().size() == 0) {
            emb.setTitle("No Users");
            emb.setDescription("No users were mentioned to ban");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }
        Member mToBan = m.getMentionedMembers().get(0);

        Guild g = m.getGuild();
        if (!g.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            emb.setTitle("Not Enough Permissions");
            emb.setDescription("I don't have the permission to ban");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        if (permissions.checkUserPerm(this.noBanPerm, mToBan)) {
            emb.setTitle("Internal Permission Error");
            emb.setDescription("You don't have permissions to ban people with the permission level `" + permissions
                    .getMemberHighestPermName(mToBan) + "`");

            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        if (PermissionUtil.canInteract(g.getSelfMember(), mToBan)) {
            g.getController().ban(mToBan.getUser(), 0, "Banned by BotSystem").submit();
            emb.setTitle("User Banned");
            emb.setDescription("User " + mToBan.getUser().getName() + " has been banned from this server");
        } else {
            emb.setTitle("Invalid Ban User");
            emb.setDescription("The user " + mToBan.getUser().getName() + " cannot be banned because they have higher" +
                    " permissions than the bot");
        }

        m.getChannel().sendMessage(emb.build()).submit();
    }

    /**
     * @return The description of the command
     */
    @Override
    public String getDesc() {
        return "Bans the user mentioned in the message";
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
