package me.shockpast.rofl.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Invsee implements CommandExecutor {
    private final JavaPlugin plugin;

    public Invsee(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String line, @NotNull String[] args) {
        if (args.length < 1)
            return false;

        Player player = (Player)sender;
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(Component.text(args[0])
                    .color(TextColor.color(66, 135, 245))
                    .append(Component.text(" не существует на сервере.")));

            return true;
        }

        TextComponent message = Component.text("Вы начали просматривать, инвентарь игрока ")
                .append(Component.text(target.getName())
                        .color(TextColor.color(66, 135, 245)));

        player.sendMessage(message);

        player.getInventory().close();
        player.openInventory(target.getInventory())
                .setTitle(target.getName());

        return true;
    }
}
