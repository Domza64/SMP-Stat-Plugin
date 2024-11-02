package net.smpstat.listener;

import net.smpstat.SmpStat;
import net.smpstat.api.HttpService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;

public class SmpStatEventListener implements Listener {

    private final HttpService httpService;

    public SmpStatEventListener(SmpStat plugin) {
        // Initialize HttpService using the main plugin instance
        this.httpService = new HttpService(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        String uuid = event.getPlayer().getUniqueId().toString();
        httpService.playerJoin(playerName, uuid);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        String uuid = event.getPlayer().getUniqueId().toString();
        httpService.playerQuit(playerName, uuid);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getName();
        String uuid = event.getEntity().getUniqueId().toString();
        String deathMessage = event.getDeathMessage();
        httpService.playerDied(playerName, deathMessage, uuid);
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        // Check if the advancement has a display
        if (event.getAdvancement().getDisplay() != null) {
            // Check if the advancement is set to announce to chat
            if (event.getAdvancement().getDisplay().shouldAnnounceChat()) {

                String playerName = event.getPlayer().getName();
                String uuid = event.getPlayer().getUniqueId().toString();

                // Get the display name from the advancement
                String advancementName = event.getAdvancement().getDisplay().getTitle();

                // Send to HTTP service
                httpService.playerAdvancement(playerName, advancementName, uuid);
            }
        }
    }

    //    @EventHandler
//    public void onPlayerKillDog(EntityDeathEvent event) {
//        // If entity that died is dog or a cat and a player is the killer add that players name to deathlist or something like that so that everyone knows what that player did.
//        String playerName = event.getDamageSource().getCausingEntity().getName();
//    }
}
