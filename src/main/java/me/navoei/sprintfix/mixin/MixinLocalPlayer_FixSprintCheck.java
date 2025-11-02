package me.navoei.sprintfix.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer_FixSprintCheck {
    @Shadow
    public abstract boolean isUsingItem();

    @ModifyReturnValue(method = "shouldStopRunSprinting", at = @At("RETURN"))
    private boolean sprintfix$addItemUseCheck(boolean original) {
        return original || this.isUsingItem();
    }
}

