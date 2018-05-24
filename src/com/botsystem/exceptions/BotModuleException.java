package com.botsystem.exceptions;

import com.botsystem.core.BotSystemModule;

public class BotModuleException extends Exception {
    private static final long serialVersionUID = 1L;

    private BotSystemModule module;

    public BotModuleException(BotSystemModule module, String message) {
        super(message);
        this.module = module;
    }

    public BotSystemModule getModule() {
        return module;
    }
}
