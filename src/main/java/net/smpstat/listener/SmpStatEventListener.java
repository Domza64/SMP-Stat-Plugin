package net.smpstat.listener;

import net.smpstat.api.HttpService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SmpStatEventListener implements Listener {

    private final HttpService httpService;
    private final String serverSecret;

    public SmpStatEventListener(JavaPlugin plugin, String serverSecret) {
        // Initialize HttpService using the main plugin instance
        this.httpService = new HttpService(plugin);
        this.serverSecret = serverSecret;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        // Log and send HTTP request on player join
        httpService.playerJoin(playerName, serverSecret);
    }

//    @EventHandler
//    public void onPlayerKillDog(EntityDeathEvent event) {
//        // If entity that died is dog or a cat and a player is the killer add that players name to deathlist or something like that so that everyone knows what that player did.
//        String playerName = event.getDamageSource().getCausingEntity().getName();
//    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getName();
        String deathMessage = event.getDeathMessage();
        httpService.playerDied(playerName, deathMessage, serverSecret);
    }
}
