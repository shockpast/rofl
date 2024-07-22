package me.shockpast.rofl;

import me.shockpast.rofl.commands.*;
import me.shockpast.rofl.listeners.EntityListener;
import me.shockpast.rofl.listeners.PlayerListener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rofl extends JavaPlugin {
    @Override
    public void onEnable() {
        SharedData data = new SharedData();

        //
        getServer().getPluginManager().registerEvents(new PlayerListener(this, data), this);
        getServer().getPluginManager().registerEvents(new EntityListener(), this);

        //
        getCommand("vanish").setExecutor(new Vanish(this, data));
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("mute").setExecutor(new Mute(this, data));
        getCommand("report").setExecutor(new Report(data));

        // Additional Permissions
        getServer().getPluginManager().addPermission(new Permission("rofl.command.report.send"));
        getServer().getPluginManager().addPermission(new Permission("rofl.command.report.close"));

        //
        this.getLogger().info("""
         \n
         /$$$$$$$   /$$$$$$  /$$$$$$$$ /$$     \s
        | $$__  $$ /$$__  $$| $$_____/| $$     \s
        | $$  \\ $$| $$  \\ $$| $$      | $$     \s
        | $$$$$$$/| $$  | $$| $$$$$   | $$     \s
        | $$__  $$| $$  | $$| $$__/   | $$     \s
        | $$  \\ $$| $$  | $$| $$      | $$     \s
        | $$  | $$|  $$$$$$/| $$      | $$$$$$$$
        |__/  |__/ \\______/ |__/      |________/
                            is now enabled, thanks.
        """);
    }
}
