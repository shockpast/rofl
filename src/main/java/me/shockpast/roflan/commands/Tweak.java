package me.shockpast.roflan.commands;

import me.shockpast.roflan.constants.Colors;
import net.kyori.adventure.text.Component;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Tweak implements TabExecutor {
    private final JavaPlugin plugin;

    public Tweak(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("roflan.commands.tweak"))
            return true;
        if (args.length < 1)
            return false;

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("tweaks");
        if (section == null)
            return true;

        Player player = (Player)sender;

        if (args.length == 1) {
            Object value = section.get(args[0]);
            if (value == null)
                return true;

            player.sendMessage(Component.text(args[0], Colors.Blue)
                    .append(Component.text(" = ", Colors.White))
                    .append(Component.text(value.toString(), Boolean.parseBoolean(value.toString()) ? Colors.Green : Colors.Red)));

            return true;
        }

        String sub = args[0];
        String input = args[1];

        if (section.get(sub) == null)
            return false;

        plugin.getConfig().set("tweaks." + sub, Boolean.parseBoolean(input));
        plugin.saveConfig();

        player.sendMessage(Component.text(sub, Colors.Blue)
                .append(Component.text(" = ", Colors.White))
                .append(Component.text(input, input.equals("true") ? Colors.Green : Colors.Red)));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("tweaks");
            if (section == null)
                return List.of();

            return section.getKeys(true).stream().toList();
        }

        if (args.length == 2)
            return List.of("true", "false");

        return List.of();
    }
}
