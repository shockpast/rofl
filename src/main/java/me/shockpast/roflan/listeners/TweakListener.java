package me.shockpast.roflan.listeners;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.event.player.PlayerItemCooldownEvent;

public class TweakListener implements Listener {
    private final JavaPlugin plugin;
    private final FileConfiguration config;

    public TweakListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void creativeNetherWaterPlacement(PlayerBucketEmptyEvent event) {
        if (!config.getBoolean("tweaks.creativeNetherWaterPlacement"))
            return;

        World world = event.getBlock().getWorld();
        if (world.getEnvironment() != Environment.NETHER)
            return;

        Block block = event.getBlockClicked();
        BlockFace blockFace = event.getBlockFace();

        block.getRelative(blockFace)
            .setType(Material.WATER);
    }

    @EventHandler
    public void creativeNoItemCooldown(PlayerItemCooldownEvent event) {
        if (!config.getBoolean("tweaks.creativeNoItemCooldown"))
            return;

        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE)
            return;

        event.setCooldown(0);
    }

    @EventHandler
    public void creativeOpenContainerForcibly(PlayerInteractEvent event) {
        if (!config.getBoolean("tweaks.creativeOpenContainerForcibly"))
            return;

        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE)
            return;

        Block block = event.getClickedBlock();
        if (block == null)
            return;
        if (!(block.getState() instanceof Chest))
            return;

        Chest chest = (Chest)block.getState();
        if (chest.isBlocked() || chest.isLocked()) {
            player.openInventory(chest.getInventory());
            player.setMetadata("creativeOpenContainerForcibly", new FixedMetadataValue(plugin, chest));

            chest.open();
        }
    }

    @EventHandler
    public void creativeOpenContainerForcibly_InventoryClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        if (player == null)
            return;

        List<MetadataValue> meta = player.getMetadata("creativeOpenContainerForcibly");
        if (meta.isEmpty())
            return;

        Chest chest = (Chest)meta.getFirst().value();
        chest.close();
    }
}
