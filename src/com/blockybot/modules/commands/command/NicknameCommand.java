package com.blockybot.modules.commands.command;

import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.modules.commands.BotCommand;
import com.blockybot.modules.display.DisplayModule;

import net.dv8tion.jda.core.entities.Message;

/**
 * Command for bot to change the bot's nickname
 * 
 * @author BlockBa5her
 *
 */
public class NicknameCommand extends BotCommand {
    /**
     * Creates an Instance of the "nickname" command
     * 
     * @param cmd
     *            The command to invoke with
     */
    public NicknameCommand(String cmd) {
        super(cmd);
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        // setup stuff
        BlockyBotEmbed emb = new BlockyBotEmbed();
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
}
