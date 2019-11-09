package io.cg360.secondmoon.astroitemlib.data.impl;

import io.cg360.secondmoon.astroitemlib.data.AstroItemData;
import io.cg360.secondmoon.astroitemlib.data.AstroKeys;
import io.cg360.secondmoon.astroitemlib.data.ImmutableAstroItemData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.List;
import java.util.Optional;

public class AstroItemDataBuilder extends AbstractDataBuilder<AstroItemData> implements DataManipulatorBuilder<AstroItemData, ImmutableAstroItemData> {

    public static final int DATA_VERSION = 1;

    public AstroItemDataBuilder(){
        super(AstroItemData.class, DATA_VERSION);
    }

    @Override
    public AstroItemDataImpl create() {
        return new AstroItemDataImpl();
    }

    @Override
    public Optional<AstroItemData> createFrom(DataHolder dataHolder) {
        try{
            return create().fill(dataHolder);
        } catch (Exception err) {
            err.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    protected Optional<AstroItemData> buildContent(DataView container) throws InvalidDataException {
        try{
            if(container.contains(AstroKeys.FUNCTION_TAGS)) {
                List<String> data = container.getStringList(AstroKeys.FUNCTION_TAGS.getQuery()).get();
                return Optional.of(new AstroItemDataImpl(data));
            }
            return Optional.empty();
        } catch (Exception err) {
            err.printStackTrace();
            return Optional.empty();
        }
    }

}
