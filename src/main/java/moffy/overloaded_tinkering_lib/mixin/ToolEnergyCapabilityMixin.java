package moffy.overloaded_tinkering_lib.mixin;

import moffy.overloaded_tinkering_lib.capability.IUniversalEnergyStorage;
import moffy.overloaded_tinkering_lib.common.registry.UniversalEnergyStorageRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

@Mixin(value = ToolEnergyCapability.class, remap = false)
public class ToolEnergyCapabilityMixin {
    @Shadow
    public Supplier<? extends IToolStackView> tool() {
        throw new AssertionError();
    }

    @Inject(
            method = "receiveEnergy",
            at = @At("HEAD"),
            cancellable = true
    )
    public void wrapReceiveEnergy(int maxReceive, boolean simulate, CallbackInfoReturnable<Integer> cir){
        IUniversalEnergyStorage<?> storage = UniversalEnergyStorageRegistry.INSTANCE.getStorage();
        if(storage != null){
            cir.setReturnValue(storage.receiveEnergy(tool().get(), maxReceive, simulate).intValue());
        }
    }

    @Inject(
            method = "extractEnergy",
            at = @At("HEAD"),
            cancellable = true
    )
    public void wrapExtractEnergy(int maxExtract, boolean simulate, CallbackInfoReturnable<Integer> cir){
        IUniversalEnergyStorage<?> storage = UniversalEnergyStorageRegistry.INSTANCE.getStorage();
        if(storage != null){
            cir.setReturnValue(storage.extractEnergy(tool().get(), maxExtract, simulate).intValue());
        }
    }

    @Inject(
            method = "getEnergyStored",
            at = @At("HEAD"),
            cancellable = true
    )
    public void wrapGetEnergyStored(CallbackInfoReturnable<Integer> cir){
        IUniversalEnergyStorage<?> storage = UniversalEnergyStorageRegistry.INSTANCE.getStorage();
        if(storage != null){
            cir.setReturnValue(storage.getEnergyStored(tool().get()).intValue());
        }
    }

    @Inject(
            method = "getMaxEnergyStored",
            at = @At("HEAD"),
            cancellable = true
    )
    public void wrapGetMaxEnergyStored(CallbackInfoReturnable<Integer> cir){
        IUniversalEnergyStorage<?> storage = UniversalEnergyStorageRegistry.INSTANCE.getStorage();
        if(storage != null){
            cir.setReturnValue(storage.getMaxEnergyStored(tool().get()).intValue());
        }
    }

    @Inject(
            method = "canExtract",
            at = @At("HEAD"),
            cancellable = true
    )
    public void wrapCanExtract(CallbackInfoReturnable<Boolean> cir){
        IUniversalEnergyStorage<?> storage = UniversalEnergyStorageRegistry.INSTANCE.getStorage();
        if(storage != null){
            cir.setReturnValue(storage.canExtract(tool().get()));
        }
    }

    @Inject(
            method = "canReceive",
            at = @At("HEAD"),
            cancellable = true
    )
    public void wrapCanReceive(CallbackInfoReturnable<Boolean> cir){
        IUniversalEnergyStorage<?> storage = UniversalEnergyStorageRegistry.INSTANCE.getStorage();
        if(storage != null){
            cir.setReturnValue(storage.canReceive(tool().get()));
        }
    }
}
