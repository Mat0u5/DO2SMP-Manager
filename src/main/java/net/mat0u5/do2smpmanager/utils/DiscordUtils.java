package net.mat0u5.do2smpmanager.utils;

import net.mat0u5.do2smpmanager.Main;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class DiscordUtils {
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
