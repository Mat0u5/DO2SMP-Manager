package net.mat0u5.do2smpmanager.events;



import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Events {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> onPlayerJoin(server, handler.getPlayer()));
    }

    private static void onPlayerJoin(MinecraftServer server, ServerPlayerEntity player) {
    }
}
