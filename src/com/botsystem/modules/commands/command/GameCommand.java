package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.config.ConfigModule;
import net.dv8tion.jda.core.entities.Message;

public class GameCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    public GameCommand(String cmd, String reqPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BotSystemEmbed emb = new BotSystemEmbed();
        ConfigModule confModule = this.parent.getBot().getModule(ConfigModule.class);

        if (args.length == 0) {
            confModule.setGame(null);
            confModule.queueUpdateNow();

            emb.setTitle("Reset Game");
            emb.setDescription("The bot's game has been reset to the config default");
        } else {
            String game = String.join(" ", args);
            confModule.setGame(game);
            confModule.queueUpdateNow();

            emb.setTitle("Set Bot's Game");
            emb.setDescription("Successfully changed the bot's game to: \n`" + game + "`");
        }

        m.getChannel().sendMessage(emb.build()).queue();
    }

    @Override
    public String getDesc() {
        return "Sets the bot's current game (leave blank for config)";
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
