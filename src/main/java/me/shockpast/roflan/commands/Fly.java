package me.shockpast.roflan.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.shockpast.roflan.utilities.RLanguage;
import me.shockpast.roflan.utilities.RMessage;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player)sender;
        boolean canFly = player.getAllowFlight();

        Sound sound = Sound.sound(Key.key("minecraft:block.amethyst_block.resonate"), Sound.Source.MASTER, 1f, !canFly ? 2f : 0.5f);

        player.setAllowFlight(!canFly);
        player.playSound(sound);

        Component message = !canFly
            ? RLanguage.SUCCESS_FLY_ENABLED.asPhrase()
            : RLanguage.SUCCESS_FLY_DISABLED.asPhrase();

        RMessage.sendMessage(player, message);

        return true;
    }
}
