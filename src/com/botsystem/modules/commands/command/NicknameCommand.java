package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.config.ConfigModule;
import net.dv8tion.jda.core.entities.Message;

public class NicknameCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    public NicknameCommand(String cmd, String reqPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BotSystemEmbed emb = new BotSystemEmbed();
        ConfigModule confModule = this.parent.getBot().getModule(ConfigModule.class);

        if (args.length == 0) {
            confModule.setNickname(null);
            confModule.queueUpdateNow();

            emb.setTitle("Bot's Global Nickname Reset");
            emb.setDescription("The bot's global nickname has been reset to the config");
        } else {
            String nickname = String.join(" ", args);
            confModule.setNickname(nickname);
            confModule.queueUpdateNow();

            emb.setTitle("Bot's Global Nickname Updated");
            emb.setDescription("The bot's global nickname has been set to:\n`" + nickname + "`");
        }


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
