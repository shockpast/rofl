package me.shockpast.rofl.commands;

import me.shockpast.rofl.constants.Colors;
import me.shockpast.rofl.SharedData;
import net.kyori.adventure.text.Component;
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String line, @NotNull String[] args) {
        if (args.length < 3)
            return false;

        Player target = Bukkit.getPlayer(args[0]);
        Player player = (Player)sender;

        if (target == null) {
            sender.sendMessage(Component.text(args[0], Colors.Blue)
                    .append(Component.text(" не существует на сервере.")));

            return true;
        }

        Duration duration;
        String reason;

        try {
            duration = Duration.parse("PT%s".formatted(args[1]).toUpperCase());
            reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length)); // wtf is this
        } catch (DateTimeParseException exc) {
            player.sendMessage(Component.text("Мы не смогли прочитать длительность, попробуйте ещё раз.", Colors.Red));

            return true;
        }

        data.muted_players.putIfAbsent(target.getUniqueId(), Instant.now().getEpochSecond() + duration.getSeconds());

        plugin.getServer().broadcast(Component.text(player.getName(), Colors.Blue)
                .append(Component.text(" выдал мут ", Colors.White))
                .append(Component.text(target.getName(), Colors.Blue))
                .append(Component.text(" на ", Colors.White))
                .append(Component.text(args[1], Colors.Yellow))
                .append(Component.text(" (%s)".formatted(reason), Colors.Gray)));

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
            case 3 -> Arrays.asList("Спам", "Флуд", "Оскорбление");
            default -> new ArrayList<>();
        };
    }
}
