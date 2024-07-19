package me.shockpast.rofl;

import me.shockpast.rofl.commands.Invsee;
import me.shockpast.rofl.commands.Vanish;
import me.shockpast.rofl.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rofl extends JavaPlugin {
    @Override
    public void onEnable() {
        SharedData data = new SharedData();

        //
        getServer().getPluginManager().registerEvents(new PlayerListener(this, data), this);

        //
        getCommand("vanish").setExecutor(new Vanish(this, data));
        getCommand("invsee").setExecutor(new Invsee(this));
    }

    @Override
    public void onDisable() {
    }
}
