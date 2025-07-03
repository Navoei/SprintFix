package me.navoei.sprintfix.mixin.client.ItemPrioritizationMixins;

import me.navoei.sprintfix.client.SprintFixClient;
import me.navoei.sprintfix.util.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startUsingItem(Lnet/minecraft/world/InteractionHand;)V", shift = At.Shift.AFTER), cancellable = true)
    protected void injectBlocksAttacksPriority(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        //Add a check to see if this feature was enabled by the server sending the custom packet.
        if (!SprintFixClient.ENABLED_FEATURES.contains(Feature.FIX_ITEM_PRIORITIES)) return;
        //if (!level.isClientSide()) return;
        if (interactionHand.equals(InteractionHand.OFF_HAND)) return;
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer==null) return;
        HitResult hitResult = Minecraft.getInstance().hitResult;
        ItemStack offHandItemStack = localPlayer.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack itemStackInteractionHand = localPlayer.getItemInHand(interactionHand);
        if (!itemStackInteractionHand.is(ItemTags.SWORDS)) return;
        if (!itemStackInteractionHand.has(DataComponents.BLOCKS_ATTACKS)) return;
        System.out.println("OFfHandIsEmpty: " + offHandItemStack.isEmpty());
        if (!offHandItemStack.isEmpty()) {

            if (localPlayer.getCooldowns().isOnCooldown(offHandItemStack)) {
                localPlayer.stopUsingItem();
                player.stopUsingItem();
                localPlayer.interact(player, InteractionHand.OFF_HAND);
                player.interact(player, InteractionHand.OFF_HAND);
                cir.setReturnValue(InteractionResult.PASS);
                return;
            }

            //localPlayer.stopUsingItem();
            //player.stopUsingItem();
            //localPlayer.interact(localPlayer, InteractionHand.OFF_HAND);
            //player.interact(player, InteractionHand.OFF_HAND);
            //cir.setReturnValue(InteractionResult.PASS);

            //From To calculations
            float f = Mth.wrapDegrees(localPlayer.getYRot());
            float f1 = Mth.wrapDegrees(localPlayer.getXRot());
            double x = player.getX();
            double eyeY = player.getEyeY();
            double z = player.getZ();
            Vec3 from = new Vec3(x, eyeY, z);
            float f3 = Mth.cos(-f * 0.017453292F - 3.1415927F);
            float f4 = Mth.sin(-f * 0.017453292F - 3.1415927F);
            float f5 = -Mth.cos(-f1 * 0.017453292F);
            float f6 = Mth.sin(-f1 * 0.017453292F);
            float f7 = f4 * f5;
            float f8 = f3 * f5;
            double d3 = player.blockInteractionRange();
            Vec3 to = from.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);

            BlockHitResult blockHitResult = localPlayer.level().clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, localPlayer));
            //localPlayer.startUsingItem(InteractionHand.OFF_HAND);
            //localPlayer.interact(localPlayer, InteractionHand.OFF_HAND);
            InteractionResult interactionResult = offHandItemStack.use(localPlayer.level(), localPlayer, InteractionHand.OFF_HAND);
            System.out.println("HitResult: " + blockHitResult.getType());
            System.out.println("InteractionResult: " + interactionResult);
            //System.out.println("LocalPlayer useItem: " + localPlayer.getUseItem());
            //System.out.println("LocalPlayer usedItemHand: " + localPlayer.getUsedItemHand());
            //System.out.println("LocalPlayer ItemInUsedItemHand: " + localPlayer.getItemInHand(player.getUsedItemHand()));
            //System.out.println("LocalPlayer OffHandItem: " + localPlayer.getOffhandItem());
            //System.out.println("LocalPlayer useItemRemainingTicks: " + localPlayer.getUseItemRemainingTicks());
            if (interactionResult instanceof InteractionResult.Success) {
                localPlayer.stopUsingItem();
                player.stopUsingItem();
                localPlayer.interact(localPlayer, InteractionHand.OFF_HAND);
                player.interact(player, InteractionHand.OFF_HAND);
                if (offHandItemStack.getUseAnimation().equals(ItemUseAnimation.NONE)) {
                    localPlayer.swing(InteractionHand.OFF_HAND);
                    player.swing(InteractionHand.OFF_HAND);
                }
                cir.setReturnValue(InteractionResult.PASS);
                return;
            }
            if (blockHitResult.getType()== HitResult.Type.BLOCK) {
                if (offHandItemStack.getItem() instanceof BlockItem && !(interactionResult instanceof InteractionResult.Fail) && hitResult!=null && !hitResult.getType().equals(HitResult.Type.ENTITY)) {
                    localPlayer.stopUsingItem();
                    player.stopUsingItem();
                    localPlayer.interact(localPlayer, InteractionHand.OFF_HAND);
                    player.interact(player, InteractionHand.OFF_HAND);
                    cir.setReturnValue(InteractionResult.PASS);
                    return;
                }
                if (hitResult!=null && !hitResult.getType().equals(HitResult.Type.ENTITY)) {
                    UseOnContext useOnContext = new UseOnContext(localPlayer, InteractionHand.OFF_HAND, blockHitResult);
                    interactionResult = offHandItemStack.useOn(useOnContext);
                    if (interactionResult instanceof InteractionResult.Success) {
                        localPlayer.stopUsingItem();
                        player.stopUsingItem();
                        localPlayer.interact(localPlayer, InteractionHand.OFF_HAND);
                        player.interact(player, InteractionHand.OFF_HAND);
                        if (interactionResult instanceof InteractionResult.Success) {
                            localPlayer.swing(InteractionHand.OFF_HAND);
                            player.swing(InteractionHand.OFF_HAND);
                        }
                        cir.setReturnValue(InteractionResult.PASS);
                        return;
                    }
                }
            }
            localPlayer.stopUsingItem();
            player.stopUsingItem();
            localPlayer.interact(localPlayer, InteractionHand.MAIN_HAND);
            player.interact(player, InteractionHand.MAIN_HAND);
            localPlayer.startUsingItem(InteractionHand.MAIN_HAND);
            cir.setReturnValue(InteractionResult.CONSUME);
            /*
            Items that need to be checked:
            All blocks.
            All items that place an entity (boats/end crystals).
            All consumables.
            All throwables (potions, ender pearls).
            All useables (bows, tridents, crossbows, brush, shears, bundles).
            All items that place blocks (flint and steel).
            All trial keys.
            */
        } else {
            player.startUsingItem(interactionHand);
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startUsingItem(Lnet/minecraft/world/InteractionHand;)V"))
    protected void redirection(Player instance, InteractionHand interactionHand) {
        if (!SprintFixClient.ENABLED_FEATURES.contains(Feature.FIX_ITEM_PRIORITIES)) {
            instance.startUsingItem(interactionHand);
        }
    }

}

