package me.shockpast.roflan.utilities;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.shockpast.roflan.Roflan;

public class RFile {
    private static File file;
    private static FileConfiguration config;

    private final Roflan plugin = Roflan.getInstance();

    public RFile(String fileName) {
        file = new File(plugin.getDataFolder(), fileName + ".yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(fileName + ".yml", false);
        }

        try {
            config = new YamlConfiguration();
            config.load(file);
        } catch (Exception e) {
            plugin.getLogger().warning(fileName + ".yml couldn't be created/loaded.");
        }
    }

    public void save() {
        try {
            config.save(file);
            reload();
        } catch (Exception e) {
            plugin.getLogger().warning("RFile couldn't save/update configuration file.");
        }
    }

    public void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            plugin.getLogger().warning("RFile couldn't reload configuration file.");
        }
    }

    public FileConfiguration get() { return config; }
    public FileConfiguration getConfig() { return config; }
}
