package moffy.overloaded_tinkering_lib.common;

import moffy.overloaded_tinkering_lib.OverloadedTinkeringLib;
import moffy.overloaded_tinkering_lib.capability.FEUniversalEnergyStorage;
import moffy.overloaded_tinkering_lib.capability.provider.OverloadedCapabilityProvider;
import moffy.overloaded_tinkering_lib.common.hooks.CriticalModifierHook;
import moffy.overloaded_tinkering_lib.common.hooks.DamageSourceModifierHook;
import moffy.overloaded_tinkering_lib.common.registry.UniversalEnergyStorageRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;

public class CommonSetup {
    public static void init(FMLJavaModLoadingContext context){
        ToolCapabilityProvider.register(OverloadedCapabilityProvider::new);

        AdvancedModifierHooks.DAMAGE_SOURCE = ModifierHooks.LOADER.register(
                new ModuleHook<>(
                        OverloadedTinkeringLib.getResource("modify_damage_source"),
                        DamageSourceModifierHook.class,
                        DamageSourceModifierHook.AllMerger::new,
                        new DamageSourceModifierHook.DefaultClass()
                )
        );

        AdvancedModifierHooks.CRITICAL = ModifierHooks.LOADER.register(
                new ModuleHook<>(
                        OverloadedTinkeringLib.getResource("critical"),
                        CriticalModifierHook.class,
                        CriticalModifierHook.AllMerger::new,
                        new CriticalModifierHook.DefaultClass()
                )
        );
    }
}
