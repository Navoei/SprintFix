package me.navoei.sprintfix.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class SprintFixClientMixin {

    @Inject(method = "shouldStopRunSprinting", at = @At("RETURN"), cancellable = true)
    protected void injectSprintFixMethod(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player==null) return;
        if (player.isUsingItem()) cir.setReturnValue(true);
    }

}

