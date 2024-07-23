package me.shockpast.roflan;

import me.shockpast.roflan.commands.*;
import me.shockpast.roflan.listeners.EntityListener;
import me.shockpast.roflan.listeners.PlayerListener;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public final class Roflan extends JavaPlugin {
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
        getCommand("item").setExecutor(new Item());

        // Additional Permissions
        server.getPluginManager().addPermission(new Permission("roflan.command.report.send"));
        server.getPluginManager().addPermission(new Permission("roflan.command.report.close"));
    }
}
