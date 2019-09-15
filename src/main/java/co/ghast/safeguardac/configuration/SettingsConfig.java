package co.ghast.safeguardac.configuration;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.checks.Check;
import co.ghast.safeguardac.util.ConfigMan;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SettingsConfig extends ConfigMan{

    private FileConfiguration config;
    @Getter private String prefix;
    @Getter @Setter private int cooldown;
    @Getter @Setter private int banTimer;
    @Getter @Setter private String detectionMode;
    @Getter @Setter private boolean logBans;
    @Getter private List<String> punishCommands;
    @Getter private List<String> chatComplete;
    @Getter @Setter private int rotatingPlayers;


    public SettingsConfig() {
        super("settings.yml", SafeGuard.getInstance().getDataFolder());
        this.config = getConfig();
        genConfig();
    }

    public void genConfig() {
        if (!config.contains("prefix")) {
            config.set("prefix", "&8&l[&6⚓&8] &e");
            config.set("command-prefix", "sg");
            config.set("cooldown", 1000);
            config.set("autoban-time", 10);
            config.set("log-bans", true);
            List<String> cmdlist = new ArrayList<>();
            cmdlist.add("ban %player% &7(&6⚓&7)&e Unfair Advantage \n &eDetected: &7(%check%)");
            cmdlist.add("your-command");
            cmdlist.add("as-many-as-you-want-just-follow-format");
            config.set("punish-commands", cmdlist);
            List<String> chatComplete = new ArrayList<>();
            chatComplete.add(".friend");
            chatComplete.add(".killaura");
            config.set("chat-complete-blacklist", chatComplete);
            config.set("player-rotating-detection", 5);
            config.set("detection-mode", "REGULAR");
            saveConfig();
        }
        this.prefix = config.getString("detection-mode");
        this.prefix = config.getString("prefix");
        this.logBans = config.getBoolean("log-bans");
        this.cooldown = config.getInt("cooldown");
        this.banTimer = config.getInt("banTimer");
        this.rotatingPlayers = config.getInt("player-rotating-detection");
        this.punishCommands = config.getStringList("punish-commands");
        this.chatComplete = config.getStringList("chat-complete-blacklist");
    }

    public void reload() {
        reloadConfig();
        this.config = getConfig();
        genConfig();
        SafeGuard.getInstance().reloadPrefix();
        SafeGuard.getInstance().reloadCooldown();
    }
    public void multipleCommands(Player p, Check check) {
        for (String command : punishCommands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%check%", check.getName()).replace("%player%", p.getName()));
        }
    }


}
