package moffy.overloaded_tinkering_lib.client.model;

import moffy.overloaded_tinkering_lib.client.lib.PartPredicate;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TinkerModelMap {
    private final Map<MaterialVariantId, Supplier<ModelProvider>> cacheByMaterial = new HashMap<>();
    private final Map<ModifierId, Supplier<ModelProvider>> cacheByModifier = new HashMap<>();
    protected Map<Supplier<ModelProvider>, PartPredicate<?>> modelMap;

    public TinkerModelMap(){
        this.modelMap = new HashMap<>();
    }

    public Supplier<ModelProvider> getModelProvider(MaterialVariantId materialVariantId){
        if(cacheByMaterial.containsKey(materialVariantId)){
            return cacheByMaterial.get(materialVariantId);
        }

        for(Supplier<ModelProvider> provider : modelMap.keySet()){
            PartPredicate<?> predicate = modelMap.get(provider);
            if (predicate instanceof PartPredicate.Material materialPredicate) {
                if (materialPredicate.testPredicate(materialVariantId)) {
                    cacheByMaterial.put(materialVariantId, provider);
                    return provider;
                }
            }
        }

        return null;
    }

    public Supplier<ModelProvider> getModelProvider(ModifierId modifierId){
        if(cacheByModifier.containsKey(modifierId)){
            return cacheByModifier.get(modifierId);
        }

        for(Supplier<ModelProvider> provider : modelMap.keySet()){
            PartPredicate<?> predicate = modelMap.get(provider);
            if (predicate instanceof PartPredicate.Modifier modifierPredicate) {
                if (modifierPredicate.testPredicate(modifierId)) {
                    cacheByModifier.put(modifierId, provider);
                    return provider;
                }
            }
        }

        return null;
    }

    public int size() {
        return modelMap.size();
    }

    public void clearCache() {
        cacheByMaterial.clear();
        cacheByModifier.clear();
    }

    public void addModel(PartPredicate<?> predicate, Supplier<ModelProvider> provider){
        this.modelMap.put(provider, predicate);
        clearCache();
    }

    public void addModel(MaterialVariantId materialVariantId, Supplier<ModelProvider> provider){
        addModel(new PartPredicate.Material(materialVariantId), provider);
    }

    public void addModel(ModifierId modifierId, Supplier<ModelProvider> provider){
        addModel(new PartPredicate.Modifier(modifierId), provider);
    }
}
