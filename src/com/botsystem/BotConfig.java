package com.botsystem;

import com.botsystem.exceptions.ExceptionHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class BotConfig {
    private JSONParser parser;
    private JSONObject base;
    private String file;

    public BotConfig(String file) {
        this.file = file;
        parser = new JSONParser();
    }

    public void configParse() {
        try {
            Debug.trace("loaded permissions into JSONObject buffer");
            base = (JSONObject) parser.parse(new FileReader(file));
        } catch (IOException e) {
            ExceptionHelper.throwException(e);
        } catch (ParseException e) {
            ExceptionHelper.throwException(e);
        }
    }

    public Object get(String val) {
        return base.get(val);
    }

    public boolean getBoolean(String val) {
        return (boolean) get(val);
    }

    public String getString(String val) {
        return (String) get(val);
    }

    public double getDouble(String val) {
        return (double) get(val);
    }

    public int getInt(String val) {
        return (int) get(val);
    }

    public long getLong(String val) {
        return (long) get(val);
    }

    public JSONObject getObject(String val) {
        return (JSONObject) get(val);
    }

    public JSONArray getArray(String val) {
        return (JSONArray) get(val);
    }

    public boolean containsKey(String val) {
        return base.containsKey(val);
    }

    public boolean isNull(String val) {
        return get(val) == null;
    }
}
