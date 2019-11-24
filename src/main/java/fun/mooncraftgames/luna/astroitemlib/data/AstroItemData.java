package fun.mooncraftgames.luna.astroitemlib.data;

import org.spongepowered.api.data.manipulator.mutable.ListData;
import org.spongepowered.api.data.value.mutable.ListValue;

public interface AstroItemData extends ListData<String, AstroItemData, ImmutableAstroItemData> {

    ListValue<String> ftags();

}
