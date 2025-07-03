package me.navoei.sprintfix.packet;

import me.navoei.sprintfix.client.SprintFixClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record RequestEnabledFeaturesPayload() implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, RequestEnabledFeaturesPayload> CODEC = CustomPacketPayload.codec(null, RequestEnabledFeaturesPayload::write);
    public static final CustomPacketPayload.Type<RequestEnabledFeaturesPayload> PAYLOAD_ID = new CustomPacketPayload.Type<>(SprintFixClient.id("request_enabled_features"));

    private static RequestEnabledFeaturesPayload write(FriendlyByteBuf buffer) {
        return new RequestEnabledFeaturesPayload();
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_ID;
    }

}
