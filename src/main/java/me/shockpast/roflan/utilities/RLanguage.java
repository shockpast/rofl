package me.shockpast.roflan.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public enum RLanguage {
    LANGUAGE_NAME("name"),

    NOTIFICATION_LANGUAGE_CHANGE("notification.language_change"),

    ERROR_GENERIC_CONSOLE("error.generic.console"),
    ERROR_GENERIC_TARGET("error.generic.target"),

    ERROR_PM_ARGUMENTS("error.commands.pm.arguments"),
    ERROR_REPLY_NONE("error.commands.reply.unknown"),
    ERROR_REPLY_LEFT("error.commands.reply.left"),
    ERROR_REPLY_TIMEOUT("error.commands.reply.timeout"),
    ERROR_ITEM_RENAME_COST("error.commands.item.rename.cost"),

    SUCCESS_VANISH_ENABLED("success.commands.vanish.enabled"),
    SUCCESS_VANISH_DISABLED("success.commands.vanish.disabled"),
    SUCCESS_FLY_ENABLED("success.commands.fly.enabled"),
    SUCCESS_FLY_DISABLED("success.commands.fly.disabled"),
    SUCCESS_ITEM_SIGN("success.commands.item.sign"),
    SUCCESS_ITEM_RENAME("success.commands.item.rename"),

    ITEM_SIGN_MESSAGE("message.item.sign");

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final String key;
    private String translation;

    private static final Map<String, Component> phraseCache = new HashMap<>();

    RLanguage(String key) {
        this.key = key;
        this.translation = key;
    }

    public String rawTranslation() {
        return translation;
    }

    public static RLanguage getByKey(String key) {
        for (RLanguage lang : values()) {
            if (lang.key.equals(key))
                return lang;
        }

        return null;
    }

    public Component asPhrase(Component... components) {
        return RLanguage.createPhrase(rawTranslation(), components);
    }

    public static void setLanguage(String name) {
        FileConfiguration file = new RFile("languages/" + name).get();
        FileConfiguration fallback = new RFile("languages/en_us").get();

        for (RLanguage lang : RLanguage.values()) {
            lang.translation = file.getString(lang.key, fallback.getString(lang.key, lang.translation));
        }

        RMessage.sendRawMessage(Bukkit.getConsoleSender(), RLanguage.NOTIFICATION_LANGUAGE_CHANGE.asPhrase());
    }

    public static Component createPhrase(String key, Component... components) {
        if (phraseCache.containsKey(key))
            return phraseCache.get(key);

        String converted = String.join("", convertStringToMini(key));

        List<TagResolver> resolvers = new ArrayList<>();
        for (int i = 0; i < components.length; i++) {
            resolvers.add(Placeholder.component(Integer.toString(i), components[i]));
        }

        Component phrase = miniMessage.deserialize(converted, resolvers.toArray(TagResolver[]::new));

        if (components.length == 0)
            phraseCache.put(key, phrase);

        return phrase;
    }

    private static List<String> convertStringToMini(String message) {
        return List.of(message.replace("{", "<").replace("}", ">").split("\\n"));
    }
}
