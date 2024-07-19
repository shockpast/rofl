package me.shockpast.rofl.listeners;

import me.shockpast.rofl.SharedData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
            event.quitMessage(Component.text().build()); // just send nothing at all?
        }
    }
}
