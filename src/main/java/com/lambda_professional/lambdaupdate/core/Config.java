package com.lambda_professional.lambdaupdate.core;

import com.lambda_professional.lambdaupdate.LambdaUpdate;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;

public class Config {

    private static FileConfiguration config;

    public static void createConfigFileIfNotExists() throws Exception {
        File configFile = new File(LambdaUpdate.getPlugin().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            LambdaUpdate.getPlugin().saveResource("config.yml", true);
        }
    }

    public static void loadConfig() throws Exception {
        config = LambdaUpdate.getPlugin().getConfig();
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


}
