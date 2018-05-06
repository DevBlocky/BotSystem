package com.botsystem.core;

import com.botsystem.Debug;

public abstract class BotSystemModule {
    protected BotSystem bot = null;

    final void setBotSystem(BotSystem bot) {
        this.bot = bot;
    }

    public void onStart() {
        Debug.trace("starting " + this.getClass().getSimpleName());
    }

    public void onTick() {}

    public String getName() {
        return this.getClass().getSimpleName();
    }
}
