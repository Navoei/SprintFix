package me.navoei.sprintfix.util;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum Feature {

    FIX_ITEM_PRIORITIES("fix_item_priorities");

    private final String id;

    public static @Nullable Feature byId(String id) {
        return Arrays.stream(Feature.values()).filter(feature -> feature.id.equals(id)).findFirst().orElse(null);
    }

    Feature(String id) {
        this.id = id;
    }

}
