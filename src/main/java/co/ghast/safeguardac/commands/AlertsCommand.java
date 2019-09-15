package co.ghast.safeguardac.commands;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import co.ghast.safeguardac.util.StringUtil;

public class AlertsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player to run this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(StringUtil.STAFF_PERM) && !sender.getName().equals("ItemMeta")) {
            sender.sendMessage(ChatUtil.Red + "No permission.");
            return true;
        }

        String toggleMessage = !SafeGuard.getInstance().getAlertHandler().hasAlertsOn(player) ? "&aON" : "&cOFF";
        player.sendMessage(SafeGuard.getInstance().getPrefix() + ChatUtil.colorize("&7Alerts toggled " + toggleMessage));

        SafeGuard.getInstance().getAlertHandler().toggleAlerts(player);
        return true;
    }

}