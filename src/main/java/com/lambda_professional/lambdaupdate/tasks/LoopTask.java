package com.lambda_professional.lambdaupdate.tasks;

import com.lambda_professional.lambdaupdate.LambdaUpdate;
import com.lambda_professional.lambdaupdate.core.Config;
import com.lambda_professional.lambdaupdate.core.Kernel;
import com.lambda_professional.lambdaupdate.utils.Logging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LoopTask {

    private static BukkitRunnable loopTask;

    public static void start() {
        loopTask = new BukkitRunnable() {
            @Override
            public void run() {

                Bukkit.getScheduler().runTask(LambdaUpdate.getPlugin(), () -> {

                    if (Kernel.checkForUpdates()) {
                        Logging.log("Found new updates...");

                        if (Bukkit.getOnlinePlayers().size() <= Config.getInt("players-online")) {

                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.kick(Component.text(Config.getString("kick-message")), PlayerKickEvent.Cause.PLUGIN);
                            }

                            try {
                                Kernel.makeUpdate();
                            } catch (Exception e) {
                                LambdaUpdate.getPlugin().getLogger().severe("Error trying to update: " + e.getMessage());
                            }

                        }

                    }

                });

            }
        };
        loopTask.runTaskTimerAsynchronously(LambdaUpdate.getPlugin(), Config.getInt("loop-interval-ticks"), Config.getInt("loop-interval-ticks"));
    }

    public static void stop() {
        if (loopTask != null) {
            loopTask.cancel();
            loopTask = null;
        }
    }

}
