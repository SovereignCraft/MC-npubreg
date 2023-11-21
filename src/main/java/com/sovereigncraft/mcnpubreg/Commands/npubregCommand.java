package com.sovereigncraft.mcnpubreg.Commands;

import com.google.gson.Gson;
import com.sovereigncraft.mcnpubreg.ConfigHandler;
import com.google.gson.JsonParseException;
import com.sovereigncraft.mcnpubreg.HttpUtil;
import com.sovereigncraft.mcnpubreg.NostrData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.Map;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class npubregCommand implements CommandExecutor {

    private final Gson gson = new Gson();
    private final Logger logger = Logger.getLogger("MC_npubreg");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // Check if the command has the minimum required arguments
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /npubreg <npub>");
            return true;
        }

        // Ensure the command is used by a player and not from the console
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName(); // Get the player's name

        String npub = args[0];
        String url = "https://" + ConfigHandler.getDomain() + "/.well-known/nostr.json";

        // Check if the player has the required permission
        // Permission node for the command
        String permissionNode = "mcnpubreg.use";
        if (!sender.hasPermission(permissionNode)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        // Validate the npub format
        // Prefix for valid npub
        String npubPrefix = "npub";
        if (!npub.startsWith(npubPrefix)) {
            sender.sendMessage(ChatColor.RED + "Invalid npub format. npub should start with 'npub'.");
            return true;
        }

        try {
            // Fetch the current JSON data from the server
            String currentJson = HttpUtil.sendGetRequest(url);

            // Parse the JSON into NostrData object
            NostrData data = gson.fromJson(currentJson, NostrData.class);
            if (data == null) {
                data = new NostrData(); // Create a new instance if null
            }

            // Update the npub with the player's name
            data.addName(playerName, npub);

            // Convert the updated data back to JSON string
            String updatedJson = gson.toJson(data);

            // Send the updated JSON to the server
            String response = HttpUtil.sendPostRequest(url, updatedJson);

            // Parse the server's response
            try {
                Type responseType = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> responseMap = gson.fromJson(response, responseType);

                // Check if the update was successful
                if (responseMap != null && Boolean.TRUE.equals(responseMap.get("success"))) {
                    sender.sendMessage(ChatColor.GREEN + "Npub successfully registered.");
                } else {
                    // Handle unsuccessful update
                    String errorMessage = responseMap != null ? (String) responseMap.get("message") : "Unknown error";
                    sender.sendMessage(ChatColor.RED + "Failed to register npub: " + errorMessage);
                }
            } catch (JsonParseException e) {
                // Handle JSON parsing errors of the response
                sender.sendMessage(ChatColor.RED + "Error parsing server response: " + e.getMessage());
                logger.warning("Response parsing error: " + e.getMessage());
            }

        } catch (IOException e) {
            // Handle IO/network errors
            sender.sendMessage(ChatColor.RED + "Network error occurred: " + e.getMessage());
            logger.warning("Network error: " + e.getMessage());
        } catch (JsonParseException e) {
            // Handle JSON parsing errors
            sender.sendMessage(ChatColor.RED + "Error parsing JSON: " + e.getMessage());
            logger.warning("JSON parsing error: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected errors
            sender.sendMessage(ChatColor.RED + "An unexpected error occurred: " + e.getMessage());
            logger.severe("Unexpected error: " + e.getMessage());
        }

        return true;
    }
}
