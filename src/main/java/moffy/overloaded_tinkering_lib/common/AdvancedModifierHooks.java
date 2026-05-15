package moffy.overloaded_tinkering_lib.common;

import moffy.overloaded_tinkering_lib.common.hooks.CriticalModifierHook;
import moffy.overloaded_tinkering_lib.common.hooks.ModifyDamageSourceModifierHook;
import slimeknights.tconstruct.library.module.ModuleHook;

public class AdvancedModifierHooks {
    public static ModuleHook<ModifyDamageSourceModifierHook> MODIFY_DAMAGE_SOURCE;
    public static ModuleHook<CriticalModifierHook> CRITICAL;
}
