package me.shockpast.rofl.commands;

import me.shockpast.rofl.SharedData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Report implements CommandExecutor, TabExecutor {
    private final SharedData data;

    public Report(SharedData data) {
        this.data = data;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String line, @NotNull String[] args) {
        if (args.length < 2)
            return false;

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Эта команда доступна только для игроков.")
                    .color(TextColor.color(240, 55, 55)));

            return true;
        }

        if (args[0].equals("close")) {
            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

            if (target == null || target.getName() == null) {
                player.sendMessage(Component.text(args[1]).color(TextColor.color(66, 135, 245))
                        .append(Component.text(" никогда не заходил на сервер.").color(TextColor.color(255, 255, 255))));

                return true;
            }

            if (data.reported_players.get(target.getUniqueId()) == null) {
                player.sendMessage(Component.text(target.getName()).color(TextColor.color(66, 135, 245))
                        .append(Component.text(" не имеет открытой жалобы на своём счету.").color(TextColor.color(255, 255, 255))));

                return true;
            }

            UUID uuid = data.reported_players.entrySet()
                    .stream().filter(entry -> target.getUniqueId().equals(entry.getKey()))
                    .map(Map.Entry::getKey).findFirst().get();

            OfflinePlayer oComplainer = Bukkit.getOfflinePlayer(uuid);
            data.reported_players.remove(target.getUniqueId());

            if (oComplainer.isOnline()) {
                Player pComplainer = Bukkit.getPlayer(uuid);
                pComplainer.sendMessage(Component.text(player.getName()).color(TextColor.color(66, 135, 245))
                        .append(Component.text(" закрыл вашу жалобу на ").color(TextColor.color(255, 255, 255))
                        .append(Component.text(target.getName()).color(TextColor.color(66, 135, 245)))));
            }

            player.sendMessage(Component.text("Вы закрыли жалобу ")
                    .append(Component.text(oComplainer.getName()).color(TextColor.color(66, 135, 245)))
                    .append(Component.text(" на игрока ").color(TextColor.color(255, 255, 255)))
                    .append(Component.text(target.getName()).color(TextColor.color(66, 135, 245))));

            return true;
        }

        if (args[0].equals("send")) {
            if (args.length < 3)
                return false;

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage(Component.text(args[1]).color(TextColor.color(66, 135, 245))
                        .append(Component.text(" не существует на сервере.").color(TextColor.color(255, 255, 255))));

                return true;
            }

            String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

            Map<UUID, String> report = new HashMap<>();
            report.putIfAbsent(target.getUniqueId(), reason);

            Map<UUID, String> previous_report = data.reported_players.get(player.getUniqueId());

            if (previous_report == null) {
                data.reported_players.putIfAbsent(player.getUniqueId(), report);

                player.sendMessage(Component.text("Вы успешно отправили жалобу на ")
                        .append(Component.text(target.getName())
                                .color(TextColor.color(66, 135, 245))));

                for (Player online_player : Bukkit.getOnlinePlayers()) {
                    if (online_player.hasPermission("rofl.command.vanish")) {
                        online_player.sendMessage(Component.text(player.getName()).color(TextColor.color(66, 135, 245))
                                .append(Component.text(" отправил жалобу на ").color(TextColor.color(255, 255, 255))
                                        .append(Component.text(target.getName()).color(TextColor.color(66, 135, 245))
                                                .append(Component.text(" (%s)".formatted(reason)).color(TextColor.color(78, 78, 78))))));
                    }
                }
            } else {
                if (previous_report.containsKey(target.getUniqueId())) {
                    player.sendMessage(Component.text("Вы уже отправляли жалобу на ")
                            .append(Component.text(target.getName()).color(TextColor.color(66, 135, 245))));
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String line, @NotNull String[] args) {
        return switch (args.length) {
            case 1 -> List.of("send", "close");
            case 2 -> {
                List<String> tab = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    tab.add(player.getName());
                }

                yield tab;
            }
            default -> new ArrayList<>();
        };
    }
}
