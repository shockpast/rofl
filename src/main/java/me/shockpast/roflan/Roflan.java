package me.shockpast.roflan;

import me.shockpast.roflan.commands.*;
import me.shockpast.roflan.listeners.*;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
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

        //
        getCommand("vanish").setExecutor(new Vanish(this, data));
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("mute").setExecutor(new Mute(this, data));
        getCommand("report").setExecutor(new Report(data));
        getCommand("item").setExecutor(new Item());
        getCommand("tweak").setExecutor(new Tweak(this));

        // Additional Permissions
        pluginManager.addPermission(new Permission("roflan.command.report.send"));
        pluginManager.addPermission(new Permission("roflan.command.report.close"));
    }
}
