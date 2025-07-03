package me.navoei.sprintfix.packet;

import me.navoei.sprintfix.client.SprintFixClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record AllowEnabledFeaturesPayload(double modVersion) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, AllowEnabledFeaturesPayload> CODEC = CustomPacketPayload.codec(AllowEnabledFeaturesPayload::write, null);
    public static final CustomPacketPayload.Type<AllowEnabledFeaturesPayload> PAYLOAD_ID = new CustomPacketPayload.Type<>(SprintFixClient.id("allow"));

    private void write(FriendlyByteBuf buffer) {
        buffer.writeDouble(modVersion);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_ID;
    }
}
