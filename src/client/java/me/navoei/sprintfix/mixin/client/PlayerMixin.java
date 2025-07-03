package me.navoei.sprintfix.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {

    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 5))
    protected float injectParticleOverrideMixin(float constant) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null)
            return constant;
        if (!player.getWeaponItem().isEnchanted()) {
            return constant;
        }
        for (Holder<Enchantment> enchantmentHolder : player.getWeaponItem().getEnchantments().keySet()) {
            String enchantmentName = enchantmentHolder.getRegisteredName();
            if (enchantmentName.equalsIgnoreCase("minecraft:sharpness") || enchantmentName.equalsIgnoreCase("minecraft:smite") || enchantmentName.equalsIgnoreCase("minecraft:bane_of_arthropods")) {
                return -1.0f;
            }
        }
        return constant;
    }

}
