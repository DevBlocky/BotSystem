package com.botsystem.modules.commands.command;

import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import com.botsystem.modules.display.DisplayModule;

import net.dv8tion.jda.core.entities.Message;

/**
 * A bot command to change the game of the bot
 * 
 * @author BlockBa5her
 *
 */
public class GameCommand extends BotCommand {

    private String cmd;
    private String reqPerm;

    /**
     * Creates an Instance of the "game" command
     * 
     * @param cmd
     *            The command to invoke with
     * @param reqPerm
     *            The required perm to invoke
     */
    public GameCommand(String cmd, String reqPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BotSystemEmbed emb = new BotSystemEmbed(); // creating new embed
        // getting display module to override game
        DisplayModule confModule = this.parent.getBot().getModule(DisplayModule.class);

        // if no input given
        if (args.length == 0) {
            // disable game override
            confModule.setGame(null);
            // queue game update now
            confModule.queueUpdateNow();

            // say so
            emb.setTitle("Reset Game");
            emb.setDescription("The bot's game has been reset to the config default");
        } else { // if there was input
            // combine arguments to get one game
            String game = String.join(" ", args);
            // set the game override
            confModule.setGame(game);
            // queue game update now
            confModule.queueUpdateNow();

            // say so
            emb.setTitle("Set Bot's Game");
            emb.setDescription("Successfully changed the bot's game to: \n`" + game + "`");
        }

        // send message
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
