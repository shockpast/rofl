package me.shockpast.rofl.commands;

import me.shockpast.rofl.Colors;
import me.shockpast.rofl.SharedData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.RandomStringUtils;
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
        if (args[0].equals("send") && args.length >= 3 && sender.hasPermission("rofl.command.report.send")) {
            String id = RandomStringUtils.randomAlphanumeric(6);

            Player player = (Player)sender;
            Player target = Bukkit.getPlayer(args[1]) == null ? (Player)Bukkit.getOfflinePlayer(args[1]) : Bukkit.getPlayer(args[1]);

            boolean was_reported = data.report_cases.get(data.player_reports.get(player.getUniqueId())) != null;
            if (was_reported)
                return true;

            data.player_reports.put(player.getUniqueId(), id);

            if (target == null)
                return true;

            String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            Map<UUID, String> report = new HashMap<>();

            report.put(target.getUniqueId(), reason);
            data.report_cases.put(id, report);

            player.sendMessage(Component.text("ID жалобы: ")
                    .append(Component.text(id, Colors.Blue)
                        .hoverEvent(HoverEvent.showText(Component.text("Тыкните, чтобы скопировать.")))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, id))));

            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (!staff.hasPermission("rofl.command.vanish"))
                    continue;

                staff.sendMessage(Component.text(player.getName(), Colors.Blue)
                        .append(Component.text(" отправил жалобу на ", Colors.White)
                        .append(Component.text(target.getName(), Colors.Blue)
                        .append(Component.text(" (%s)".formatted(reason), Colors.Gray)))));
            }

            return true;
        }

        if (args[0].equals("close") && args.length == 2 && sender.hasPermission("rofl.command.report.close")) {
            String id = args[1];
            Player player = (Player)sender;

            Map<UUID, String> report = data.report_cases.get(id);
            if (report == null)
                return true;

            @SuppressWarnings("OptionalGetWithoutIsPresent")
            UUID cUUID = data.player_reports.entrySet()
                    .stream().filter(entry -> Objects.equals(entry.getValue(), id))
                    .map(Map.Entry::getKey)
                    .findFirst().get();

            Player complainer = Bukkit.getPlayer(cUUID) == null ? (Player)Bukkit.getOfflinePlayer(cUUID) : Bukkit.getPlayer(cUUID);
            if (complainer == null)
                return true;

            OfflinePlayer target = Bukkit.getOfflinePlayer(report.entrySet().iterator().next().getKey());
            if (target.getName() == null)
                return true;

            if (complainer.isOnline()) {
                complainer.sendMessage(Component.text(player.getName(), Colors.Blue)
                        .append(Component.text(" закрыл вашу жалобу на ", Colors.White)
                        .append(Component.text(target.getName(), Colors.Blue))
                        .append(Component.text(" [%s]".formatted(id), Colors.Gray)
                            .hoverEvent(HoverEvent.showText(Component.text("Тыкните, чтобы скопировать.")))
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, id)))));
            }

            player.sendMessage(Component.text("Вы успешно закрыли жалобу игрока ")
                    .append(Component.text(complainer.getName(), Colors.Blue)
                    .append(Component.text(" на игрока ", Colors.White)
                    .append(Component.text(target.getName(), Colors.Blue))
                    .append(Component.text(" [%s]".formatted(id), Colors.Gray)
                            .hoverEvent(HoverEvent.showText(Component.text("Тыкните, чтобы скопировать.")))
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, id))))));

            data.player_reports.remove(complainer.getUniqueId(), id);
            data.report_cases.remove(id);

            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String line, @NotNull String[] args) {
        if (args.length == 1)
            return List.of("send", "close");

        if (args.length == 2 && args[0].equals("close"))
            return data.report_cases.keySet().stream().toList();

        if (args.length == 2 && args[0].equals("send")) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }

            return players;
        };

        return new ArrayList<>();
    }
}
