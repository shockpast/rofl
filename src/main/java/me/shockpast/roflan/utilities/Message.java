package me.shockpast.roflan.utilities;

import me.shockpast.roflan.constants.Colors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class Message {
    public static final Component prefix = Component.text("[R] ", Colors.Yellow);

    public static void sendMessage(Audience audience, Component component) {
        audience.sendMessage(prefix.append(component));
    }
}
