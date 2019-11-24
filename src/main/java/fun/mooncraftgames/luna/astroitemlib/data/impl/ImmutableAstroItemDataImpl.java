package fun.mooncraftgames.luna.astroitemlib.data.impl;

import fun.mooncraftgames.luna.astroitemlib.data.AstroItemData;
import fun.mooncraftgames.luna.astroitemlib.data.AstroKeys;
import fun.mooncraftgames.luna.astroitemlib.data.ImmutableAstroItemData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableListData;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;

import java.util.List;

public class ImmutableAstroItemDataImpl extends AbstractImmutableListData<String, ImmutableAstroItemData, AstroItemData> implements ImmutableAstroItemData {

    public ImmutableAstroItemDataImpl(List<String> value){
        super(value, AstroKeys.FUNCTION_TAGS);
    }

    @Override
    public ImmutableListValue<String> ftags() {
        return getListValue();
    }

    @Override
    public AstroItemData asMutable() {
        return new AstroItemDataImpl(getValue());
    }

    @Override
    public DataContainer toContainer() {
        try{
        return super.toContainer().set(AstroKeys.FUNCTION_TAGS.getQuery(), this.getListValue());
        } catch (Exception err) {
            err.printStackTrace();
            return super.toContainer();
        }
    }

    @Override
    public int getContentVersion() {
        return AstroItemDataBuilder.DATA_VERSION;
    }
}
