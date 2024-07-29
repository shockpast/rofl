package me.shockpast.roflan.listeners;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

public class ChatListener implements Listener {
    private final FileConfiguration config;

    public ChatListener(JavaPlugin plugin) {
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void chatModuleEvent(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;

        Component message = event.message();

        // FROM: i'm here :loc:
        // TO:   i'm here [10, 67, -103]
        if (config.getBoolean("features.chat_modules.location")) {
            Location location = player.getLocation();
            String text = "[%d, %d, %d]".formatted((int)location.getX(), (int)location.getY(), (int)location.getZ());

            message = message.replaceText(TextReplacementConfig.builder()
                .matchLiteral(":loc:")
                .replacement(text).once().build());
        }

        event.message(message);
    }
}
