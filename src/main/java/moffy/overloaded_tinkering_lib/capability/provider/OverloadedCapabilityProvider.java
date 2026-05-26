package moffy.overloaded_tinkering_lib.capability.provider;

import moffy.overloaded_tinkering_lib.capability.AdvancedToolCapabilities;
import moffy.overloaded_tinkering_lib.capability.ToolEnchantmentCapability;
import moffy.overloaded_tinkering_lib.modifier.EnchantmentModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class OverloadedCapabilityProvider implements ToolCapabilityProvider.IToolCapabilityProvider {

    protected final ToolEnchantmentCapability enchantmentCapability;

    public OverloadedCapabilityProvider(ItemStack stack, Supplier<? extends IToolStackView> toolSupplier){
        enchantmentCapability = new ToolEnchantmentCapability(toolSupplier);
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView iToolStackView, Capability<T> capability) {
        if(capability == AdvancedToolCapabilities.ENCHANTMENT_CAPABILITY){
            for(ModifierEntry entry : iToolStackView.getModifierList()){
                if(entry.getModifier() instanceof EnchantmentModifier){
                    return LazyOptional.of(()->enchantmentCapability).cast();
                }
            }
        }
        return LazyOptional.empty();
    }
}
