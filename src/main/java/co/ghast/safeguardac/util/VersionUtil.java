package co.ghast.safeguardac.util;

import co.ghast.safeguardac.SafeGuard;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.bukkit.Bukkit;

public class VersionUtil {
    private String version;
    private SafeGuard sg;
    public VersionUtil(SafeGuard sg) {
        this.sg = sg;

    }
    @Getter public String bukkitVersion(){
        return Bukkit.getBukkitVersion();
    }
}
