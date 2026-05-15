package moffy.overloaded_tinkering_lib.common.hooks;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CriticalModifierHook {
    default boolean isCritical(IToolStackView tool, ModifierEntry entry, boolean isCritical, boolean original){
        return original;
    }
    default float setCriticalRate(IToolStackView tool, ModifierEntry entry, float currentRate, float originalRate){
        return originalRate;
    }

    class DefaultClass implements CriticalModifierHook{

    }

    record AllMerger(Collection<CriticalModifierHook> hooks) implements CriticalModifierHook{
        @Override
        public boolean isCritical(IToolStackView tool, ModifierEntry entry, boolean isCritical, boolean original) {
            boolean currentValue = original;
            for(CriticalModifierHook hook : hooks){
                currentValue = hook.isCritical(tool, entry, currentValue, original);
            }
            return currentValue;
        }

        @Override
        public float setCriticalRate(IToolStackView tool, ModifierEntry entry, float currentRate, float originalRate) {
            float rate = originalRate;
            for(CriticalModifierHook hook : hooks){
                rate = hook.setCriticalRate(tool, entry, rate, originalRate);
            }
            return rate;
        }
    }
}
