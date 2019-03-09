package com.blockybot.modules.commands.command;

import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.modules.commands.BotCommand;
import com.blockybot.modules.permissions.PermissionsModule;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;

/**
 * Command for bot to ban a user
 * 
 * @author BlockBa5her
 *
 */
public class BanIdCommand extends BotCommand {
    /**
     * Creates an instance of the "ban" command
     *
     * @param cmd
     *            The text to invoke the command with
     */
    public BanIdCommand(String cmd) {
        super(cmd);
    }

    /**
     * The event when the command is invoked
     *
     * @param m
     *            The message the command is invoked with
     * @param args
     *            The arguments of the message
     */
    @Override
    public void onInvoke(Message m, String[] args) {
        // creating embed
        BlockyBotEmbed emb = new BlockyBotEmbed();

        // getting permissions module
        PermissionsModule permissions = this.parent.getBot().getModule(PermissionsModule.class);
        
        // getting the guild
        Guild g = m.getGuild();
        // checking if bot has ban permission
        if (!g.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            // if not, say so and return
            emb.setTitle("Not Enough Permissions");
            emb.setDescription("I don't have the permission to ban");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        // if not mentioning anyone
        if (args.length == 0) {
            // say so and return
            emb.setTitle("No Arguments");
            emb.setDescription("No arguments were given");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }
        
        User uToBan = m.getJDA().retrieveUserById(args[0]).complete();
        if (uToBan == null) {
            emb.setTitle("Invalid ID");
            emb.setDescription("That ID is an invalid user and cannot be banned");
            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }
        
        // getting the member to ban
        Member mToBan = m.getGuild().getMember(uToBan);

        // if they are a member of the server
        if (mToBan != null) {
            // checking if has internal permission to ban
            if (permissions.getPermDistForMembers(m.getMember(), mToBan) < 0) {
                // if not, say so and return
                emb.setTitle("Internal Permission Error");
                emb.setDescription("You don't have permissions to ban people with the permission level `"
                        + permissions.getMemberHighestPerm(mToBan).getKey() + "`");

                m.getChannel().sendMessage(emb.build()).queue();
                return;
            }
            // is higher than discord permissions
            if (!PermissionUtil.canInteract(g.getSelfMember(), mToBan)) {
                emb.setTitle("Invalid Ban User");
                emb.setDescription("The user " + uToBan.getName() + " cannot be banned because they have higher"
                        + " permissions than the bot");
                m.getChannel().sendMessage(emb.build()).queue();
                return;
            }
        }
        
        g.getController().ban(args[0], 0).queue();

        // send embed
        emb.setTitle("User Banned");
        emb.setDescription("The user `" + uToBan.getName() + "` was banned from the server");
        m.getChannel().sendMessage(emb.build()).submit();
    }

    /**
     * @return The description of the command
     */
    @Override
    public String getDesc() {
        return "Bans the user mentioned in the message";
    }
}
