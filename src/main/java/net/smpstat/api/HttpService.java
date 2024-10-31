package net.smpstat.api;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.bukkit.plugin.java.JavaPlugin;

public class HttpService {

    private final JavaPlugin plugin;

    public HttpService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // General method to send POST requests
    private void sendPostRequest(String endpoint, String jsonInputString) {
        // TODO - Include server secret that is generated and required by SMP Stat website;
        //  Secret should be stored somewhere in config file in mc servers folder
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
                plugin.getLogger().info("Successfully sent data to the API for payload: " + jsonInputString);
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

    public void playerJoin(String playerName, String serverSecret) {
        // JSON body data for the player join event
        String jsonInputString = String.format("{\"player\":\"%s\", \"serverSecret\":\"%s\"}", playerName, serverSecret);
        sendPostRequest("http://localhost:3000/api/playerJoin", jsonInputString);
    }

    public void playerDied(String playerName, String deathMessage, String serverSecret) {
        // JSON body data for the player death event
        String jsonInputString = String.format("{\"player\":\"%s\", \"deathMsg\":\"%s\", \"serverSecret\":\"%s\"}", playerName, deathMessage, serverSecret);
        sendPostRequest("http://localhost:3000/api/playerDeath", jsonInputString);
    }
}
