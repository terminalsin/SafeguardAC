package co.ghast.safeguardac.commands;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.checks.Check;
import co.ghast.safeguardac.mechanics.DebugSystem;
import co.ghast.safeguardac.util.ChatUtil;
import co.ghast.safeguardac.util.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SafeguardCommand implements CommandExecutor{

    private SafeGuard sg;
    private String debugPrefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "Debug" + ChatColor.GRAY + "] ";
    public SafeguardCommand(SafeGuard sg){
        this.sg = sg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        // /sg violations remove
        // /sg debug <player> <check>
        // /sg status <player>
        // /sg violation clear <player>
        // /sg status <player>
        if (!sender.hasPermission(PermissionUtil.STAFF_PERMISSION)) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player)sender;

            switch (args.length) {
                case 0: {
                    for(String s : sg.getMessagesConfig().getSgHelp()){
                        player.sendMessage(ChatUtil.colorize(s));
                    }
                    break;
                }
                case 1: {
                    if (args[0].equalsIgnoreCase("gui")){
                        player.openInventory(SafeGuard.getInstance().getMainGUI().getGui().getInventory());
                    }
                    break;
                }
                case 2: {
                    if (args[0].equalsIgnoreCase("cooldown")){
                        try {
                            int cooldown = Integer.parseInt(args[1]);
                            sg.getSettingsConfig().setCooldown(cooldown);
                            sg.reloadCooldown();
                            player.sendMessage(sg.getPrefix() + ChatUtil.colorize("&aUpdated cooldown to &e" + cooldown ));

                        } catch (NumberFormatException e){
                            player.sendMessage(sg.getPrefix() + ChatUtil.colorize("&4Invalid cooldown!"));
                        }
                    }
                    if (args[0].equalsIgnoreCase("status")){
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + args[1] + " not found.");
                            return true;
                        }
                        Map<Check, Integer> vlMap = sg.getVlhandler().getViolations(target);
                        if (vlMap == null) {
                            sender.sendMessage(ChatUtil.Gray + "No violations founds.");
                            return true;
                        }
                        List<String> status = new ArrayList<>();
                        for (Map.Entry<Check, Integer> entrySet : vlMap.entrySet()) {
                            status.add(entrySet.getKey().getName() + " VL: " + entrySet.getValue());
                        }
                        sender.sendMessage(ChatUtil.colorize("&aShowing status of player &a" + target.getName()));
                        sender.sendMessage(ChatUtil.Gray + String.join(", ", status));
                    }
                    break;
                }

                case 3: {
                    if (args[0].equalsIgnoreCase("violation")){
                        if (args[1].equalsIgnoreCase("clear")) {
                            Player target = Bukkit.getServer().getPlayer(args[2]);
                            if (target == null) {
                                sender.sendMessage(ChatColor.RED + args[1] + " not found.");
                                return true;
                            }
                            sg.getVlhandler().removeViolations(target);
                            sender.sendMessage(ChatUtil.colorize("&4Cleared violations for user"));
                        }
                    }
                    if (args[0].equalsIgnoreCase("debug")){
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + args[1] + " not found.");
                            return true;
                        }
                        if (sg.getCheckList().contains(args[2])){
                            if (sg.getDebugger().isPlayerDebug(target)) {
                                DebugSystem.removeDebugPlayer(target);
                                player.sendMessage(debugPrefix + ChatColor.RED + "Succesfully disabled debug for: " + args[1]);
                            } else {
                                player.sendMessage(debugPrefix + ChatColor.YELLOW + "Succesfully enabled " + args[1] + "'s debug for: " + args[2]);
                                DebugSystem.addDebugPlayer(target, args[2]);
                            }

                        } else {
                            // Send message that player is invalid
                            player.sendMessage(debugPrefix + ChatUtil.colorize("&4Invalid check!"));
                        }
                    }
                    break;
                }
                default:
                    return false;


            }
        }

        return false;
    }
}
