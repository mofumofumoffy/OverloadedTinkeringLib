package moffy.overloaded_tinkering_lib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(value = ToolEnergyCapability.class, remap = false)
public interface ToolEnergyCapabilityInvoker {
    @Invoker(value = "setEnergyRaw")
    static void invokeSetEnergyRaw(IToolStackView tool, int energy) {
        throw new AssertionError();
    }
}
