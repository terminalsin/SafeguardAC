package co.ghast.safeguardac.checks;

import co.ghast.safeguardac.SafeGuard;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SetChecks {

    private static String max = ".max-vl";
    private static String autoban = ".autoban";
    private static String enabled = ".enabled";

    //TRIGGERED

    public static void syncChecks() {
        for (Check check : SafeGuard.getInstance().getChecks()) {
            check.setEnabled(SafeGuard.getInstance().getConfig().getBoolean(check.getName() + enabled));
            check.setMaxViolations(SafeGuard.getInstance().getConfig().getInt(check.getName() + max));
            check.setBannable(SafeGuard.getInstance().getConfig().getBoolean(check.getName() + autoban));
            check.setAutobanTimer(SafeGuard.getInstance().getConfig().getBoolean(check.getName() + autoban));
        }
        System.out.println("SafeGuard | All checks synced.");
    }

    public static void saveChecks() {
        for (Check check : SafeGuard.getInstance().getChecks()) {
            if (!SafeGuard.getInstance().getConfig().contains(check.getName() + enabled)) {
                SafeGuard.getInstance().getConfig().set(check.getName() + max, check.getMaxViolations());
                SafeGuard.getInstance().getConfig().set(check.getName() + autoban, check.isBannable());
                SafeGuard.getInstance().getConfig().set(check.getName() + enabled, check.isEnabled());

            }
        }
        SafeGuard.getInstance().saveConfig();
        System.out.println("JI | Checks saved!");
    }

    public static void saveCheck(Check check) {
        SafeGuard.getInstance().getConfig().set(check.getName() + max, check.getMaxViolations());
        SafeGuard.getInstance().getConfig().set(check.getName() + autoban, check.isBannable());
        SafeGuard.getInstance().getConfig().set(check.getName() + enabled, check.isEnabled());
        SafeGuard.getInstance().saveConfig();
        System.out.println("JI | " + check.getName() + " saved!");
    }

    public static void reloadFromConfig() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(SafeGuard.getInstance().getDataFolder(),
                "config.yml"));
        for (Check check : SafeGuard.getInstance().getChecks()) {
            check.setEnabled(config.getBoolean(check.getName() + enabled));
            check.setMaxViolations(config.getInt(check.getName() + max));
            check.setBannable(config.getBoolean(check.getName() + autoban));
            check.setAutobanTimer(config.getBoolean(check.getName() + autoban));
        }
        SafeGuard.getInstance().getMainConfig().reload();
    }
}
