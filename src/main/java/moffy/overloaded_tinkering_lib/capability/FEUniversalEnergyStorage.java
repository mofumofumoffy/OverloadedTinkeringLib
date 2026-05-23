package moffy.overloaded_tinkering_lib.capability;

import moffy.overloaded_tinkering_lib.mixin.ToolEnergyCapabilityInvoker;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FEUniversalEnergyStorage implements IUniversalEnergyStorage<Integer>{
    @Override
    public Integer getMaxEnergyStored(IToolStackView tool) {
        return ToolEnergyCapability.getMaxEnergy(tool);
    }

    @Override
    public Integer getEnergyStored(IToolStackView tool) {
        return ToolEnergyCapability.getEnergy(tool);
    }

    @Override
    public boolean canExtract(IToolStackView tool) {
        return true;
    }

    @Override
    public boolean canReceive(IToolStackView tool) {
        return true;
    }

    @Override
    public <M extends Number & Comparable<M>> Integer receiveEnergy(IToolStackView tool, M maxReceive, boolean simulate) {
        if (maxReceive.intValue() <= 0) {
            return 0;
        }
        int current = getEnergyStored(tool);
        int filled = Math.min(getMaxEnergyStored(tool) - current, maxReceive.intValue());
        if (!simulate) {
            ToolEnergyCapabilityInvoker.invokeSetEnergyRaw(tool, current + filled);
        }
        return filled;
    }

    @Override
    public <M extends Number & Comparable<M>> Integer extractEnergy(IToolStackView tool, M maxExtract, boolean simulate) {
        if (maxExtract.intValue() <= 0) {
            return 0;
        }
        int current = getEnergyStored(tool);
        if (current <= 0) {
            return 0;
        }
        int drained = maxExtract.intValue();
        if (current < drained) {
            drained = current;
        }
        if (!simulate) {
            ToolEnergyCapabilityInvoker.invokeSetEnergyRaw(tool, current - drained);
        }
        return drained;
    }

    @Override
    public Integer getLimit() {
        return Integer.MAX_VALUE;
    }
}
