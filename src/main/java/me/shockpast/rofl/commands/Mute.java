package me.shockpast.rofl.commands;

import me.shockpast.rofl.SharedData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mute implements CommandExecutor, TabExecutor {
    private final JavaPlugin plugin;
    private final SharedData data;

    public Mute(JavaPlugin plugin, SharedData data) {
        this.plugin = plugin;
        this.data = data;
    }

    // this code is mess lol
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String line, @NotNull String[] args) {
        if (args.length < 3)
            return false;

        Player target = Bukkit.getPlayer(args[0]);
        Player player = (Player)sender;

        if (target == null) {
            sender.sendMessage(Component.text(args[0])
                    .color(TextColor.color(66, 135, 245))
                    .append(Component.text(" не существует на сервере.")));

            return true;
        }

        Duration duration;
        String reason;

        try {
            duration = Duration.parse("PT%s".formatted(args[1]).toUpperCase());
            reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length)); // wtf is this
        } catch (DateTimeParseException exc) {
            player.sendMessage(Component.text("Мы не смогли прочитать длительность, попробуйте ещё раз.")
                    .color(TextColor.color(240, 55, 55)));

            return true;
        }

        data.muted_players.putIfAbsent(target.getUniqueId(), Instant.now().getEpochSecond() + duration.getSeconds());

        // HAHAHAHAHAAHAHAAHAHAHAHAHA
        plugin.getServer().broadcast(Component.text(player.getName())
                .color(TextColor.color(66, 135, 245))
                .append(Component.text(" выдал мут ")
                        .color(TextColor.color(255, 255, 255)))
                .append(Component.text(target.getName())
                        .color(TextColor.color(66, 135, 245)))
                .append(Component.text(" на ")
                        .color(TextColor.color(255, 255, 255)))
                .append(Component.text(args[1])
                        .color(TextColor.color(130, 255, 115)))
                .append(Component.text(" (%s)".formatted(reason))
                        .color(TextColor.color(78, 78, 78))));

        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () ->
                data.muted_players.remove(target.getUniqueId()), duration.getSeconds() * 20L);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String line, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();

        return switch (args.length) {
            case 1 -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    tab.add(player.getName());
                }
                yield tab;
            }
            case 2 -> Arrays.asList("1s", "1m", "1h");
            case 3 -> Arrays.asList("Спам", "Флуд", "Плохой");
            default -> new ArrayList<>();
        };
    }
}
