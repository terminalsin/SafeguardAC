package co.ghast.safeguardac.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {

    private FileConfiguration config;
    private File file;
    private String name;

    public Config(JavaPlugin Plugin, String Path, String Name) {
        this.file = new File(Plugin.getDataFolder() + Path);

        if (this.file.mkdirs()) {
            this.file = new File(Plugin.getDataFolder() + Path, Name + ".yml");
            try {
                this.file.createNewFile();
            } catch (IOException e) {

            }
        }

        this.name = Name;
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public String getName() {
        return this.name;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void setDefault(String Path, Object Set) {
        if (!this.getConfig().contains(Path)) {
            this.config.set(Path, Set);
            this.save();
        }
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {

        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
}

