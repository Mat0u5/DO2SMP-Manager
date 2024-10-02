package net.mat0u5.do2smpmanager.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;

import java.util.Collection;

import static net.mat0u5.do2smpmanager.utils.PermissionManager.isAdmin;
import static net.minecraft.server.command.CommandManager.literal;


public class Command {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {

        dispatcher.register(
            literal("test")
                .requires(source -> (isAdmin(source.getPlayer())))
                .executes(context -> Command.execute(
                    context.getSource())
                )
        );
    }
    public static int execute(ServerCommandSource source) throws CommandSyntaxException {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();

        self.sendMessage(Text.of("Command Successful"));
        return 1;
    }
}
