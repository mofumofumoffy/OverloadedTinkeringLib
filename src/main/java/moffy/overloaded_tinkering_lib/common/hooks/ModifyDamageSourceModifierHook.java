package moffy.overloaded_tinkering_lib.common.hooks;

import net.minecraft.world.damagesource.DamageSource;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface ModifyDamageSourceModifierHook {
    DamageSource modifyDamageSource(IToolStackView tool, ModifierEntry modifierEntry, DamageSource currentSource, DamageSource original);

    class DefaultClass implements ModifyDamageSourceModifierHook {
        @Override
        public DamageSource modifyDamageSource(IToolStackView tool, ModifierEntry modifierEntry, DamageSource currentSource, DamageSource original) {
            return original;
        }
    }

    record AllMerger(Collection<ModifyDamageSourceModifierHook> modules) implements ModifyDamageSourceModifierHook {
        @Override
        public DamageSource modifyDamageSource(IToolStackView tool, ModifierEntry modifierEntry, DamageSource currentSource, DamageSource original) {
            DamageSource source = original;
            for(ModifyDamageSourceModifierHook hook : modules){
                source = hook.modifyDamageSource(tool, modifierEntry, source, original);
            }
            return source;
        }
    }
}
