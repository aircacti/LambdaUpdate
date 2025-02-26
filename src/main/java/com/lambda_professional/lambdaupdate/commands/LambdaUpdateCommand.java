package com.lambda_professional.lambdaupdate.commands;

import com.lambda_professional.lambdaupdate.LambdaUpdate;
import com.lambda_professional.lambdaupdate.core.Config;
import com.lambda_professional.lambdaupdate.core.Kernel;
import com.lambda_professional.lambdaupdate.utils.Logging;
import com.lambda_professional.lambdaupdate.utils.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LambdaUpdateCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission(Kernel.PERMISSION)) {
            sendNoPermission(sender);
            Logging.log("Player " + sender.getName() + " used the command without permission!");
            return false;
        }

        if (args.length == 0) {
            sendAvailableCommandsMessage(sender, label);
            return false;
        }

        if (args.length == 1) {
            String arg = args[0];

            if (arg.equalsIgnoreCase("reload")) {
                try {
                    Config.reloadConfig();
                    sendReloaded(sender);
                } catch (Exception e) {
                    sendError(sender, e);
                    LambdaUpdate.getPlugin().getLogger().severe("Error loading config file: " + e.getMessage());
                }
                return false;
            }

            if (arg.equalsIgnoreCase("forceupdate")) {

                sendForceUpdate(sender);

                try {
                    Kernel.makeUpdate();
                } catch (Exception e) {
                    sendError(sender, e);
                    LambdaUpdate.getPlugin().getLogger().severe("Error trying to update: " + e.getMessage());
                }

                sendShouldRestart(sender);

                sendDone(sender);
                return false;
            }

            if (arg.equalsIgnoreCase("status")) {
                sendStatus(sender);
                return false;
            }

            sendInvalidArgument(sender, label);
            return false;
        }


        sendInvalidUsage(sender, label);
        return false;
    }

    private void sendNoPermission(CommandSender sender) {
        sender.sendMessage(Text.formatLegacyMessage(Config.getString("no-permission-message")));
    }

    private void sendAvailableCommandsMessage(CommandSender sender, String label) {
        TextComponent.Builder messageBuilder = Component.text();

        messageBuilder.append(
                Component.text("Available commands:\n").color(NamedTextColor.WHITE)
        );

        messageBuilder.append(
                Component.text("/"+label+" reload ").color(NamedTextColor.GREEN))
                .append(Component.text("- Reload the configuration\n").color(NamedTextColor.WHITE)
        );

        messageBuilder.append(
                Component.text("/"+label+" forceupdate").color(NamedTextColor.GREEN))
                .append(Component.text("- Force update ignoring player threshold and kicking. Recommended console\n").color(NamedTextColor.WHITE)
        );

        messageBuilder.append(
                Component.text("/"+label+" status ").color(NamedTextColor.GREEN))
                .append(Component.text("- Show the current plugin status").color(NamedTextColor.WHITE)
        );

        sender.sendMessage(
                Text.formatComponentMessage(messageBuilder.build())
        );
    }

    private void sendInvalidUsage(CommandSender commandSender, String label) {
        commandSender.sendMessage(Text.formatComponentMessage(
                Component.text("Invalid usage. Check /"+label).color(NamedTextColor.RED))
        );
    }

    private void sendReloaded(CommandSender commandSender) {
        commandSender.sendMessage(Text.formatComponentMessage(
                Component.text("Reloaded!").color(NamedTextColor.GREEN))
        );
    }

    private void sendError(CommandSender commandSender, Exception e) {
        commandSender.sendMessage(Text.formatComponentMessage(
                Component.text("An error occurred! Please report to the author. "+ e.getMessage()).color(NamedTextColor.RED))
        );
    }

    private void sendForceUpdate(CommandSender commandSender) {
        commandSender.sendMessage(Text.formatComponentMessage(
                Component.text("Forced update! Be prepared for shutdown.").color(NamedTextColor.GREEN))
        );
    }

    private void sendDone(CommandSender commandSender) {
        commandSender.sendMessage(Text.formatComponentMessage(
                Component.text("Done!").color(NamedTextColor.GREEN))
        );
    }

    private void sendShouldRestart(CommandSender commandSender) {
        commandSender.sendMessage(Text.formatComponentMessage(
                Component.text("The server should be restarted immediately to avoid errors. Check the logs!").color(NamedTextColor.RED))
        );
    }

    private void sendInvalidArgument(CommandSender commandSender, String label) {
        commandSender.sendMessage(Text.formatComponentMessage(
                Component.text("The argument you provided is incorrect. Check /"+label).color(NamedTextColor.RED))
        );
    }


    private void sendStatus(CommandSender sender) {
        TextComponent.Builder messageBuilder = Component.text();

        messageBuilder.append(
                Component.text("Plugin status:\n").color(NamedTextColor.WHITE)
        );

        String serverVersion = LambdaUpdate.getPlugin().getServer().getVersion();
        messageBuilder.append(
                Component.text("Server Version: ").color(NamedTextColor.GREEN)
        ).append(Component.text(serverVersion + "\n").color(NamedTextColor.WHITE));

        String pluginVersion = LambdaUpdate.getPlugin().getPluginMeta().getVersion();
        messageBuilder.append(
                Component.text("Plugin Version: ").color(NamedTextColor.GREEN)
        ).append(Component.text(pluginVersion + "\n").color(NamedTextColor.WHITE));



        int playersOnline = Bukkit.getOnlinePlayers().size();
        messageBuilder.append(
                Component.text("Players online: ").color(NamedTextColor.GREEN)
        ).append(Component.text(playersOnline + "\n").color(NamedTextColor.WHITE));

        boolean updateAvailable = Kernel.checkForUpdates();
        messageBuilder.append(
                Component.text("Update Available: ").color(NamedTextColor.GREEN)
        ).append(Component.text((updateAvailable ? "Yes" : "No") + "\n").color(NamedTextColor.WHITE));

        boolean updateInProgress = Kernel.duringUpdate();
        messageBuilder.append(
                Component.text("Update In Progress: ").color(NamedTextColor.GREEN)
        ).append(Component.text((updateInProgress ? "Yes" : "No") + "\n").color(NamedTextColor.WHITE));

        messageBuilder.append(
                Component.text("Config data:\n").color(NamedTextColor.GREEN)
        );

        HashMap<String, String> configSettings = Config.getAllSettings();
        for (String key : configSettings.keySet()) {
            String value = configSettings.get(key);
            messageBuilder.append(
                    Component.text("- " + key + ": ").color(NamedTextColor.GREEN)
            ).append(Component.text(value + "\n").color(NamedTextColor.WHITE));
        }

        messageBuilder.append(
                Component.text("That's all!").color(NamedTextColor.GREEN)
        );

        sender.sendMessage(
                Text.formatComponentMessage(messageBuilder.build())
        );
    }



}
