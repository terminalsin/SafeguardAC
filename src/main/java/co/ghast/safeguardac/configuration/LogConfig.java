package co.ghast.safeguardac.configuration;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.util.ConfigMan;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class LogConfig extends ConfigMan {

    public LogConfig() {
        super("bans.yml", SafeGuard.getInstance().getDataFolder());
    }

    public void logBan(Player p, List<String> cl) {
        String checks = StringUtils.join(cl, ", ");
        getConfig().set(p.getName(), checks);
        saveConfig();
    }

}