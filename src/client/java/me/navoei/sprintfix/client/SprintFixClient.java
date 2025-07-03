package me.navoei.sprintfix.client;

import me.navoei.sprintfix.packet.AllowEnabledFeaturesPayload;
import me.navoei.sprintfix.packet.FeaturesPayload;
import me.navoei.sprintfix.packet.RequestEnabledFeaturesPayload;
import me.navoei.sprintfix.util.Feature;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.client.networking.v1.*;

import java.util.ArrayList;
import java.util.List;

public class SprintFixClient implements ClientModInitializer {

    public static List<Feature> ENABLED_FEATURES = new ArrayList<>();
    public static final String MOD_ID = "sprintfix";
    static ModContainer modContainer = FabricLoader.getInstance().getModContainer("sprintfix").orElseThrow();
    static ModMetadata modMetadata = modContainer.getMetadata();
    static String version = modMetadata.getVersion().getFriendlyString();
    static double modVersion = Double.parseDouble(version);

    public static AllowEnabledFeaturesPayload getInfoPayload() {
        return new AllowEnabledFeaturesPayload(modVersion);
    }

    @Override
    public void onInitializeClient() {
        ClientLoginConnectionEvents.DISCONNECT.register((packet, client) -> ENABLED_FEATURES.clear());
        ClientConfigurationConnectionEvents.DISCONNECT.register((packet, client) -> ENABLED_FEATURES.clear());
        ClientPlayConnectionEvents.DISCONNECT.register((packet, client) -> ENABLED_FEATURES.clear());

        PayloadTypeRegistry.configurationS2C().register(FeaturesPayload.PAYLOAD_ID, FeaturesPayload.CODEC);
        ClientConfigurationNetworking.registerGlobalReceiver(FeaturesPayload.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> {
            ENABLED_FEATURES.clear();
            ENABLED_FEATURES.addAll(payload.features());
        }));
        PayloadTypeRegistry.playS2C().register(FeaturesPayload.PAYLOAD_ID, FeaturesPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(FeaturesPayload.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> {
            ENABLED_FEATURES.clear();
            ENABLED_FEATURES.addAll(payload.features());
        }));
        PayloadTypeRegistry.playC2S().register(AllowEnabledFeaturesPayload.PAYLOAD_ID, AllowEnabledFeaturesPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RequestEnabledFeaturesPayload.PAYLOAD_ID, RequestEnabledFeaturesPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(RequestEnabledFeaturesPayload.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> ClientPlayNetworking.send(SprintFixClient.getInfoPayload())));

    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
