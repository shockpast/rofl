package me.shockpast.rofl.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.shockpast.rofl.SharedData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.Map;
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
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("rofl.command.vanish"))
            return;

        for (Map.Entry<UUID, Map<UUID, String>> reportEntry : data.reported_players.entrySet()) {
            Map.Entry<UUID, String> reportData = reportEntry.getValue().entrySet().iterator().next();

            OfflinePlayer complainer = Bukkit.getOfflinePlayer(reportEntry.getKey());
            OfflinePlayer target = Bukkit.getOfflinePlayer(reportData.getKey());
            String reason = reportData.getValue();

            if (complainer.getName() == null || target.getName() == null)
                continue;

            Bukkit.getScheduler().runTask(plugin, () ->
                player.sendMessage(Component.text(complainer.getName()).color(TextColor.color(66, 135, 245))
                        .append(Component.text(" отправил жалобу на ").color(TextColor.color(255, 255, 255))
                        .append(Component.text(target.getName()).color(TextColor.color(66, 135, 245))
                        .append(Component.text(" (%s)".formatted(reason)).color(TextColor.color(78, 78, 78)))))));
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
            player.sendMessage(Component.text("Вы не можете писать в чат, так-как вы были заглушены.")
                    .color(TextColor.color(240, 55, 55)));

            event.setCancelled(true);
        }
        else {
            data.muted_players.remove(uuid);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        BlockData blockData = block.getBlockData();

        if (!(blockData instanceof Stairs) && !(blockData instanceof Slab))
            return;

        Player player = event.getPlayer();
        if (player.isSneaking()) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) return;
        if (player.isInsideVehicle()) return;

        event.setCancelled(true);

        Location location = block.getLocation();
        location.setX(location.getX() + 0.5);
        location.setY(location.getY() - 0.4);
        location.setZ(location.getZ() + 0.5);

        if (blockData instanceof Directional) {
            BlockFace blockFace = ((Directional) blockData).getFacing();

            switch (blockFace) {
                case SOUTH -> location.setYaw(180);
                case WEST -> location.setYaw(270);
                case EAST -> location.setYaw(90);
                case NORTH -> location.setYaw(0);
            }
        } else {
            location.setYaw(player.getYaw() - 180);
        }

        if (blockData instanceof Stairs) {
            Stairs.Shape shape = ((Stairs)blockData).getShape();

            switch (shape) {
                case Stairs.Shape.INNER_RIGHT,
                     Stairs.Shape.OUTER_RIGHT -> location.setYaw(location.getYaw() + 45);
                case Stairs.Shape.INNER_LEFT,
                     Stairs.Shape.OUTER_LEFT -> location.setYaw(location.getYaw() - 45);
            }
        }

        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setBasePlate(false);
        armorStand.setArms(false);
        armorStand.setVisible(false);
        armorStand.setCanPickupItems(false);
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setAI(false);
        armorStand.addPassenger(player);
        armorStand.setMetadata("sit", new FixedMetadataValue(plugin, armorStand));
    }
}
