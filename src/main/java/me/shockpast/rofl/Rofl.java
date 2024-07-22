package me.shockpast.rofl;

import me.shockpast.rofl.commands.*;
import me.shockpast.rofl.listeners.EntityListener;
import me.shockpast.rofl.listeners.PlayerListener;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rofl extends JavaPlugin {
    @Override
    public void onEnable() {
        SharedData data = new SharedData();

        Server server = getServer();

        //
        server.getPluginManager().registerEvents(new PlayerListener(this, data), this);
        server.getPluginManager().registerEvents(new EntityListener(), this);

        //
        getCommand("vanish").setExecutor(new Vanish(this, data));
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("mute").setExecutor(new Mute(this, data));
        getCommand("report").setExecutor(new Report(data));

        // Additional Permissions
        server.getPluginManager().addPermission(new Permission("rofl.command.report.send"));
        server.getPluginManager().addPermission(new Permission("rofl.command.report.close"));
    }
}
