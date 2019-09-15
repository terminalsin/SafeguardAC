package co.ghast.safeguardac.configuration;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.util.ConfigMan;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessagesConfig extends ConfigMan {

    private SafeGuard sg;
    private FileConfiguration config;
    @Getter private String banTimerMessage;
    @Getter private List<String> punishMessage;
    @Getter private List<String> sgHelp;


    public MessagesConfig(SafeGuard sg) {
        super("messages.yml", SafeGuard.getInstance().getDataFolder());
        this.config = getConfig();
        this.sg = sg;
        genConfig();
    }

    public void genConfig() {
        if (!config.contains("ban-timer-message")) {
            config.set("ban-timer-message", "&c%player% &7will be banned in 5 seconds. &cReason &7[&c%check%&7]");

            // Cmd list message
            List<String> cmdlistMessage = new ArrayList<>();
            cmdlistMessage.add("&8&m----------------&8&l" + sg.getPrefix() + "&8&m----------------&8&b");
            cmdlistMessage.add("&6&l/sg &7>>&e Prints out this message");
            cmdlistMessage.add("&6&l/sg &e&lgui &7>>&e Opens the configuration menu");
            cmdlistMessage.add("&6&l/sg &e&ldebug &7<&eplayer&7> <&echeck&7> &7>>&e Activates debug mode for user. [DEV]");
            cmdlistMessage.add("&6&l/sg &e&lcooldown &7<&etime in &6ms&7> &7>>&eChanges the cooldown between alerts per user ");
            cmdlistMessage.add("&6&l/sg &e &7>>");
            cmdlistMessage.add("&6&l/sg &e &7>>");
            cmdlistMessage.add("&6&l/alerts &7>> &eToggles alert for user");
            config.set("sg-help", cmdlistMessage);
            saveConfig();
        }

        if (!config.contains("punish-message")) {
            config.set("punish-message", Collections.singletonList("none"));
            saveConfig();
        }
        this.sgHelp = config.getStringList("sg-help");
        this.punishMessage = config.getStringList("punish-message");
        this.banTimerMessage = config.getString("ban-timer-message");
    }

    public void reload() {
        reloadConfig();
        this.config = getConfig();
        genConfig();
        SafeGuard.getInstance().reloadPrefix();
        SafeGuard.getInstance().reloadCooldown();
    }

}
