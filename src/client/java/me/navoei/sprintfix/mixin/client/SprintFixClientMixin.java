package me.navoei.sprintfix.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class SprintFixClientMixin {

    @Inject(method = "applyMovementSpeedFactors", at = @At("RETURN"))
    protected void injectSprintFixMethod(Vec2f input, CallbackInfoReturnable<Vec2f> cir) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        if(clientPlayerEntity.isUsingItem()) {
            clientPlayerEntity.setSprinting(false);
        }
    }
}

