// npubregCommand.java
package com.sovereigncraft.mcnpubreg.Commands;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.sovereigncraft.mcnpubreg.HttpUtil;
import com.sovereigncraft.mcnpubreg.NostrData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;  // Import the NotNull annotation
import java.io.IOException;

public class npubregCommand implements CommandExecutor {

    private final Gson gson = new Gson();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /npubreg <npub>");
            return true;
        }

        String npub = args[0];
        String url = "https://sovereigncraft.com/.well-known/nostr.json";

        try {
            // Read the current JSON
            String currentJson = HttpUtil.sendGetRequest(url);

            // Validate and parse the JSON
            NostrData data;
            try {
                data = gson.fromJson(currentJson, NostrData.class);
                if (data == null) {
                    throw new JsonParseException("Received JSON is not valid");
                }
            } catch (JsonParseException e) {
                sender.sendMessage("Error parsing JSON: " + e.getMessage());
                return true;
            }

            // Update the npub
            data.setNpub(npub);

            // Convert back to JSON string
            String updatedJson = gson.toJson(data);

            // Send the updated JSON back
            String response = HttpUtil.sendPostRequest(url, updatedJson);
            sender.sendMessage("npub registered: " + response);
        } catch (IOException e) {
            sender.sendMessage("Error while processing the request: " + e.getMessage());
        }

        return true;
    }
}
