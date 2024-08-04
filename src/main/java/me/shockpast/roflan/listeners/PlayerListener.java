package me.shockpast.roflan.listeners;

import me.shockpast.roflan.SharedData;
import net.kyori.adventure.text.Component;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
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
        UUID pUUID = player.getUniqueId();

        if (data.vanished_players.contains(pUUID)) {
            data.vanished_players.remove(pUUID);
            event.quitMessage(Component.empty());
        }

        if (data.reply_data.containsKey(pUUID)) {
            UUID tUUID = data.reply_data.get(pUUID);

            data.reply_data.remove(pUUID);
            data.reply_memory.remove(pUUID);
            data.reply_data.remove(tUUID);
            data.reply_memory.remove(tUUID);
        }

        if (!player.getMetadata("sit").isEmpty()) {
            Pig pig = (Pig)player.getMetadata("sit").getFirst().value();
            pig.remove();
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
        if (blockData instanceof Slab slab && slab.getType() != Slab.Type.BOTTOM)
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
                case WEST ->  location.setYaw(270);
                case EAST ->  location.setYaw(90);
                case NORTH -> location.setYaw(0);
                default ->    location.setYaw(0);
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
                default -> location.setYaw(location.getYaw());
            }
        }

        Pig pig = location.getWorld().spawn(location, Pig.class);
        pig.setAI(false);
        pig.setAgeLock(true);
        pig.setAggressive(false);
        pig.setInvisible(true);
        pig.setInvulnerable(true);
        pig.setGravity(false);
        pig.setSilent(true);
        pig.addPassenger(player);
        pig.setMetadata("sit", new FixedMetadataValue(plugin, pig));
        player.setMetadata("sit", new FixedMetadataValue(plugin, pig));
    }
}
