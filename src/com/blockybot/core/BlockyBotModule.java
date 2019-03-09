package com.blockybot.core;

import org.json.JSONObject;

import com.blockybot.Debug;

public abstract class BlockyBotModule {
    protected BlockyBot bot = null;
    protected JSONObject config = null;

    final void setConfig(BlockyBot bot, JSONObject config) {
        this.bot = bot;
        this.config = config;
    }

    public void onStart() {
        Debug.trace("starting " + this.getClass().getSimpleName());
    }

    public void onTick() {
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }
}
