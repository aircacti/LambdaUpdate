package com.lambda_professional.lambdaupdate.core;

import com.lambda_professional.lambdaupdate.LambdaUpdate;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class Config {

    public static final int REQUIRED_CONFIG_VERSION = 2;

    private static FileConfiguration config;

    public static void createConfigFileIfNotExists() throws Exception {
        File configFile = new File(LambdaUpdate.getPlugin().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            LambdaUpdate.getPlugin().saveResource("config.yml", false);
        }
    }

    public static void loadConfig() throws Exception {
        config = LambdaUpdate.getPlugin().getConfig();
    }

    public static void reloadConfig() throws Exception {
        LambdaUpdate.getPlugin().reloadConfig();
        loadConfig();
    }

    public static String getString(String path) {
        if (config == null) {
            return "";
        }
        return config.getString(path);
    }

    public static int getInt(String path) {
        if (config == null) {
            return 0;
        }
        return config.getInt(path);
    }

    public static boolean getBool(String path) {
        if (config == null) {
            return false;
        }
        return config.getBoolean(path);
    }


    public static HashMap<String, String> getAllSettings() {
        HashMap<String, String> settingsMap = new HashMap<>();
        if (config != null) {
            Set<String> keys = config.getKeys(false); // false oznacza, że nie wchodzimy w podklucze
            for (String key : keys) {
                String value = String.valueOf(config.get(key));
                settingsMap.put(key, value);
            }
        }
        return settingsMap;
    }

}
