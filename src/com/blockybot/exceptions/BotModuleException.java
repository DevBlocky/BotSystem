package com.blockybot.exceptions;

import com.blockybot.core.BlockyBotModule;

public class BotModuleException extends Exception {
    private static final long serialVersionUID = 1L;

    private BlockyBotModule module;

    public BotModuleException(BlockyBotModule module, String message) {
        super(message);
        this.module = module;
    }

    public BlockyBotModule getModule() {
        return module;
    }
}
