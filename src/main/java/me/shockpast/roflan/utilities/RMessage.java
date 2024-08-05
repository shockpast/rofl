package me.shockpast.roflan.utilities;

import me.shockpast.roflan.constants.Colors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public class RMessage {
    public static final Component prefix = Component.text("[R] ", Colors.Yellow);

    /**
     * Sends Message with prefix of RoflanUtils, simply a wrapper.
     *
     * @param audience
     * @param component
     *
     */
    public static void sendMessage(Audience audience, Component component) {
        audience.sendMessage(prefix.append(component));
    }

    /**
     * Sends Message without a prefix of RoflanUtils, simply a wrapper.
     *
     * @param audience
     * @param component
     */
    public static void sendRawMessage(Audience audience, Component component) {
        audience.sendMessage(component);
    }
}
