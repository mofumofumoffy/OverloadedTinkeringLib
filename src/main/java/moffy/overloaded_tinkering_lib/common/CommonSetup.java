package moffy.overloaded_tinkering_lib.common;

import moffy.overloaded_tinkering_lib.OverloadedTinkeringLib;
import moffy.overloaded_tinkering_lib.common.hooks.CriticalModifierHook;
import moffy.overloaded_tinkering_lib.common.hooks.DamageSourceModifierHook;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;

public class CommonSetup {
    public static void init(FMLJavaModLoadingContext context){

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
