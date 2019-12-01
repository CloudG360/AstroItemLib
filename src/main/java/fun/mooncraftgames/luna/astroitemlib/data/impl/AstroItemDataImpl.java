package fun.mooncraftgames.luna.astroitemlib.data.impl;

import com.google.common.collect.ImmutableList;
import fun.mooncraftgames.luna.astroitemlib.data.AstroItemData;
import fun.mooncraftgames.luna.astroitemlib.data.AstroKeys;
import fun.mooncraftgames.luna.astroitemlib.data.ImmutableAstroItemData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractListData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.ListValue;

import java.util.List;
import java.util.Optional;

public class AstroItemDataImpl extends AbstractListData<String, AstroItemData, ImmutableAstroItemData> implements AstroItemData {

    public AstroItemDataImpl(List<String> tags){
        super(tags, AstroKeys.FUNCTION_TAGS);

    }

    public AstroItemDataImpl(){
        this(ImmutableList.of());
    }

    @Override
    public ListValue<String> ftags() {
        try{
            return getListValue();
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<AstroItemData> fill(DataHolder dataHolder, MergeFunction overlap) {
        try {
            AstroItemData itemDataMerged = overlap.merge(this, dataHolder.get(AstroItemData.class).orElse(null));
            setValue(itemDataMerged.ftags().get());
            return Optional.of(itemDataMerged);
        } catch (Exception err) {
            err.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<AstroItemData> from(DataContainer container) {
        try {
            if(container.contains(AstroKeys.FUNCTION_TAGS)) {
                List<String> tags = container.getObjectList(AstroKeys.FUNCTION_TAGS.getQuery(), String.class).get();
                return Optional.of(setValue(tags));
            }
            return Optional.of(this);
        } catch (Exception err) {
            err.printStackTrace();
            return Optional.of(this);
        }
    }

    @Override
    public DataContainer toContainer() {
        try {
            return super.toContainer().set(AstroKeys.FUNCTION_TAGS.getQuery(), this.getValue());
        } catch (Exception err) {
            err.printStackTrace();
            return super.toContainer();
        }
    }

    @Override
    public AstroItemData copy() {
        try {
            return new AstroItemDataImpl(getValue());
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    @Override
    public ImmutableAstroItemDataImpl asImmutable() {
        try{
            return new ImmutableAstroItemDataImpl(getValue());
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    @Override
    public int getContentVersion() {
        return AstroItemDataBuilder.DATA_VERSION;
    }
}
