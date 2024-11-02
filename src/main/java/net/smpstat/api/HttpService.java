package net.smpstat.api;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import net.smpstat.SmpStat;
import org.bukkit.plugin.java.JavaPlugin;

public class HttpService {

    private final SmpStat plugin;

    public HttpService(SmpStat plugin) {
        this.plugin = plugin;
    }

    // General method to send POST requests
    private void sendPostRequest(String endpoint, String jsonInputString) {
        try {
            // URL of the API endpoint
            URL url = new URL(endpoint);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Write the JSON input string to the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                plugin.getLogger().info("Successfully sent data to the API");
            } else {
                plugin.getLogger().warning("Failed to send data to the API. Response code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            plugin.getLogger().severe("Error sending data to the API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void playerJoin(String playerName, String uuid) {
        // JSON body data for the player join event
        String jsonInputString = String.format("{\"player\":\"%s\", \"uuid\":\"%s\", \"serverSecret\":\"%s\"}", playerName, uuid, plugin.getServerSecret());
        sendPostRequest(plugin.getApiUrl() + "/api/playerJoin", jsonInputString);
    }

    public void playerDied(String playerName, String deathMessage, String uuid) {
        // JSON body data for the player death event
        String jsonInputString = String.format("{\"player\":\"%s\", \"deathMsg\":\"%s\", \"uuid\":\"%s\", \"serverSecret\":\"%s\"}", playerName, deathMessage, uuid, plugin
                .getServerSecret());
        sendPostRequest(plugin.getApiUrl() + "/api/playerDeath", jsonInputString);
    }

    public void playerQuit(String playerName, String uuid) {
        // JSON body data for the player quit event
        String jsonInputString = String.format("{\"player\":\"%s\", \"uuid\":\"%s\", \"serverSecret\":\"%s\"}", playerName, uuid, plugin.getServerSecret());
        sendPostRequest(plugin.getApiUrl() + "/api/playerQuit", jsonInputString);
    }

    public void playerAdvancement(String playerName, String advancement, String uuid) {
        // JSON body data for the player advancement event
        String jsonInputString = String.format("{\"player\":\"%s\", \"advancement\":\"%s\", \"uuid\":\"%s\", \"serverSecret\":\"%s\"}", playerName, advancement, uuid, plugin.getServerSecret());
        sendPostRequest(plugin.getApiUrl() + "/api/playerAdvancement", jsonInputString);
    }

}
