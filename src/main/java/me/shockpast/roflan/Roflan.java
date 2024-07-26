package me.shockpast.roflan;

import me.shockpast.roflan.commands.*;
import me.shockpast.roflan.listeners.*;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Roflan extends JavaPlugin {
    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        saveDefaultConfig();

        //
        SharedData data = new SharedData();

        Server server = getServer();
        PluginManager pluginManager = server.getPluginManager();

        //
        pluginManager.registerEvents(new PlayerListener(this, data), this);
        pluginManager.registerEvents(new EntityListener(this), this);
        pluginManager.registerEvents(new TweakListener(this), this);

        //
        getCommand("vanish").setExecutor(new Vanish(this, data));
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("report").setExecutor(new Report(data));
        getCommand("item").setExecutor(new Item(this));
        getCommand("tweak").setExecutor(new Tweak(this));
    }
}
