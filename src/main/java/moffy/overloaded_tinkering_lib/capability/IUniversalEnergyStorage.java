package moffy.overloaded_tinkering_lib.capability;

import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface IUniversalEnergyStorage<T extends Number & Comparable<T>> {
    T getMaxEnergyStored(IToolStackView tool);
    T getEnergyStored(IToolStackView tool);
    boolean canExtract(IToolStackView tool);
    boolean canReceive(IToolStackView tool);
    <M extends Number & Comparable<M>> T receiveEnergy(IToolStackView tool, M maxReceive, boolean simulate);
    <M extends Number & Comparable<M>> T extractEnergy(IToolStackView tool, M maxExtract, boolean simulate);

    T getLimit();
}
