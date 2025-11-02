package me.navoei.sprintfix.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer_FixSprintCheck extends AbstractClientPlayer {
    @Shadow
    public ClientInput input;

    @Shadow
    protected abstract boolean isSprintingPossible(boolean flying);

    @Shadow
    public abstract boolean isMovingSlowly();

    @Unique
    private boolean sprintfix$shouldRestoreSprint = false;

    public MixinLocalPlayer_FixSprintCheck(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V", shift = At.Shift.AFTER))
    private void sprintfix$fixMC152728(CallbackInfo ci) {
        if (sprintfix$shouldRestoreSprint && !this.sprintfix$canResumeSprinting()) {
            sprintfix$shouldRestoreSprint = false;
        }

        if (this.isSprinting() && this.isUsingItem()) {
            ItemStack stack = this.getUseItem();
            if (!stack.isEmpty() /*&& !stack.getOrDefault(DataComponents.USE_EFFECTS, UseEffects.DEFAULT).canSprint()*/) {
                this.setSprinting(false);
                sprintfix$shouldRestoreSprint = true;
            }
        } else if (sprintfix$shouldRestoreSprint && !this.isUsingItem()) {
            this.setSprinting(true);
            sprintfix$shouldRestoreSprint = false;
        }
    }

    @Unique
    private boolean sprintfix$canResumeSprinting() {
        return this.input.hasForwardImpulse() &&
                this.isSprintingPossible(this.getAbilities().flying) &&
                (!this.isFallFlying() || this.isUnderWater()) &&
                (!this.isMovingSlowly() || this.isUnderWater());
    }
}

