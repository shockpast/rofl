package me.shockpast.rofl.commands;

import me.shockpast.rofl.constants.Colors;
import me.shockpast.rofl.SharedData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Vanish implements CommandExecutor {
    private final JavaPlugin plugin;
    private final SharedData data;

    public Vanish(JavaPlugin plugin, SharedData data) {
        this.plugin = plugin;
        this.data = data;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String line, @NotNull String[] args) {
        Player target;

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("Эта команда доступна только для игроков.", Colors.Red));

                return true;
            }

            target = (Player)sender;
        } else {
            target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(Component.text(args[0], Colors.Blue)
                        .append(Component.text(" не существует на сервере.", Colors.White)));

                return true;
            }
        }

        //
        UUID uuid = target.getUniqueId();
        boolean is_vanished = data.vanished_players.contains(uuid);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.equals(target))
                continue;

            if (is_vanished)
                player.showPlayer(plugin, target);
            else
                player.hidePlayer(plugin, target);
        }

        if (is_vanished)
            data.vanished_players.remove(uuid);
        else
            data.vanished_players.add(uuid);

        sender.sendMessage(Component.text(target.getName(), Colors.Blue)
                .append(Component.text(is_vanished ? " снова видимый" : " теперь невидимый", Colors.White)));

        return true;
    }
}
