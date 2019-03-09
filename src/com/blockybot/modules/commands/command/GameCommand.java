package com.blockybot.modules.commands.command;

import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.modules.commands.BotCommand;
import com.blockybot.modules.display.DisplayModule;

import net.dv8tion.jda.core.entities.Message;

/**
 * A bot command to change the game of the bot
 * 
 * @author BlockBa5her
 *
 */
public class GameCommand extends BotCommand {
    /**
     * Creates an Instance of the "game" command
     * 
     * @param cmd
     *            The command to invoke with
     */
    public GameCommand(String cmd) {
        super(cmd);
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BlockyBotEmbed emb = new BlockyBotEmbed(); // creating new embed
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
}
