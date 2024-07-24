package me.shockpast.roflan.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityListener implements Listener {
    private final JavaPlugin plugin;

    public EntityListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        Entity player = event.getEntity();
        Entity entity = event.getDismounted();

        if (entity.getType() != EntityType.ARMOR_STAND)
            return;
        if (entity.getMetadata("sit").isEmpty())
            return;

        Location location = player.getLocation();
        location.setY(location.getY() + 1.7);

        player.teleport(location);
        entity.remove();
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplosion(ExplosionPrimeEvent event) {
        if (this.plugin.getConfig().getBoolean("tweaks.explosionNoBlockDamage"))
            event.setCancelled(true);
    }
}
