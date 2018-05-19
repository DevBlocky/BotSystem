package com.botsystem;

import com.botsystem.exceptions.ExceptionHelper;
import org.json.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BotConfig {
    private JSONObject base;
    private String file;

    public BotConfig(String file) {
        this.file = file;
    }

    public void configParse() {
        try {
        	// debug
            Debug.trace("loaded permissions into JSONObject buffer");
            
            // reading the file
            File fr = new File(file); // get file obj
            FileInputStream fis = new FileInputStream(fr); // create input stream from file
            ByteBuffer bf = ByteBuffer.allocate((int)fr.length()); // new byte buffer with fr length
            fis.read(bf.array()); // reading file into buffer
            fis.close(); // close the stream
            new String();
            String str = new String(bf.array(), "UTF-8"); // get raw string out of data
            
            // parsing JSON
            base = new JSONObject(str);
        } catch (IOException e) {
            ExceptionHelper.throwException(e);
        } catch (JSONException e) {
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
        return base.has(val);
    }

    public boolean isNull(String val) {
        return get(val) == null;
    }
}
