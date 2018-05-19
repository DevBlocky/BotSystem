package com.botsystem.modules.display;

import com.botsystem.BotConfig;
import com.botsystem.Debug;
import com.botsystem.Main;
import com.botsystem.core.BotSystemModule;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;

import java.time.Instant;

/**
 * Module for BotSystem about changing the basic discord information of the bot
 * Name, Game, Status ETC
 * @author BlockBa5her
 *
 */
public class DisplayModule extends BotSystemModule {

    private BotConfig conf; // the config of the bot
    private Instant lastTime; // the last time of the bot update

    // overrides of the stuff
    private String overrideStatus = null;
    private String overrideNickname = null;
    private String overrideGame = null;

    /**
     * Creates instance of ConfigModule class
     */
    public DisplayModule() {
        this.conf = Main.CONFIG; // getting config from "Main"
    }

    /**
     * Updates the bot's information globally
     */
    private void updateBot() {
        conf.configParse(); // reload config

        String game = overrideGame == null ? conf.getString("game") : overrideGame; // gets the game either from the config or override
        String status = overrideStatus == null ? conf.getString("status") : overrideStatus; // gets the status either from the config or override
        String nick = overrideNickname == null ? conf.getString("nickname") : overrideNickname; // gets the nickname either from the config or override

        bot.setGame(game); // sets the bot's game
        bot.setStatus(status); // sets the bot's status

        for (Guild g : bot.getGuilds()) { // for every guild of the bot
            if (g.getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)) { // if has permission to change nickname
                g.getController().setNickname(g.getSelfMember(), nick).queue(); // queue change nickname event with new nickname
            }
        }

        Debug.trace("updated game, status, and nickname"); // debug
    }

    /**
     * Sets the override online status for the bot (null for bot-config.json)
     * @param status The online status to override with
     */
    public void setStatus(String status) {
        this.overrideStatus = status; // overriding the status
    }

    /**
     * Sets the override game for the bot (null for bot-config.json)
     * @param game The game to override with
     */
    public void setGame(String game) {
        this.overrideGame = game; // overriding the game
    }

    /**
     * Sets the override nickname for the bot (null for bot-config.json)
     * @param nickname The nickname to override with
     */
    public void setNickname(String nickname) {
        this.overrideNickname = nickname; // overriding the nickname
    }

    /**
     * Queues the update of bot information for next tick
     */
    public void queueUpdateNow() {
    	// setting last time to the minimum so that executes next tick
        this.lastTime = Instant.MIN;
    }

    @Override
    public void onTick() {
        if (lastTime == null) { // if there was no lastTime
            lastTime = Instant.MIN; // set last time to minimum value
        }
        if ((Instant.now().getEpochSecond() - lastTime.getEpochSecond()) < 60) // between now and lastTime is not over 60
            return;

        lastTime = Instant.now(); // reset lastTime to now

        updateBot(); // update bot information
    }
}
