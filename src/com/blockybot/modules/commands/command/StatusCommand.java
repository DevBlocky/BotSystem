package com.blockybot.modules.commands.command;

import com.blockybot.extensions.BlockyBotEmbed;
import com.blockybot.extensions.Utils;
import com.blockybot.modules.commands.BotCommand;
import com.blockybot.modules.display.DisplayModule;

import net.dv8tion.jda.core.entities.Message;

import java.util.Map;

public class StatusCommand extends BotCommand {
    private Map<String, Utils.StatusInfo> possible;

    public StatusCommand(String cmd) {
        super(cmd);
        possible = Utils.getPossibleStatuses();
    }

    private String possibleToString() {
        int index = 0;
        StringBuilder output = new StringBuilder();
        for (String key : possible.keySet()) {
            if (index == 0)
                output.append(key);
            else
                output.append("/" + key);
            index++;
        }
        return output.toString();
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BlockyBotEmbed emb = new BlockyBotEmbed();

        if (args.length == 0) {
            emb.setTitle("No Input");
            emb.setDescription("You didn't give any input when using this command!\nStatuses for input: `"
                    + possibleToString() + "`");

            m.getChannel().sendMessage(emb.build()).queue();
            return;
        }

        String key = args[0].toLowerCase();

        if (possible.containsKey(key)) {
            Utils.StatusInfo info = possible.get(key);

            DisplayModule confModule = this.parent.getBot().getModule(DisplayModule.class);
            confModule.setStatus(info.getInternal());
            confModule.queueUpdateNow();

            emb.setTitle("Status Set");
            emb.setDescription(info.getEmbedReply() + "\n\nGive it a minute for the status to update");
        } else {
            emb.setTitle("Invalid Status");
            emb.setDescription(
                    "You gave an invalid status as an argument.\nStatuses for input: `" + possibleToString() + "`");
        }

        m.getChannel().sendMessage(emb.build()).queue();
    }

    @Override
    public String getDesc() {
        return "Sets the bot's display status (eg. online or do not disturb)";
    }
}
