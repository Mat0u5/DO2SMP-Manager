package net.mat0u5.do2smpmanager.world;

import net.mat0u5.do2smpmanager.Main;
import net.mat0u5.do2smpmanager.utils.MSPTUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.server.world.ServerChunkManager;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlockScanner extends MSPTUtils {
    List<Integer> percentCompleted = new ArrayList<>();
    String blockPassword = "";
    int lockOrUnlock=0;
    int listPos=0;

    RegistryWrapper.WrapperLookup registryLookup;
    Integer positionsToCheckInt;
    ServerWorld world = null;
    PlayerEntity player = null;
    ServerChunkManager chunkManager;
    final Set<Block> lockableBlocks = Set.of(
            Blocks.CHEST, Blocks.HOPPER, Blocks.TRAPPED_CHEST,
            Blocks.DISPENSER, Blocks.DROPPER, Blocks.FURNACE,
            Blocks.BARREL, Blocks.SMOKER, Blocks.BLAST_FURNACE
    );
    Integer minX;
    Integer minY;
    Integer minZ;
    Integer maxX;
    Integer maxY;
    Integer maxZ;


    public void scanArea(String lock, ServerWorld world, BlockPos startPos, BlockPos endPos, PlayerEntity player) {
        this.world = world;
        registryLookup = ((ServerWorld) world).getRegistryManager();
        this.player = player;
        listPos = 0;
        percentCompleted = new ArrayList<>();
        this.blockPassword = lock;
        lockOrUnlock = 0;

        minX = Math.min(startPos.getX(), endPos.getX());
        maxX = Math.max(startPos.getX(), endPos.getX());
        minY = Math.min(startPos.getY(), endPos.getY());
        maxY = Math.max(startPos.getY(), endPos.getY());
        minZ = Math.min(startPos.getZ(), endPos.getZ());
        maxZ = Math.max(startPos.getZ(), endPos.getZ());

        positionsToCheckInt = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
        chunkManager = world.getChunkManager();

        startBoosted(Main.server);
    }

    @Override
    protected void complexFunction() {
        if (!running) {
            return;
        }
        if (listPos >= positionsToCheckInt) {
            System.out.println("Stopping...");
            stop();
        }
        else {
            int batchSize = 10_000;
            int batchEndPos = Math.min(listPos + batchSize, positionsToCheckInt);
            for (int i = listPos; i < batchEndPos; i++) {
                processPosition(i);
            }

            listPos = batchEndPos;

            int div = (positionsToCheckInt / 100);
            if ((listPos % (positionsToCheckInt / 100.0))%25 == 0 && listPos != 0 && div != 0) {
                int percent = listPos / div;
                if (!percentCompleted.contains(percent) ) {
                    percentCompleted.add(percent);
                    player.sendMessage(Text.of("[Block Database Searcher] Processed " + percent + "% of positions."), false);
                    player.sendMessage(Text.of("-Modified " + lockOrUnlock + " blocks."), false);
                    System.out.println("[Block Database Searcher] Processed " + percent + "% of positions.");
                }
            }
        }
    }
    private void processPosition(int posIndex) {
        int x = minX + (posIndex % (maxX - minX + 1));
        int y = minY + ((posIndex / (maxX - minX + 1)) % (maxY - minY + 1));
        int z = minZ + (posIndex / ((maxX - minX + 1) * (maxY - minY + 1)));
        BlockPos pos = new BlockPos(x, y, z);

        ChunkPos chunkPos = new ChunkPos(pos);
        if (!chunkManager.isChunkLoaded(chunkPos.x, chunkPos.z)) {
            System.out.println("Loading Chunk");
            chunkManager.addTicket(ChunkTicketType.FORCED, chunkPos, 1, chunkPos);
            world.getChunk(chunkPos.x, chunkPos.z);
        }

        Block block = world.getBlockState(pos).getBlock();

        processContainerBlockPos(block,pos);
    }
    private void processContainerBlockPos(Block block, BlockPos pos) {
        if (!lockableBlocks.contains(block) && !block.asItem().toString().contains("shulker_box")) return;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null) return;
        NbtCompound nbt = blockEntity.createNbt(registryLookup);
        if (blockPassword.equalsIgnoreCase("unlock") || blockPassword.isEmpty()) {
            if (nbt.contains("Lock")) {
                nbt.remove("Lock");
                blockEntity.read(nbt, registryLookup);
                blockEntity.markDirty();
                world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                lockOrUnlock++;
            }
        }
        else {
            if (!nbt.contains("Lock") || !nbt.getString("Lock").equalsIgnoreCase(blockPassword)) {
                nbt.putString("Lock", blockPassword);
                blockEntity.read(nbt, registryLookup);
                blockEntity.markDirty();
                world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                lockOrUnlock++;
            }
        }
    }

    @Override
    protected void stoppedFunction() {
        player.sendMessage(Text.of("§aBlock scan complete."), false);
        System.out.println("Block scan complete.");
        player.sendMessage(Text.of("- Modified " + lockOrUnlock + " blocks."), false);
    }
}
