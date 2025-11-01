package me.navoei.sprintfix.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class SprintFixClientMixin {

    @Shadow
    public abstract boolean isUsingItem();

    @Inject(method = "shouldStopRunSprinting", at = @At("RETURN"), cancellable = true)
    protected void sprintfix$injectSprintFixMethod(CallbackInfoReturnable<Boolean> cir) {
        if (this.isUsingItem()) cir.setReturnValue(true);
    }

}

