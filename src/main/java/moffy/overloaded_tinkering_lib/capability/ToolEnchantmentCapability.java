package moffy.overloaded_tinkering_lib.capability;

import moffy.overloaded_tinkering_lib.OverloadedTinkeringLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ToolEnchantmentCapability {
    public static final ResourceLocation TOOL_ENCHANTMENTS = OverloadedTinkeringLib.getResource("tool_enchantments");

    protected final Map<Enchantment, Integer> enchantments;
    protected final IToolStackView tool;

    public ToolEnchantmentCapability(Supplier<? extends IToolStackView> toolSupplier) {
        this.tool = toolSupplier.get();
        enchantments = readFromPersistentData(tool);
    }

    public void add(Enchantment enchantment, int level){
        enchantments.compute(enchantment, (enchantment1, currentLevel) -> mergeEnchantment(enchantment, currentLevel, level));
        writeToPersistentData(tool, enchantments);
    }

    public static int mergeEnchantment(Enchantment enchantment, @Nullable Integer currentLevel, int inputLevel){
        if(currentLevel == null){
            return inputLevel;
        } else {
            if(currentLevel == inputLevel){
                return Math.min(enchantment.getMaxLevel(), currentLevel + 1);
            } else {
                return Math.max(currentLevel, inputLevel);
            }
        }
    }

    public static void removeAll(IToolStackView tool){
        tool.getPersistentData().remove(TOOL_ENCHANTMENTS);
    }

    public static void writeToPersistentData(IToolStackView tool, Map<Enchantment, Integer> enchantments){
        ListTag enchantmentsTag = new ListTag();
        for(var enchantmentEntry : enchantments.entrySet()){
            ResourceLocation enchantmentKey = EnchantmentHelper.getEnchantmentId(enchantmentEntry.getKey());
            if(enchantmentKey != null){
                CompoundTag enchantmentEntryTag = new CompoundTag();
                enchantmentEntryTag.putString("id", enchantmentKey.toString());
                enchantmentEntryTag.putInt("lvl", enchantmentEntry.getValue());
            }
        }
        tool.getPersistentData().put(TOOL_ENCHANTMENTS, enchantmentsTag);
    }

    public static Map<Enchantment, Integer> readFromPersistentData(IToolStackView tool){
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        if(tool.getPersistentData().contains(TOOL_ENCHANTMENTS, Tag.TAG_LIST)){
            ListTag enchantmentsTag = (ListTag) tool.getPersistentData().get(TOOL_ENCHANTMENTS);
            for(int i = 0; i < enchantmentsTag.size(); i++){
                CompoundTag enchantmentEntryTag = enchantmentsTag.getCompound(i);
                ResourceLocation enchantmentId = ResourceLocation.tryParse(enchantmentEntryTag.getString("id"));
                if(enchantmentId != null){
                    Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(enchantmentId);
                    if(enchantment != null){
                        enchantments.put(enchantment, enchantmentEntryTag.getInt("lvl"));
                    }
                }
            }
        }
        return enchantments;
    }

    @SuppressWarnings("deprecation")
    public static void addTooltip(IToolStackView tool, List<Component> tooltips){
        if(tool.getPersistentData().contains(TOOL_ENCHANTMENTS)){
            ListTag enchantmentsTag = (ListTag) tool.getPersistentData().get(TOOL_ENCHANTMENTS);

            for(int i = 0; i < enchantmentsTag.size(); ++i) {
                CompoundTag enchantmentTag = enchantmentsTag.getCompound(i);
                BuiltInRegistries.ENCHANTMENT.getOptional(ResourceLocation.tryParse(enchantmentTag.getString("id"))).ifPresent((enchantment) -> tooltips.add(enchantment.getFullname(enchantmentTag.getInt("lvl"))));
            }
        }
    }
}
