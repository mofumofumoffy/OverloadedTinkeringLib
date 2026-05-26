package moffy.overloaded_tinkering_lib.modifier;

import moffy.overloaded_tinkering_lib.capability.ToolEnchantmentCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Map;

public abstract class EnchantmentModifier extends NoLevelsModifier implements EnchantmentModifierHook, ModifierRemovalHook, ValidateModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ENCHANTMENTS, ModifierHooks.REMOVE);
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return advanced;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        if(modifier.getId() == this.getId()){
            ToolEnchantmentCapability.removeAll(iToolStackView);
        }
        return null;
    }

    @Override
    public void updateEnchantments(IToolStackView iToolStackView, ModifierEntry modifierEntry, Map<Enchantment, Integer> map) {
        var enchantmentMap = ToolEnchantmentCapability.readFromPersistentData(iToolStackView);
        for(var enchantmentEntry : enchantmentMap.entrySet()){
            map.compute(enchantmentEntry.getKey(), (enchantment, currentLevel) -> ToolEnchantmentCapability.mergeEnchantment(enchantment, currentLevel, enchantmentEntry.getValue()));
        }
    }

    @Override
    public int updateEnchantmentLevel(IToolStackView iToolStackView, ModifierEntry modifierEntry, Enchantment enchantment, int i) {
        var enchantmentMap = ToolEnchantmentCapability.readFromPersistentData(iToolStackView);
        if(enchantmentMap.containsKey(enchantment)){
            return i + enchantmentMap.get(enchantment);
        }
        return i;
    }
}
