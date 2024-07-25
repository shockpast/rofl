package me.shockpast.roflan.commands;

import me.shockpast.roflan.constants.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class Item implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0].equals("rename") && args.length >= 2) {
            Player player = (Player)sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();

            if (item.getType() == Material.AIR) {
                player.sendMessage(Component.text("Вы не можете переименовать... воздух?", Colors.Red));
                return true;
            }

            if (player.getLevel() < 1) {
                player.sendMessage(Component.text("Чтобы переименовать предмет, требуется минимум 1 уровень опыта.", Colors.Red));
                return true;
            }

            meta.displayName(MiniMessage.miniMessage()
                    .deserialize(String.join(" ", Arrays.copyOfRange(args, 1, args.length))));
            item.setItemMeta(meta);

            player.setLevel(player.getLevel() - 1);
            player.sendMessage(Component.text("Название было успешно изменено.", Colors.Green));

            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1)
            return List.of("rename");

        return List.of();
    }
}
