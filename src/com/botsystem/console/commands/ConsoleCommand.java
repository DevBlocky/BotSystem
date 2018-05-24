package com.botsystem.console.commands;

public abstract class ConsoleCommand {

    protected ConsoleCommands parent;

    /**
     * Set's the parent of the commands, to access API
     * 
     * @param m
     *            The API for the command to access
     */
    final void setParent(ConsoleCommands m) {
        parent = m;
    }

    /**
     * Invokes the command (with the arguments)
     * 
     * @param arguments
     */
    public abstract void onInvoke(String[] arguments);

    /**
     * Returns the Command to invoke with in the console
     * 
     * @return
     */
    public abstract String getCmd();

    /**
     * Returns the description of the command in the console
     * 
     * @return
     */
    public abstract String getDesc();
}
