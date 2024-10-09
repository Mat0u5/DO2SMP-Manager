package net.mat0u5.do2smpmanager.mixin;

import com.google.gson.JsonObject;
import net.mat0u5.do2smpmanager.utils.DiscordUtils;
import net.mat0u5.do2smpmanager.utils.OtherUtils;
import net.mat0u5.do2smpmanager.utils.TextUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.c2s.play.UpdateCommandBlockC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Inject(method = "handleDecoratedMessage(Lnet/minecraft/network/message/SignedMessage;)V",
            at = @At("HEAD"), cancellable = true)
    private void onHandleDecoratedMessage(SignedMessage message, CallbackInfo ci) {
        ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler) (Object) this;
        ServerPlayerEntity player = handler.player;
        Text originalText = message.getContent();
        String originalContent = originalText.getString();
        if (!originalContent.contains(":")) return;
        String formattedContent = TextUtils.replaceEmotes(originalContent);

        if (!originalContent.equals(formattedContent)) {
            Text playerNameWithFormatting = player.getDisplayName();
            Text formattedContentText = Text.literal(formattedContent).setStyle(originalText.getStyle());
            Text finalMessage = Text.empty().append("<").append(playerNameWithFormatting).append("> ").append(formattedContentText);

            OtherUtils.broadcastMessage(Objects.requireNonNull(player.getServer()), finalMessage);

            JsonObject json = DiscordUtils.getDefaultJSON();
            json.addProperty("content", "`[Server] "+player.getNameForScoreboard()+"` "+TextUtils.formatEmotesForDiscord(originalContent));
            DiscordUtils.sendMessageToDiscordFromAggroNet(json,DiscordUtils.getChatChannelId());
            ci.cancel();
        }
    }
}
