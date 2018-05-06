package com.botsystem.console.commands.command;

import com.botsystem.console.commands.ConsoleCommand;
import com.botsystem.modules.config.ConfigModule;

public class ConsoleNicknameCommand extends ConsoleCommand {

    private String cmd;

    public ConsoleNicknameCommand(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void onInvoke(String[] arguments) {
        ConfigModule confModule = parent.getBot().getModule(ConfigModule.class);

        if (arguments.length < 1) {
            confModule.setNickname(null);
            confModule.queueUpdateNow();
            System.out.println("Set the nickname to config default");
            return;
        }

        String newNickname = String.join(" ", arguments);

        confModule.setNickname(newNickname);
        confModule.queueUpdateNow();
        System.out.println("Set the bot's nickname successfully");
    }

    @Override
    public String getCmd() {
        return cmd;
    }

    @Override
    public String getDesc() {
        return "Sets the bot's nickname on every server that it is connected to";
    }
}
