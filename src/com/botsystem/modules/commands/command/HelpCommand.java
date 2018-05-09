package com.botsystem.modules.commands.command;

import com.botsystem.Main;
import com.botsystem.extensions.BotSystemEmbed;
import com.botsystem.modules.commands.BotCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class HelpCommand extends BotCommand {

    private String reqPerm;
    private String cmd;

    public HelpCommand(String cmd, String reqPerm) {
        this.cmd = cmd;
        this.reqPerm = reqPerm;
    }

    @Override
    public void onInvoke(Message m, String[] args) {
        BotSystemEmbed emb = new BotSystemEmbed();

        emb.setTitle("BotSystem Help");

        emb.addField(new MessageEmbed.Field("Information", "" +
                "BotSystem is a simple bot built for the DispatchSystem server. " +
                "It's here to help people install DispatchSystem without Support Staff, and even have some fun jokes.\n\n" +
                "Note: This is a WIP (Work in progress), so not all features may be available at the moment", false));
        emb.addField(new MessageEmbed.Field("Commands", "" +
                "Use `^commands` to view all of BotSystem's commands", false));
        emb.addField(new MessageEmbed.Field("Version", "" +
                Main.CONFIG.getString("version"), false));

        m.getChannel().sendMessage(emb.build()).queue();
    }

    @Override
    public String getDesc() {
        return "Displays technical information about BotSystem";
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
