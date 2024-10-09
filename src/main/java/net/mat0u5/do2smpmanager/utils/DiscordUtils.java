package net.mat0u5.do2smpmanager.utils;

import com.google.gson.JsonObject;
import net.mat0u5.do2smpmanager.Main;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscordUtils {
    public static JsonObject getDefaultJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("username", "AggroNet");
        json.addProperty("avatar_url", "https://cdn.discordapp.com/avatars/1239613119022633010/60a2103d8bf807888b715ec987db4dc2?size=1024");
        return json;
    }
    public static void sendMessageToDiscordFromAggroNet(JsonObject json, String channelId) {
        try {
            // Discord API URL for sending messages
            String apiUrl = "https://discord.com/api/v10/channels/" + channelId + "/messages";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            // Set authorization header with the bot token
            connection.setRequestProperty("Authorization", "Bot " + getWebhookToken());
            connection.setRequestProperty("Content-Type", "application/json");

            // Send the JSON payload
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response
            int responseCode = connection.getResponseCode();
            if (responseCode != 200 && responseCode != 201) {
                throw new RuntimeException("Failed to send message to Discord: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateDiscordChannelDescription() {
        List<ServerPlayerEntity> players = Main.server.getPlayerManager().getPlayerList();
        List<String> playerNames = new ArrayList<>();
        for (ServerPlayerEntity player : players) {
            playerNames.add(player.getNameForScoreboard());
        }
        if (playerNames.contains("TangoCam")) playerNames.remove("TangoCam");
        String description = "Players online (" + playerNames.size() + "): " + String.join(", ",playerNames);
        DiscordBot discordBot = new DiscordBot();
        discordBot.startBot(getWebhookToken(), getChatChannelId(),true,description);
    }
    public static String getWebhookToken() {
        return Main.config.getProperty("webhook_token");
    }
    public static String getChatChannelId() {
        return Main.config.getProperty("server_chat_channel_id");
    }
}
