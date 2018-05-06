package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.permissions.PermissionsModule;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.utils.PermissionUtil;

public class KickCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    private String noKickPerm;

    public KickCommand(String cmd, String reqPerm, String noKickPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
        this.noKickPerm = noKickPerm;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BotSystemEmbed emb = new BotSystemEmbed();
        PermissionsModule permissions =
                this.parent.getBot().getModule(PermissionsModule.class);

        if (m.getMentionedMembers().size() == 0) {
            emb.setTitle("No Users");
            emb.setDescription("No users were mentioned to kick");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }
        Member mToKick = m.getMentionedMembers().get(0);

        Guild g = m.getGuild();
        if (!g.getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
            emb.setTitle("Not Enough Permissions");
            emb.setDescription("I don't have the permission to kick");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        if (permissions.checkUserPerm(this.noKickPerm, mToKick)) {
            emb.setTitle("Internal Permission Error");
            emb.setDescription(
                    "You don't have permissions to kick people with the permission level `" + permissions
                            .getMemberHighestPermName(mToKick) + "`");

            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        if (PermissionUtil.canInteract(g.getSelfMember(), mToKick)) {
            g.getController().kick(mToKick, "Kicked by BotSystem").submit();
            emb.setTitle("User Kicked");
            emb.setDescription("User " + mToKick.getUser().getName() + " has been kicked from this server");
        } else {
            emb.setTitle("Invalid Kick User");
            emb.setDescription("The user " + mToKick.getUser().getName() + " cannot be kicked by the bot because of " +
                    "permissions");
        }

        m.getChannel().sendMessage(emb.build()).submit();
    }

    @Override
    public String getDesc() {
        return "Kicks the user mentioned in the message";
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
