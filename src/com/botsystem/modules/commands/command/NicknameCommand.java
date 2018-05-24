package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.display.DisplayModule;

import net.dv8tion.jda.core.entities.Message;

/**
 * Command for bot to change the bot's nickname
 * 
 * @author BlockBa5her
 *
 */
public class NicknameCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    /**
     * Creates an Instance of the "nickname" command
     * 
     * @param cmd
     *            The command to invoke with
     * @param reqPerm
     *            The required permission to invoke
     */
    public NicknameCommand(String cmd, String reqPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        // setup stuff
        BotSystemEmbed emb = new BotSystemEmbed();
        DisplayModule confModule = this.parent.getBot().getModule(DisplayModule.class);

        // if no arguments
        if (args.length == 0) {
            // stop override and queue update now
            confModule.setNickname(null);
            confModule.queueUpdateNow();

            // setup embed
            emb.setTitle("Bot's Global Nickname Reset");
            emb.setDescription("The bot's global nickname has been reset to the config");
        } else {
            // get nickname by joining arguments
            String nickname = String.join(" ", args);
            // set the nickname and queue update now
            confModule.setNickname(nickname);
            confModule.queueUpdateNow();

            // setup embed
            emb.setTitle("Bot's Global Nickname Updated");
            emb.setDescription("The bot's global nickname has been set to:\n`" + nickname + "`");
        }

        // send embed into chnanel
        m.getChannel().sendMessage(emb.build()).queue();
    }

    @Override
    public String getDesc() {
        return "Changes the nickname of the bot on all servers";
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
