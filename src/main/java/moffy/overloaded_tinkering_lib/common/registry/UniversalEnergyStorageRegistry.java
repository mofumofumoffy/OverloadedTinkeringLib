package moffy.overloaded_tinkering_lib.common.registry;

import moffy.overloaded_tinkering_lib.capability.IUniversalEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class UniversalEnergyStorageRegistry {
    public static UniversalEnergyStorageRegistry INSTANCE = new UniversalEnergyStorageRegistry();

    private final List<IUniversalEnergyStorage<?>> universalEnergyHandlerList;
    private IUniversalEnergyStorage<?> largestStorage = null;

    public UniversalEnergyStorageRegistry() {
        this.universalEnergyHandlerList = new ArrayList<>();
    }

    public void register(IUniversalEnergyStorage<?> handler){
        universalEnergyHandlerList.add(handler);
    }

    @Nullable
    public IUniversalEnergyStorage<?> getStorage(){
        if(largestStorage == null){
            init();
        }
        return largestStorage;
    }

    private void init(){
        var largestStorageOptional = universalEnergyHandlerList.stream().max(Comparator.comparing(IUniversalEnergyStorage::getLimit));
        largestStorageOptional.ifPresent(numberIUniversalEnergyStorage -> largestStorage = numberIUniversalEnergyStorage);
    }
}
