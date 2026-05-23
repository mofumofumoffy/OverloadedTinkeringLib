package moffy.overloaded_tinkering_lib.common;

import moffy.overloaded_tinkering_lib.OverloadedTinkeringLib;
import moffy.overloaded_tinkering_lib.capability.FEUniversalEnergyStorage;
import moffy.overloaded_tinkering_lib.common.hooks.CriticalModifierHook;
import moffy.overloaded_tinkering_lib.common.hooks.ModifyDamageSourceModifierHook;
import moffy.overloaded_tinkering_lib.common.registry.UniversalEnergyStorageRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;

public class CommonSetup {
    public static void init(FMLJavaModLoadingContext context){
        UniversalEnergyStorageRegistry.INSTANCE.register(new FEUniversalEnergyStorage());

        AdvancedModifierHooks.MODIFY_DAMAGE_SOURCE = ModifierHooks.LOADER.register(
                new ModuleHook<>(
                        OverloadedTinkeringLib.getResource("modify_damage_source"),
                        ModifyDamageSourceModifierHook.class,
                        ModifyDamageSourceModifierHook.AllMerger::new,
                        new ModifyDamageSourceModifierHook.DefaultClass()
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
