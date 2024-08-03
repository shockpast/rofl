package me.shockpast.roflan.listeners;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.shockpast.roflan.constants.Colors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class ChatListener implements Listener, ChatRenderer {
    private final FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ChatListener(JavaPlugin plugin) {
        this.config = plugin.getConfig();
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        if (viewer instanceof Player pViewer) {
            String textMessage = miniMessage.serialize(message);
            String textName = miniMessage.escapeTags(miniMessage.serialize(pViewer.displayName()));

            String mentionText = this.config.getString("chat.modules.mention.prefix") + textName;

            System.out.println(textMessage.contains(mentionText));
            System.out.println(mentionText);
            System.out.println(textMessage);
            System.out.println(textName);

            if (textMessage.contains(mentionText) && config.getBoolean("chat.modules.mention.enabled")) {
                message = message.replaceText(config -> config
                    .matchLiteral(mentionText)
                    .replacement(text -> text.color(Colors.Yellow)));

                viewer.playSound(Sound.sound(Key.key(config.getString("chat.modules.mention.sound")), Sound.Source.PLAYER, 1f, 1f));
            }
        }

        return miniMessage.deserialize(config.getString("chat.public.format"),
            Placeholder.component("name", sourceDisplayName),
            Placeholder.component("message", message));
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;

        Component message = event.message();

        // FROM: i'm here :loc:
        // TO:   i'm here [10, 67, -103]
        if (config.getBoolean("chat.modules.location")) {
            Location location = player.getLocation();
            String text = "[%d, %d, %d]".formatted((int)location.getX(), (int)location.getY(), (int)location.getZ());

            message = message.replaceText(config -> config
                .matchLiteral(":loc:")
                .replacement(text)
                .once());
        }

        event.message(message);
        event.renderer(this);
    }
}
