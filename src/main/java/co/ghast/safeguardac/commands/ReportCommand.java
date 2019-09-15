package co.ghast.safeguardac.commands;

import co.ghast.safeguardac.mechanics.ReportSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    // DOCUMENTATION
    /*
    To report a user: /report <player [args 0]> <string [args 1]>


    */


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        Player t = Bukkit.getPlayer(args[0]);
        switch (args.length){
            case 0:
            case 1: {
                p.sendMessage(ChatColor.RED + "Invalid usage! Please use /report <user> <reason>");
                break;
            }
            case 2: {
                if (t != null) {
                    String message = " ";

                    for(int i = 1; i < args.length; i++)
                        message += " " + args[i];
                    ReportSystem.Report(p, t, message);
                } else {
                    p.sendMessage(ChatColor.RED + "The player you are reporting does not exist! Please check if you misspelled his name!");
                }
            }

        }
        return false;
    }

}
