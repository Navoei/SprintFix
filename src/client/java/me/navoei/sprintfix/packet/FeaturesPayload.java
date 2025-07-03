package me.navoei.sprintfix.packet;

import me.navoei.sprintfix.client.SprintFixClient;
import me.navoei.sprintfix.util.Feature;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record FeaturesPayload(List<Feature> features) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, FeaturesPayload> CODEC = CustomPacketPayload.codec(null, FeaturesPayload::read);
    public static final Type<FeaturesPayload> PAYLOAD_ID = new Type<>(SprintFixClient.id("allowed_features"));

    private static FeaturesPayload read(FriendlyByteBuf buffer) {
        List<Feature> features = new ArrayList<>();
        final int size = buffer.readVarInt();
        for (int i = 0; i < size; ++i) {
            Feature feature = Feature.byId(buffer.readUtf());
            if (feature != null) {
                features.add(feature);
            }
        }

        return new FeaturesPayload(features);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_ID;
    }
}
