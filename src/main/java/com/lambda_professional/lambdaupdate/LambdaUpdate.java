package com.lambda_professional.lambdaupdate;

import com.lambda_professional.lambdaupdate.core.Config;
import com.lambda_professional.lambdaupdate.core.Kernel;
import com.lambda_professional.lambdaupdate.tasks.LoopTask;
import com.lambda_professional.lambdaupdate.utils.Logging;
import org.bukkit.plugin.java.JavaPlugin;

public final class LambdaUpdate extends JavaPlugin {

    @Override
    public void onEnable() {


        try {
            Kernel.createPluginFolderIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating plugin folder: " + e.getMessage());
        }

        try {
            Logging.createLogFileIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating log file: " + e.getMessage());
        }

        try {
            Config.createConfigFileIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating config file: " + e.getMessage());
        }

        try {
            Config.loadConfig();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error loading config file: " + e.getMessage());
        }

        try {
            Kernel.createUpdatesFolderIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating updates folder: " + e.getMessage());
        }

        try {
            Kernel.createBackupsFolderIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating backups folder: " + e.getMessage());
        }


        Kernel.registerEvents();

        LoopTask.start();

        LambdaUpdate.getPlugin().getLogger().info("Hello! Everything is good! Checking for updates every " + Config.getString("loop-interval-ticks") + " ticks.");

    }

    @Override
    public void onDisable() {
        LoopTask.stop();
    }

    public static JavaPlugin getPlugin() {
        return JavaPlugin.getPlugin(LambdaUpdate.class);
    }

}
