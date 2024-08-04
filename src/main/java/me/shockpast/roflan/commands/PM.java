package me.shockpast.roflan.commands;

import java.util.Arrays;

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
import me.shockpast.roflan.utilities.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class PM implements CommandExecutor {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final FileConfiguration config;
    private final SharedData data;

    public PM(JavaPlugin plugin, SharedData data) {
        this.config = plugin.getConfig();
        this.data = data;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            Message.sendMessage(sender, Component.text("Вы не указали игрока или сообщение!", Colors.Red));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Message.sendMessage(sender, Component.text(args[0] + " не найден на сервере!", Colors.Red));
            return true;
        }

        String senderName = sender instanceof Player player ? player.getName() : "CONSOLE";
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Component senderFormat = miniMessage.deserialize(config.getString("chat.private.senderFormat"),
            Placeholder.parsed("sender", senderName),
            Placeholder.parsed("receiver", target.getName()),
            Placeholder.parsed("message", message));
        Message.sendRawMessage(sender, senderFormat);

        Component receiverFormat = miniMessage.deserialize(config.getString("chat.private.receiverFormat"),
            Placeholder.parsed("sender", senderName),
            Placeholder.parsed("receiver", target.getName()),
            Placeholder.parsed("message", message));
        Message.sendRawMessage(target, receiverFormat);

        if (sender instanceof Player player) {
            data.reply_data.put(player.getUniqueId(), target.getUniqueId());
            data.reply_data.put(target.getUniqueId(), player.getUniqueId());

            data.reply_memory.put(player.getUniqueId(), System.currentTimeMillis() + config.getLong("chat.private.cooldown") * 1000);
            data.reply_memory.put(target.getUniqueId(), System.currentTimeMillis() + config.getLong("chat.private.cooldown") * 1000);
        }

        return true;
    }
}
