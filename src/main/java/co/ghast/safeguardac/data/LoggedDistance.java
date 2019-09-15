package co.ghast.safeguardac.data;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LoggedDistance {
    @Getter
    private static List<LoggedDistance> loggedDistances = new ArrayList<>();
    @Getter private Player player;
    @Getter private Float distance;
    @Getter private Long time;
    public LoggedDistance(Player player, Long time, Float distance){
        this.player = player;
        this.distance = distance;
        this.time = time;
        loggedDistances.add(this);
    }

    public void delete(){
        loggedDistances.remove(this);
    }

    public static void addPlayer(Player player, Long time, Float distance){
        new LoggedDistance(player, time, distance);
    }

    public static LoggedDistance getByPlayerName(Player player){
        for(LoggedDistance players : LoggedDistance.loggedDistances){
            if(players.getPlayer().equals(player)){
                return players;
            }
        }
        return null;
    }
    public static Float getDistance(Player player, Long time){
        for(LoggedDistance players : LoggedDistance.loggedDistances){
            if(players.getPlayer().equals(player) && players.getTime().equals(time)){
                return players.getDistance();
            }
        }
        return null;
    }



}
