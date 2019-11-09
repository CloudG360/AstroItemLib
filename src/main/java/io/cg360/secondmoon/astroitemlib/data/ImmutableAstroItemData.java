package io.cg360.secondmoon.astroitemlib.data;

import org.spongepowered.api.data.manipulator.immutable.ImmutableListData;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;

public interface ImmutableAstroItemData extends ImmutableListData<String, ImmutableAstroItemData, AstroItemData> {

    ImmutableListValue<String> ftags();

}
