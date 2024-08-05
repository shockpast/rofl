package me.shockpast.roflan.listeners;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import me.shockpast.roflan.constants.Colors;
import me.shockpast.roflan.utilities.RLanguage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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
        String renderText = config.getString("chat.public.format");

        if (viewer instanceof Player pViewer) {
            String textMessage = miniMessage.serialize(message);
            String textName = miniMessage.escapeTags(miniMessage.serialize(pViewer.displayName()));
            String textMention = this.config.getString("chat.modules.mention.prefix") + textName;

            if (textMessage.contains(textMention) && config.getBoolean("chat.modules.mention.enabled")) {
                message = message.replaceText(config -> config
                    .matchLiteral(textMention)
                    .replacement(text -> text.color(Colors.Yellow)));

                viewer.playSound(Sound.sound(Key.key(config.getString("chat.modules.mention.sound")), Sound.Source.PLAYER, 1f, 1f));
            }

            renderText = PlaceholderAPI.setPlaceholders(pViewer, renderText);
        }

        return miniMessage.deserialize(renderText,
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
            Component tag = Component.text(text, Colors.Green);

            message = message.replaceText(config -> config
                .matchLiteral(":loc:")
                .replacement(tag)
                .once());
        }

        if (config.getBoolean("chat.modules.gentleman"))

        event.message(message);
        event.renderer(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ClickEvent helloClick = ClickEvent.suggestCommand(miniMessage.serialize(RLanguage.ACTION_SAY_HELLO.asPhrase(player.displayName())));
        HoverEvent<Component> tipHover = HoverEvent.showText(RLanguage.ACTION_SAY_HELLO_DESC.asPhrase(player.displayName()));

        String renderText = config.getString("chat.public.join.format");
        renderText = PlaceholderAPI.setPlaceholders(player, renderText);

        event.joinMessage(miniMessage.deserialize(renderText, Placeholder.component("name", player.displayName()))
            .clickEvent(helloClick)
            .hoverEvent(tipHover));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        String renderText = config.getString("chat.public.quit.format");
        renderText = PlaceholderAPI.setPlaceholders(player, renderText);

        event.quitMessage(miniMessage.deserialize(renderText,
            Placeholder.component("name", player.displayName())));
    }
}
