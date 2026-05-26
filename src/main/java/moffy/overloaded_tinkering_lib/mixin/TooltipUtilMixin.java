package moffy.overloaded_tinkering_lib.mixin;

import moffy.overloaded_tinkering_lib.capability.ToolEnchantmentCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

@Mixin(value = TooltipUtil.class, remap = false)
public class TooltipUtilMixin {
    @Inject(
            method = "addModifierNames",
            at = @At("TAIL")
    )
    private static void addExtraEnchantmentTooltip(ItemStack stack, IToolStackView tool, Player player, List<Component> tooltips, TooltipFlag flag, CallbackInfo ci){
        ToolEnchantmentCapability.addTooltip(tool, tooltips);
    }
}
