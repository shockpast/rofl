package me.shockpast.roflan.constants;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class Message {
    public static final Component prefix = Component.text("[R] ", Colors.Yellow);

    public static void sendMessage(Audience audience, Component component) {
        audience.sendMessage(Message.prefix.append(component));
    }
}
