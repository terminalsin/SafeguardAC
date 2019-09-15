package co.ghast.safeguardac;

import cc.funkemunky.api.Atlas;
import co.ghast.safeguardac.checks.Check;
import co.ghast.safeguardac.checks.SetChecks;
import co.ghast.safeguardac.checks.combat.killaura.KillAuraD;
import co.ghast.safeguardac.checks.combat.killaura.KillAuraE;
import co.ghast.safeguardac.checks.combat.reach.Reach;
import co.ghast.safeguardac.checks.movement.fly.Fly;
import co.ghast.safeguardac.checks.others.miscellaneous.TabComplete;
import co.ghast.safeguardac.commands.AlertsCommand;
import co.ghast.safeguardac.commands.ReportCommand;
import co.ghast.safeguardac.commands.SafeguardCommand;
import co.ghast.safeguardac.commands.gui.MainGUI;
import co.ghast.safeguardac.configuration.LogConfig;
import co.ghast.safeguardac.configuration.MessagesConfig;
import co.ghast.safeguardac.configuration.SettingsConfig;
import co.ghast.safeguardac.data.DataManager;
import co.ghast.safeguardac.data.LoggedPlayer;
import co.ghast.safeguardac.handlers.AlertHandler;
import co.ghast.safeguardac.handlers.ViolationHandler;
import co.ghast.safeguardac.mechanics.DebugSystem;
import co.ghast.safeguardac.mechanics.ReportSystem;
import co.ghast.safeguardac.packets.PacketCore;
import co.ghast.safeguardac.update.Updater;
import co.ghast.safeguardac.util.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// CAN YOU SEE THIS? I'm pretty sure you can normally. If so, type in here



public class SafeGuard extends JavaPlugin implements Listener{
    // INITIALIZE VARIABLES
    @Getter public static SafeGuard instance;
    @Getter private Updater updater;
    @Getter MainGUI mainGUI;
    @Getter private Config mainConfig;
    @Getter private SettingsConfig settingsConfig;
    @Getter private LogConfig logConfig;
    @Getter private MessagesConfig messagesConfig;
    @Getter private PacketCore packetCore;
    @Getter private VersionUtil versionUtil;
    @Getter DebugSystem debugger;
    @Getter BoundingBox boundingBox;
    @Getter @Setter private boolean Debug = false;
    @Getter private ViolationHandler vlhandler;
    @Getter private AlertHandler alertHandler;
    @Getter private int cooldown;
    @Getter private String prefix;
    @Getter public String serverVersion;
    @Getter private DataManager dataManager;
    @Getter LoggedPlayer loggedPlayer;
    // File config


    // INITIALIZE CHECK ARRAY LIST
    @Getter public Map<String, Check> namesBanned = new HashMap<>();
    @Getter public List<Check> checks = new ArrayList<>();
    @Getter public List<String> checkList = new ArrayList<>();

    public void onEnable(){

        this.checks.clear();
        instance = this; // Register the instance
        this.serverVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        this.getConfig().options().copyDefaults(true);
        saveConfig();
        new SizesUtil();
        new ReflectionsUtil();
        this.dataManager = new DataManager();
        this.versionUtil = new VersionUtil(this);
        this.mainConfig = new Config(this, "", "config");
        this.settingsConfig = new SettingsConfig();
        this.logConfig = new LogConfig();
        this.messagesConfig = new MessagesConfig(this);
        reloadPrefix();
        reloadCooldown();

        this.vlhandler = new ViolationHandler(this);
        this.packetCore = new PacketCore(this);
        // #######################
        // --> REGISTER CHECKS
        // #######################
        /* Example:
        this.checks.add(new Reach(this));
         */ // REACH IS BROKEN

        if (getServer().getPluginManager().getPlugin("Citizens")!=null) {
            this.checks.add(new KillAuraE(this));
        } else {
            System.out.println("[SafeGuard] Disabled KillAura Entity Check");
        }
        this.checks.add(new Fly(this));

        this.checks.add(new KillAuraD(this));
        this.checks.add(new TabComplete(this));
        // registers the listener for the checks //
        for (Check check : this.checks) {
            if (check.isEnabled() && (!check.isAtlas())) {
                this.registerListener(check);
            }
            checkList.add(check.getIdentifier());
        }
        this.checks.add(new Reach(this));

        Atlas.getInstance().getEventManager().registerListeners(new Reach(this), this);

        SetChecks.saveChecks();
        SetChecks.syncChecks();


        // #######################
        // --> REGISTER LISTENERS
        // #######################

        this.alertHandler = new AlertHandler();
        this.registerListener(this);
        registerListener(new ReportSystem());
        /*registerListener(new AlertHandler());
        registerListener(new ViolationHandler(this));*///Hey Ghast, this is a way to go aswell :P xD
        // nice job!

        // #######################
        // --> REGISTER MANAGERS
        // #######################
        this.updater = new Updater(this);

        this.cooldown = settingsConfig.getCooldown();
        this.prefix = ChatColor.translateAlternateColorCodes('&', settingsConfig.getPrefix());

        PluginManager count = Bukkit.getPluginManager();
        count.registerEvents(this.alertHandler, this);

        // REGISTER MAIN GUI



        // #######################
        // --> REGISTER COMMANDS
        // #######################
        this.getCommand("safeguard").setExecutor(new SafeguardCommand(this));
        this.getCommand("report").setExecutor(new ReportCommand());
        this.getCommand("alerts").setExecutor(new AlertsCommand());
        SetChecks.reloadFromConfig();
        this.mainGUI = new MainGUI(this);

        // Registering systems/mechanics here
        this.debugger = new DebugSystem(this);



        // #######################
        // --> LICENSE UTIL
        // #######################
        if (!new License(License.getConfig.getString("license"), License.getConfig.getString("mode"), License.getConfig.getInt("activation_id"), this).checkLicense())
            return;

    }
    @Override
    public void onDisable() {
        if (this.updater != null) {
            this.updater.Disable();
        }
        ReportSystem.shutdown();
        saveConfig();
    }

    public void alert(String message) {
        for (Player playerplayer : alertHandler.getAlertable()) {
            playerplayer.sendMessage(prefix + message);
        }
    }

    public void registerListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public void reloadPrefix() {
        prefix = ChatUtil.colorize(settingsConfig.getPrefix());
    }
    public void reloadCooldown() {
        cooldown = settingsConfig.getCooldown();
    }

}
