package co.ghast.safeguardac.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.Set;

public class ConfigMan {

    private File file;
    private FileConfiguration config;

    public ConfigMan(String yml, File folder) {
        if (!folder.exists()) folder.mkdirs();
        this.file = new File(folder, yml);
        try {
            if(!file.exists()) file.createNewFile();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Set<String> getSection(String path, boolean deep) {
        return config.getConfigurationSection(path).getKeys(deep);
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
