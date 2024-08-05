package me.shockpast.roflan.commands;

import me.shockpast.roflan.utilities.RLanguage;
import me.shockpast.roflan.utilities.RMessage;
import me.shockpast.roflan.SharedData;
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
        Player player = (Player)sender;

        //
        UUID pUUID = player.getUniqueId();
        boolean is_vanished = data.vanished_players.contains(pUUID);

        for (Player oPlayer : Bukkit.getOnlinePlayers()) {
            if (oPlayer.equals(player))
                continue;

            if (is_vanished)
                oPlayer.showPlayer(plugin, player);
            else
                oPlayer.hidePlayer(plugin, player);
        }

        if (is_vanished)
            data.vanished_players.remove(pUUID);
        else
            data.vanished_players.add(pUUID);

        Component message = is_vanished
            ? RLanguage.SUCCESS_VANISH_DISABLED.asPhrase()
            : RLanguage.SUCCESS_FLY_ENABLED.asPhrase();

        RMessage.sendMessage(sender, message);

        return true;
    }
}
