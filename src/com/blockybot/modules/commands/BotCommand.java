package com.blockybot.modules.commands;

import net.dv8tion.jda.core.entities.Message;

public abstract class BotCommand {
    protected BotCommandsModule parent;
    private String perm;
    private String cmd;

    protected BotCommand(String cmd) {
    	this.cmd = cmd;
    }
    
    /**
     * Sets the parent of the command (internally)
     * 
     * @param m
     *            The CommandModule to set the parent to
     */
    final void setModule(BotCommandsModule m, String perm) {
        parent = m;
        this.perm = perm;
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
     * @return The required permission for invocation
     */
    public final String getRequiredPerm() {
    	return perm;
    }

    /**
     * Gets the String command used for invocation
     * 
     * @return The String command to invoke the command with
     */
    public final String getCmd() {
    	return cmd;
    }
}
