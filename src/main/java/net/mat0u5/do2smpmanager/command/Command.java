package net.mat0u5.do2smpmanager.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mat0u5.do2smpmanager.utils.ItemManager;
import net.mat0u5.do2smpmanager.utils.OtherUtils;
import net.mat0u5.do2smpmanager.world.BlockScanner;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.mat0u5.do2smpmanager.utils.PermissionManager.isAdmin;
import static net.minecraft.server.command.CommandManager.*;


public class Command {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {

        dispatcher.register(
            literal("playerlist")
                .executes(context -> Command.playerList(
                    context.getSource())
                )
        );
        dispatcher.register(
            literal("blocklock")
                .requires(source -> (isAdmin(source.getPlayer())))
                .then(argument("lock", StringArgumentType.string())
                    .suggests((context, builder) -> CommandSource.suggestMatching(List.of("unlock"), builder))
                    .then(argument("fromPos", BlockPosArgumentType.blockPos()) // Suggests the block you're looking at
                        .then(argument("toPos", BlockPosArgumentType.blockPos()) // Suggests the block you're looking at
                            .executes(context -> {
                                BlockPos fromPos = BlockPosArgumentType.getBlockPos(context, "fromPos");
                                BlockPos toPos = BlockPosArgumentType.getBlockPos(context, "toPos");
                                return Command.executeLock(
                                    context.getSource(),
                                    fromPos.getX(),
                                    fromPos.getY(),
                                    fromPos.getZ(),
                                    toPos.getX(),
                                    toPos.getY(),
                                    toPos.getZ(),
                                    StringArgumentType.getString(context, "lock")
                                );
                            })
                        )
                        .executes(context -> {
                            BlockPos fromPos = BlockPosArgumentType.getBlockPos(context, "fromPos");
                            return Command.executeLock(
                                context.getSource(),
                                fromPos.getX(),
                                fromPos.getY(),
                                fromPos.getZ(),
                                fromPos.getX(),
                                fromPos.getY(),
                                fromPos.getZ(),
                                StringArgumentType.getString(context, "lock")
                            );
                        })
                    )
                )
        );
        dispatcher.register(
            literal("casino")
                .requires(source -> (isAdmin(source.getPlayer()) || source.getPlayer().getUuidAsString().equalsIgnoreCase("3a41ea41-6876-47ea-8cf5-89ef8b375011")))
                .then(literal("makeItem")
                    .executes(context -> Command.makeCasino(
                        context.getSource())
                    )
                )
        );
        dispatcher.register(
            literal("whattimeisitfor")
                .then(argument("target", EntityArgumentType.player())
                    .executes(context -> Command.whatTimeIsItFor(
                        context.getSource(), EntityArgumentType.getPlayer(context,"target"))
                    )
                )
        );
    }
    public static int makeCasino(ServerCommandSource source) {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();
        if (self == null) return -1;
       ItemManager.setComponentInt(self.getStackInHand(Hand.MAIN_HAND), "CasinoItem", 1);
        return 1;
    }
    public static int executeLock(ServerCommandSource source, int fromX, int fromY, int fromZ, int toX, int toY, int toZ, String lock) {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();
        if (self == null) return -1;
        self.sendMessage(Text.of("Started Block Lock Search..."), false);
        new BlockScanner().scanArea(lock, (ServerWorld) self.getWorld(),new BlockPos(fromX, fromY, fromZ),new BlockPos(toX, toY, toZ), source.getPlayer());
        return 1;
    }
    public static int playerList(ServerCommandSource source) {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();

        int playerCount = 0;
        MutableText message = Text.translatable("There are "+server.getPlayerManager().getPlayerList().size()+" players online: ");
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            playerCount++;
            message = message.append(player.getDisplayName());
            if (playerCount != server.getPlayerManager().getPlayerList().size()) {
                message = message.append(", ");
            }
        }
        if (self != null) {
            self.sendMessage(message, false);
        }
        else {
            System.out.println(message.getString());
        }
        return 1;
    }
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    public static int whatTimeIsItFor(ServerCommandSource source, ServerPlayerEntity target) {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();
        if (target == null) {
            source.sendError(Text.of("Invalid Target."));
            return -1;
        }
        executor.submit(() -> {
            String playerIp = OtherUtils.getPlayerIPAddress(target);
            String timezoneId = OtherUtils.getTimezoneFromIP(playerIp);
            String timezone = OtherUtils.formatTimezoneToUTC(timezoneId);
            String time = OtherUtils.getCurrentTimeInTimezone(timezoneId);
            String message = "It's "+time+" ("+timezone+") for "+target.getNameForScoreboard()+".";
            source.sendMessage(Text.of(message));
        });
        return 1;
    }
}
