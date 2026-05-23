package moffy.overloaded_tinkering_lib.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moffy.overloaded_tinkering_lib.common.AdvancedModifierHooks;
import moffy.overloaded_tinkering_lib.common.hooks.CriticalModifierHook;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = ToolAttackUtil.class, remap = false)
public class ToolAttackUtilMixin {
    @ModifyExpressionValue(
            method = "performAttack",
            at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/context/ToolAttackContext;makeDamageSource()Lnet/minecraft/world/damagesource/DamageSource;")
    )
    private static DamageSource modifyDamageSource(DamageSource original, @Local(argsOnly = true) IToolStackView tool){
        DamageSource newDamageSource = original;
        for(ModifierEntry entry : tool.getModifierList()){
            newDamageSource = entry.getHook(AdvancedModifierHooks.MODIFY_DAMAGE_SOURCE).modifyDamageSource(tool, entry, newDamageSource, original);
        }
        return newDamageSource;
    }

    @WrapOperation(
            method = "getCriticalModifier",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/player/CriticalHitEvent;getDamageModifier()F")
    )
    private static float modifyCriticalModifier(
            CriticalHitEvent instance,
            Operation<Float> original,
            LivingEntity attacker,
            Player attackerPlayer,
            Entity target,
            @Local(name = "isCritical") boolean isCritical,
            @Local(name = "criticalModifier") float criticalModifier
    ){
        boolean currentCrit = isCritical;
        float currentModifier = criticalModifier;

        for(EquipmentSlot slot : EquipmentSlot.values()){
            ItemStack stack = attacker.getItemBySlot(slot);
            if(stack.getItem() instanceof IModifiable){
                ToolStack tool = ToolStack.from(stack);

                for(ModifierEntry entry : tool.getModifierList()){
                    CriticalModifierHook hook = entry.getHook(AdvancedModifierHooks.CRITICAL);
                    currentCrit = hook.isCritical(tool, entry, currentCrit, isCritical);
                    currentModifier = hook.setCriticalRate(tool, entry, currentModifier, criticalModifier);
                }
            }
        }

        return original.call(ForgeHooks.getCriticalHit(attackerPlayer, target, currentCrit, currentModifier));
    }

}
