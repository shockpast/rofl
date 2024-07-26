package me.shockpast.roflan.listeners;

import me.shockpast.roflan.constants.Colors;
import me.shockpast.roflan.SharedData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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

        for (Map.Entry<UUID, String> report : data.player_reports.entrySet()) {
            Map.Entry<UUID, String> report_case = data.report_cases.get(report.getValue()).entrySet().iterator().next();

            OfflinePlayer complainer = Bukkit.getOfflinePlayer(report.getKey());
            OfflinePlayer target = Bukkit.getOfflinePlayer(report_case.getKey());

            if (target.getName() == null || complainer.getName() == null)
                continue;

            player.sendMessage(Component.text(complainer.getName(), Colors.Blue)
                    .append(Component.text(" отправил жалобу на ", Colors.White)
                    .append(Component.text(target.getName(), Colors.Blue))
                    .append(Component.text(" (%s)".formatted(report_case.getValue()), Colors.Gray))
                    .append(Component.text(" [%s]".formatted(report.getValue()), Colors.Gray)
                        .hoverEvent(HoverEvent.showText(Component.text("Тыкните, чтобы скопировать.")))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, report.getValue())))));
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
