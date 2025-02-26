package com.lambda_professional.lambdaupdate;

import com.lambda_professional.lambdaupdate.core.Config;
import com.lambda_professional.lambdaupdate.core.Kernel;
import com.lambda_professional.lambdaupdate.tasks.LoopTask;
import com.lambda_professional.lambdaupdate.utils.Logging;
import org.bukkit.plugin.java.JavaPlugin;

public final class LambdaUpdate extends JavaPlugin {

    @Override
    public void onEnable() {

        boolean problem = false;

        try {
            Kernel.createPluginFolderIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating plugin folder: " + e.getMessage());
            problem = true;
        }

        try {
            Logging.createLogFileIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating log file: " + e.getMessage());
            problem = true;
        }

        try {
            Config.createConfigFileIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating config file: " + e.getMessage());
            problem = true;
        }

        try {
            Config.loadConfig();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error loading config file: " + e.getMessage());
            problem = true;
        }

        try {
            Kernel.createUpdatesFolderIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating updates folder: " + e.getMessage());
            problem = true;
        }

        try {
            Kernel.createBackupsFolderIfNotExists();
        } catch (Exception e) {
            LambdaUpdate.getPlugin().getLogger().severe("Error creating backups folder: " + e.getMessage());
            problem = true;
        }

        if (problem) {
            LambdaUpdate.getPlugin().getLogger().severe("Errors during basic startup do not allow safe use of the plugin! Will be disabled to avoid damage.");
            getPlugin().setEnabled(false);
            return;
        }

        int currentConfigVer = Config.getInt("config-ver");
        int requiredConfigVer = Config.REQUIRED_CONFIG_VERSION;
        if (currentConfigVer != requiredConfigVer) {
            Logging.log("You have the wrong config version. This plugin version requires "+requiredConfigVer+", not " + currentConfigVer + ". Will be disabled to avoid damage.");
            getPlugin().setEnabled(false);
            return;
        }


        Kernel.registerEvents();

        Kernel.registerCommands();

        LoopTask.start();

        double ticksToSecs = Double.parseDouble(Config.getString("loop-interval-ticks"))/20D;

        LambdaUpdate.getPlugin().getLogger().info("Hello! Checking for updates every " + Config.getString("loop-interval-ticks") + " ticks (+- " + ticksToSecs + " sec).");

    }

    @Override
    public void onDisable() {
        LoopTask.stop();
    }

    public static JavaPlugin getPlugin() {
        return JavaPlugin.getPlugin(LambdaUpdate.class);
    }

}
