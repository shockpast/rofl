package me.shockpast.roflan.commands;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import me.shockpast.roflan.SharedData;
import me.shockpast.roflan.constants.Colors;
import me.shockpast.roflan.utilities.RLanguage;
import me.shockpast.roflan.utilities.RMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class Reply implements CommandExecutor {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final FileConfiguration config;
    private final SharedData data;

    public Reply(JavaPlugin plugin, SharedData data) {
        this.config = plugin.getConfig();
        this.data = data;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player)sender;

        UUID tUUID = data.reply_data.get(player.getUniqueId());
        if (tUUID == null) {
            RMessage.sendMessage(player, RLanguage.ERROR_REPLY_NONE.asPhrase().color(Colors.Red));
            return true;
        }

        Player target = Bukkit.getPlayer(tUUID);
        if (target == null) {
            RMessage.sendMessage(player, RLanguage.ERROR_REPLY_LEFT.asPhrase().color(Colors.Red));
            return true;
        }

        if (data.reply_memory.get(player.getUniqueId()) <= System.currentTimeMillis()) {
            RMessage.sendMessage(player, RLanguage.ERROR_REPLY_TIMEOUT.asPhrase().color(Colors.Red));

            data.reply_data.remove(tUUID);
            data.reply_data.remove(player.getUniqueId());
            data.reply_memory.remove(tUUID);
            data.reply_memory.remove(player.getUniqueId());

            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        Component senderFormat = miniMessage.deserialize(config.getString("chat.private.senderFormat"),
            Placeholder.parsed("sender", player.getName()),
            Placeholder.parsed("receiver", target.getName()),
            Placeholder.parsed("message", message));
        RMessage.sendRawMessage(sender, senderFormat);

        Component receiverFormat = miniMessage.deserialize(config.getString("chat.private.receiverFormat"),
            Placeholder.parsed("sender", player.getName()),
            Placeholder.parsed("receiver", target.getName()),
            Placeholder.parsed("message", message));
        RMessage.sendRawMessage(target, receiverFormat);

        data.reply_data.put(player.getUniqueId(), target.getUniqueId());
        data.reply_data.put(target.getUniqueId(), player.getUniqueId());

        data.reply_memory.put(player.getUniqueId(), System.currentTimeMillis() + config.getLong("chat.private.cooldown") * 1000);
        data.reply_memory.put(target.getUniqueId(), System.currentTimeMillis() + config.getLong("chat.private.cooldown") * 1000);

        return true;
    }
}
