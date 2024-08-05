package me.shockpast.roflan.commands;

import me.shockpast.roflan.constants.Colors;
import me.shockpast.roflan.utilities.RLanguage;
import me.shockpast.roflan.utilities.RMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Item implements TabExecutor {
    private final JavaPlugin plugin;

    public Item(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0].equals("rename") && args.length >= 2) {
            Player player = (Player)sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();

            if (item.getType() == Material.AIR)
                return true;

            int cost = plugin.getConfig().getInt("commands.item.renameCost");
            if (player.getLevel() < cost) {
                RMessage.sendMessage(player, RLanguage.ERROR_ITEM_RENAME_COST.asPhrase(Component.text(cost)));
                return true;
            }

            meta.displayName(MiniMessage.miniMessage()
                .deserialize(String.join(" ", Arrays.copyOfRange(args, 1, args.length))));
            item.setItemMeta(meta);

            player.setLevel(player.getLevel() - cost);
            RMessage.sendMessage(player, RLanguage.SUCCESS_ITEM_RENAME.asPhrase().color(Colors.Green));

            return true;
        }

        if (args[0].equals("sign")) {
            Player player = (Player)sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();

            if (item.getType() == Material.AIR)
                return true;

            Date unix = new Date();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(unix);
            String time = new SimpleDateFormat("hh:mm:ss").format(unix);

            Component tag = RLanguage.ITEM_SIGN_MESSAGE.asPhrase(
                player.displayName().color(Colors.Blue),
                Component.text(date, Colors.Blue),
                Component.text(time, Colors.Blue)
            );

            meta.lore(List.of(tag));
            item.setItemMeta(meta);

            RMessage.sendMessage(player, RLanguage.SUCCESS_ITEM_SIGN.asPhrase().color(Colors.Green));
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1)
            return List.of("rename", "sign");

        return List.of();
    }
}
