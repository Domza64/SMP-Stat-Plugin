package net.smpstat;

import net.smpstat.listener.SmpStatEventListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmpStat extends JavaPlugin implements Listener {

    private String serverSecret; // This will hold the secret string

    @Override
    public void onEnable() {
        // Load the configuration file
        saveDefaultConfig(); // Creates the config file if it doesn't exist

        // Attempt to load the secret value from the config
        serverSecret = getConfig().getString("serverSecret", null); // Get the value or null if not defined

        if (serverSecret == null) {
            // Display warning in console if the value is not defined
            getLogger().warning("Server Secret is not set! Please use /setsecret <value> to define it.");
        }

        // Register the SmpStatListener
        getServer().getPluginManager().registerEvents(new SmpStatEventListener(this, serverSecret), this);
    }

    @Override
    public void onDisable() {
        // Send API request that server is down, it should make all online players offline and calculate their playtime

        // Save the value to config when the plugin is disabled
        if (serverSecret != null) {
            getConfig().set("serverSecret", serverSecret); // Save the current value to config
        }
        saveConfig(); // Ensure the config is saved
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setsecret")) {
            if (sender.hasPermission("smpstat.admin")) { // Check if the sender has admin permission
                if (args.length == 1) {
                    serverSecret = args[0]; // Update the secret value
                    sender.sendMessage("Secret value set to: " + serverSecret);
                    getConfig().set("serverSecret", serverSecret); // Save to config
                    saveConfig(); // Ensure the config is saved
                } else {
                    sender.sendMessage("Usage: /setsecret <value>");
                }
            } else {
                sender.sendMessage("You do not have permission to use this command.");
            }
            return true;
        }
        return false;
    }

    public String getSecretValue() {
        return serverSecret; // Provide access to the secret value
    }
}
