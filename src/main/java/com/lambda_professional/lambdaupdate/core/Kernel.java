package com.lambda_professional.lambdaupdate.core;

import com.lambda_professional.lambdaupdate.LambdaUpdate;
import com.lambda_professional.lambdaupdate.commands.LambdaUpdateCommand;
import com.lambda_professional.lambdaupdate.events.AsyncPlayerPreLoginListener;
import com.lambda_professional.lambdaupdate.utils.Logging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Kernel {

    public static final String PERMISSION = "lambdaupdate";


    private static boolean duringUpdate = false;

    public static boolean duringUpdate() {
        return duringUpdate;
    }

    public static void registerEvents() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerPreLoginListener(), LambdaUpdate.getPlugin());
    }

    public static void registerCommands() {
        LambdaUpdate.getPlugin().getCommand("lambdaupdate").setExecutor(new LambdaUpdateCommand());
    }

    public static void createPluginFolderIfNotExists() throws IOException {
        if (!LambdaUpdate.getPlugin().getDataFolder().exists()) {
            if (!LambdaUpdate.getPlugin().getDataFolder().mkdirs()) {
                throw new IOException("Can't create plugin folder");
            }
        }
    }

    public static void createUpdatesFolderIfNotExists() throws Exception {
        File updatesFolder = new File(LambdaUpdate.getPlugin().getDataFolder(), Config.getString("updates-dir-name"));
        if (!updatesFolder.exists()) {
            if (!updatesFolder.mkdirs()) {
                throw new IOException("Can't create updates folder inside the plugin folder");
            }
        }
    }

    public static void createBackupsFolderIfNotExists() throws Exception {
        File backupsFolder = new File(LambdaUpdate.getPlugin().getDataFolder(), Config.getString("backups-dir-name"));
        if (!backupsFolder.exists()) {
            if (!backupsFolder.mkdirs()) {
                throw new IOException("Can't create backups folder inside the plugin folder");
            }
        }
    }

    public static boolean checkForUpdates() {
        File updatesFolder = new File(LambdaUpdate.getPlugin().getDataFolder(), Config.getString("updates-dir-name"));

        File[] files = updatesFolder.listFiles();
        return files != null && files.length > 0;
    }


    public static void makeUpdate() throws IOException {

        duringUpdate = true;

        File updatesFolder = new File(LambdaUpdate.getPlugin().getDataFolder(), Config.getString("updates-dir-name"));
        File pluginsFolder = new File(LambdaUpdate.getPlugin().getServer().getWorldContainer(), "plugins");
        File backupsFolder = new File(LambdaUpdate.getPlugin().getDataFolder(), Config.getString("backups-dir-name"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String dateString = dateFormat.format(new Date());

        File[] updateFiles = updatesFolder.listFiles();
        if (updateFiles != null) {
            Logging.log("Updating...");

            for (File updateFile : updateFiles) {
                if (updateFile.isFile() && updateFile.getName().endsWith(".jar")) {
                    File pluginFile = new File(pluginsFolder, updateFile.getName());
                    if (pluginFile.exists()) {
                        if (Config.getBool("create-backups")) {
                            File backupFile = new File(backupsFolder, dateString + "_" + updateFile.getName());
                            if (!backupFile.exists()) {
                                if (!pluginFile.renameTo(backupFile)) {
                                    throw new IOException("Failed to backup the existing plugin: " + pluginFile.getName());
                                }
                                Logging.log("Backed up: " + pluginFile.getAbsolutePath() + " to " + backupFile.getAbsolutePath());
                            }
                        } else {
                            if (!pluginFile.delete()) {
                                throw new IOException("Failed to delete the existing plugin: " + pluginFile.getAbsolutePath());
                            }
                            Logging.log("Deleted existing plugin: " + pluginFile.getAbsolutePath());
                        }
                    }

                    File newPluginFile = new File(pluginsFolder, updateFile.getName());
                    if (!updateFile.renameTo(newPluginFile)) {
                        throw new IOException("Failed to move the update file to the plugins folder: " + updateFile.getName());
                    }
                    Logging.log("Updated: " + updateFile.getName());
                }

            }

            Logging.log("Done! Shutdown...");
            Bukkit.shutdown();
        }

        duringUpdate = false;
    }

    public static void kickPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kick(Component.text(Config.getString("kick-message")), PlayerKickEvent.Cause.PLUGIN);
        }
    }


}
