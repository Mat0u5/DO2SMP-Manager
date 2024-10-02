package net.mat0u5.do2smpmanager.events;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.mat0u5.do2smpmanager.Main;
import net.mat0u5.do2smpmanager.utils.OtherUtils;
import net.mat0u5.do2smpmanager.utils.PermissionManager;
import net.minecraft.block.Block;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Events {

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(Events::onServerStart);
        ServerLifecycleEvents.SERVER_STOPPING.register(Events::onServerStopping);
        UseBlockCallback.EVENT.register(Events::onBlockUse);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> onPlayerJoin(server, handler.getPlayer()));
    }

    private static void onPlayerJoin(MinecraftServer server, ServerPlayerEntity player) {
    }
    private static void onServerStopping(MinecraftServer server) {
    }
    private static void onServerStart(MinecraftServer server) {
        Main.server = server;
        System.out.println("MinecraftServer instance captured.");
    }
    public static ActionResult onBlockUse(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        Block block = world.getBlockState(pos).getBlock();
        if (block == null) return ActionResult.PASS;
        if (!(world.getBlockEntity(pos) instanceof LockableContainerBlockEntity)) return ActionResult.PASS;
        LockableContainerBlockEntity container = (LockableContainerBlockEntity) world.getBlockEntity(pos);
        if (container == null) return ActionResult.PASS;

        String lock = OtherUtils.getLock(container);
        if (lock == null || lock.isEmpty()) return ActionResult.PASS;
        if (PermissionManager.isAdmin(player)) {
            OtherUtils.unlockContainerForTick((ServerWorld) world, player.getServer(), container,pos);
            player.playSoundToPlayer(SoundEvents.BLOCK_AMETHYST_BLOCK_STEP, SoundCategory.PLAYERS, 0.7f, 1.0f);
            return ActionResult.PASS;
        }

        ItemStack handItem = player.getStackInHand(hand);
        if (handItem.getName().toString().isEmpty()) return ActionResult.PASS;
        if (!lock.contains(handItem.getName().getString())) return ActionResult.PASS;
        return ActionResult.PASS;
    }
}
