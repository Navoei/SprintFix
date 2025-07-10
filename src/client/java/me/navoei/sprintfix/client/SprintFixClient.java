package me.navoei.sprintfix.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.ResourceLocation;

public class SprintFixClient implements ClientModInitializer {

    public static final String MOD_ID = "sprintfix";

    @Override
    public void onInitializeClient() {

    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
