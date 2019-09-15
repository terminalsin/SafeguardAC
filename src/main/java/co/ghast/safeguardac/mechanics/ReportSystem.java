package co.ghast.safeguardac.mechanics;

import co.ghast.safeguardac.util.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;

public class ReportSystem implements Listener {

    public static Map<Integer, Player> reports_reporter = new HashMap<>();
    public static Map<Integer, Player> reports_suspect = new HashMap<>();
    public static Map<Integer, String> reports_reason = new HashMap<>();
    public static int report_id = reports_reporter.size() + 1;

    public static void Report(Player reporter, Player target, String reason) {

        /*
        Let's start by saving the report for futur requests
         */
        reports_reporter.put(report_id, reporter);
        reports_suspect.put(report_id, target);
        reports_reason.put(report_id, reason);
        /*
        Here we will send to staff member the report.
         */
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(PermissionUtil.REPORT_SEE_PERMISSION)) {
                p.sendMessage(ChatColor.YELLOW + "New report from " + reporter.getName() +"!");
                p.sendMessage(ChatColor.YELLOW + "Suspect: " + ChatColor.AQUA + target.getName());
                p.sendMessage(ChatColor.YELLOW + "Reason: " + ChatColor.AQUA + reason);
            }
        }

    }

    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission(PermissionUtil.REPORT_SEE_PERMISSION)) {
            p.sendMessage(ChatColor.RED + "You've missed " + ChatColor.DARK_RED + report_id + ChatColor.RED + "! You may review them by running /reports list!");
        }
    }

    public static void shutdown() {
        reports_reason.clear();
        reports_suspect.clear();
        reports_reporter.clear();
    }

}
