package me.shockpast.rofl.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.shockpast.rofl.SharedData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class PlayerListener implements Listener {
    private final JavaPlugin plugin;
    private final SharedData data;

    public PlayerListener(JavaPlugin plugin, SharedData data) {
        this.plugin = plugin;
        this.data = data;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (data.vanished_players.contains(player.getUniqueId())) {
            data.vanished_players.remove(player.getUniqueId());
            event.quitMessage(Component.empty());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (!event.isAsynchronous())
            return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (data.muted_players.get(uuid) == null)
            return;

        if (Instant.now().getEpochSecond() - data.muted_players.get(uuid) <= 0) {
            player.sendMessage(Component.text("Вы не можете писать в чат, так-как были глобально заглушены.")
                    .color(TextColor.color(240, 55, 55)));

            event.setCancelled(true);
        }
        else {
            data.muted_players.remove(uuid);
        }
    }
}
