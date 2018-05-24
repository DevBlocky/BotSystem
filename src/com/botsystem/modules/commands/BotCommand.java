package com.botsystem.modules.commands;

import net.dv8tion.jda.core.entities.Message;

public abstract class BotCommand {
    protected BotCommandsModule parent;

    /**
     * Sets the parent of the command (internally)
     * 
     * @param m
     *            The CommandModule to set the parent to
     */
    final void setModule(BotCommandsModule m) {
        parent = m;
    }

    /**
     * Invoked the specified command
     * 
     * @param m
     *            The message to specify
     * @param args
     *            The arguments of the command
     */
    public abstract void onInvoke(Message m, String[] args);

    /**
     * Gets the public description of the command
     * 
     * @return The description
     */
    public abstract String getDesc();

    /**
     * An internal command to get the required permission of the bot to invoke the
     * command
     * 
     * @return The required permission for invokation
     */
    public abstract String getRequiredPerm();

    /**
     * Gets the String command used for invokation
     * 
     * @return The String command to invoke the command with
     */
    public abstract String getCmd();
}
