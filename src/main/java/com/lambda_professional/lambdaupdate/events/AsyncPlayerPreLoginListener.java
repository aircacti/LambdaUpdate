package com.lambda_professional.lambdaupdate.events;

import com.lambda_professional.lambdaupdate.core.Config;
import com.lambda_professional.lambdaupdate.core.Kernel;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (Kernel.duringUpdate()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(Config.getString("kick-message")));
        }
    }
}
