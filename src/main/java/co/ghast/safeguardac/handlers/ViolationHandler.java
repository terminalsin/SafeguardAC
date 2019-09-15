package co.ghast.safeguardac.handlers;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.checks.Check;
import co.ghast.safeguardac.update.UpdateType;
import co.ghast.safeguardac.update.events.UpdateEvent;
import co.ghast.safeguardac.util.ChatUtil;
import co.ghast.safeguardac.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ViolationHandler implements Listener{

    private SafeGuard sg;
    private Boolean isCooldown = false;
    private FileConfiguration config;

    private long cooldownTimer = 0;
    private HashMap<Player, String> checkAlert = new HashMap<>();
    private Map<UUID, Map<Check, Integer>> Violations = new HashMap<>();
    private Map<UUID, Map<Check, Long>> ViolationReset = new HashMap<>();
    private Map<Player, Map.Entry<Check, Long>> AutoBan = new HashMap<>();
    private Map<Player, Map.Entry<Check, Long>> debug = new HashMap<>();
    public ViolationHandler(SafeGuard sg){
        this.sg = sg;
    }


    @EventHandler
    public void update(UpdateEvent event) {
        if (!(event.getType().equals(UpdateType.SEC)))
            return;
        Map<UUID, Map<Check, Long>> diff = new HashMap<>(this.ViolationReset);

        for (UUID uid : diff.keySet()) {
            if (!(this.Violations.containsKey(uid)))
                continue;

            Map<Check, Long> i = new HashMap<>(diff.get(uid));
            for (Check check : i.keySet()) {
                Long sender = i.get(check);

                if (System.currentTimeMillis() >= sender) {
                    this.ViolationReset.get(uid).remove(check);
                    this.Violations.get(uid).remove(check);
                }
            }
        }
        for (UUID uid : diff.keySet()) {
            if (!(this.Violations.containsKey(uid)))
                continue;

            Map<Check, Long> i = new HashMap<>(diff.get(uid));
            for (Check check : i.keySet()) {
                Long sender = i.get(check);

                if (System.currentTimeMillis() >= sender) {
                    this.ViolationReset.get(uid).remove(check);
                    this.Violations.get(uid).remove(check);
                }
            }
        }
    }
    // RIGHT HERE
    private int autobanTime() {
        return sg.getSettingsConfig().getBanTimer();
    }


    // This gets the queue
    public List<Player> getAutobanQueue() {
        return new ArrayList<>(this.AutoBan.keySet());
    }

    public void removeFromAutobanQueue(Player player) {
        this.AutoBan.remove(player);
    }
    public void autobanOver(Player player) {
        Map<Player, Map.Entry<Check, Long>> se = new HashMap<>(this.AutoBan);

        if (se.containsKey(player)) {
            this.banPlayer(player, se.get(player).getKey());
            this.AutoBan.remove(player);
        }
    }

    private void autoban(Check check, final Player player) {
        /*if (JI.getInstance().getLag().getTPS() < 17)
            return;
        if (JI.getInstance().getLag().getPing(player) > 300)
            return;
        */
        // DO IT HERE

        if (check.hasBanTimer()) {



            if (this.AutoBan.containsKey(player))
                return;
            if (this.debug.containsKey(player))
                return;
            this.AutoBan.put(player, new AbstractMap.SimpleEntry<>(check, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(autobanTime())));
            if(sg.getDebugger().isPlayerDebug(player)){
                this.debug.put(player, new AbstractMap.SimpleEntry<>(check, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(autobanTime())));
            }


            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission(StringUtil.STAFF_PERM))
                    pl.sendMessage(StringUtil.color(SafeGuard.getInstance().getPrefix() + SafeGuard.getInstance().getMessagesConfig().getBanTimerMessage().replace("%check%", check.getName()).replace("%player%", player.getName())));
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    AutoBan.remove(player);
                    if (sg.getDebugger().isPlayerDebug(player)) {
                        debug.remove(player);
                        player.sendMessage(ChatUtil.colorize("&cYou would of been banned for &7" + check.getName()));
                        return;
                    }
                    banPlayer(player, check);
                }
            }.runTaskLater(sg, autobanTime() * 20);


            if (!sg.getSettingsConfig().getConfig().getBoolean("log-bans"))
                return;

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Map<Check, Integer> se = getViolations(player);
                        List<String> string = new ArrayList<>();
                        for (Check check : se.keySet()) {
                            Integer sender = se.get(check);
                            string.add(check.getName() + " VL" + sender);
                        }
                        sg.getLogConfig().logBan(player, string);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(sg);

        } else {
            this.banPlayer(player, check);
        }
    }

    public void banPlayer(final Player player, final Check check) {

        //player.setWalkSpeed(0.1F);
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 3.0F, 1F);
        Bukkit.broadcastMessage(ChatColor.DARK_AQUA + player.getName() + " got caught using this hack: "
                + check.getName() + ". Therefor, he gets banned! :O");
        player.getWorld().strikeLightningEffect(player.getLocation());
        sg.namesBanned.put(player.getName(), check);
        sg.getSettingsConfig().multipleCommands(player, check);
        this.Violations.remove(player.getUniqueId());



        //sg.getInstance().getLangConfig().multipleCommands(player, check);
    }

    //////////////////////////////////////////////////////////////////
    //                      VIOLATION SYSTEM                        //
    //////////////////////////////////////////////////////////////////
    public void removeViolations(Player player) {
        this.Violations.remove(player.getUniqueId());
    }

    private Integer getViolations(Player player, Check check) {
        if (this.Violations.containsKey(player.getUniqueId()))
            return this.Violations.get(player.getUniqueId()).get(check);

        return 0;
    }

    public Map<Check, Integer> getViolations(Player player) {
        if (this.Violations.containsKey(player.getUniqueId()))
            return new HashMap<>(this.Violations.get(player.getUniqueId()));

        return null;
    }

    private void addViolation(Player player, Check check) {
        Map<Check, Integer> other = new HashMap<>();

        if (this.Violations.containsKey(player.getUniqueId()))
            other = this.Violations.get(player.getUniqueId());

        if (!(other.containsKey(check)))
            other.put(check, 1);
        else
            other.put(check, other.get(check) + 1);

        this.Violations.put(player.getUniqueId(), other);
    }

    public void takeViolation(Player player, Check check) {
        Map<Check, Integer> collectionType = new HashMap<>();

        if (this.Violations.containsKey(player.getUniqueId()))
            collectionType = this.Violations.get(player.getUniqueId());

        if (!(collectionType.containsKey(check)))
            collectionType.put(check, 0);
        else
            collectionType.put(check, collectionType.get(check) - 1);

        this.Violations.put(player.getUniqueId(), collectionType);
    }

    public void removeViolations(Player player, Check check) {
        if (this.Violations.containsKey(player.getUniqueId())) {
            this.Violations.get(player.getUniqueId()).remove(check);
        }
    }

    private void setViolationResetTime(Player player, Check check, long time) {
        Map<Check, Long> e = new HashMap<>();

        if (this.ViolationReset.containsKey(player.getUniqueId()))
            e = this.ViolationReset.get(player.getUniqueId());

        e.put(check, time);

        this.ViolationReset.put(player.getUniqueId(), e);
    }

    public void logDebug(Check check, Player player, String value) {
        player.sendMessage(ChatUtil.colorize("&8[&cDEBUG&8] &a"+ check.getName() + ": &c" + value));

    }

    public void logCheat(Check check, Player player, String... identifiers) {

        if (this.AutoBan.containsKey(player)){
            return;
        }
        this.addViolation(player, check);

        Integer sender = this.getViolations(player, check);
        StringBuilder result = new StringBuilder();

        if (identifiers.length > 0) {
            result.append("&7(");

            for (String indentifier : identifiers) {
                result.append(indentifier).append(", ");
            }

            result.setLength(result.length() - 2);

            result.append("&7)");
        } else {
            result = null;
        }


        if ((sender % check.getViolationsToNotify()) == 0) {


            String message = sg.getPrefix() + ChatColor.translateAlternateColorCodes
                    ('&', "&c%player% &7failed &7[&c%check%&7] [&bVL%vl%&7] %tags%");
            message = message.replace("%player%", player.getName());
            message = message.replace("%check%", check.getName());
            message = message.replace("%vl%", String.valueOf(sender));



            for (Player all : sg.getAlertHandler().getAlertable()) {

                if (result != null) {
                    message = message.replace("%tags%", result.toString());
                } else {
                    message = message.replace("%tags%", "");
                }


                if (!(cooldownTimer > System.currentTimeMillis())) {
                    checkAlert.put(player, check.getName());
                    all.sendMessage(StringUtil.color(message));
                    this.cooldownTimer = System.currentTimeMillis() + sg.getCooldown();
                }
            }

        }

        this.setViolationResetTime(player, check, System.currentTimeMillis() + check.getViolationResetTime());
        if (sender > check.getMaxViolations() && check.isBannable()) {
            autoban(check, player);

            for (String punishMessage : SafeGuard.getInstance().getMessagesConfig().getPunishMessage()) {
                if (punishMessage.equals("none")) {
                    return;
                }
                punishMessage = punishMessage.replace("%player%", player.getName());
                punishMessage = punishMessage.replace("%check%", check.getName());
                Bukkit.broadcastMessage(StringUtil.color(punishMessage));
            }
        }

    }

    public void logTabComplete(Check check, Player player, String message, String... identifiers) {
        Integer sender = this.getViolations(player, check);
        StringBuilder result = new StringBuilder();

        if (identifiers.length > 0) {
            result.append("&7(");

            for (String indentifier : identifiers) {
                result.append(indentifier).append(", ");
            }

            result.setLength(result.length() - 2);

            result.append("&7)");
        } else {
            result = null;
        }



            String msg = sg.getPrefix() + ChatColor.translateAlternateColorCodes
                    ('&', "&c%player% &7may be using &7[&c%check%&7] [&bVL%vl%&7] %tags%");
            msg = msg.replace("%player%", player.getName());
            msg = msg.replace("%check%", check.getName());
            msg = msg.replace("%vl%", String.valueOf(sender));



            for (Player all : sg.getAlertHandler().getAlertable()) {

                if (result != null) {
                    msg = msg.replace("%tags%", result.toString());
                } else {
                    msg = msg.replace("%tags%", "");
                }


                if (!(cooldownTimer > System.currentTimeMillis())) {
                    checkAlert.put(player, check.getName());
                    all.sendMessage(StringUtil.color(msg));
                    this.cooldownTimer = System.currentTimeMillis() + sg.getCooldown();
                    all.sendMessage(StringUtil.color("&8[&e" + player.getDisplayName() + "&8] &e" + message));
                }
            }



    }
}
