package fun.mooncraftgames.luna.astroitemlib.tasks.interfaces;

import java.util.HashMap;
import java.util.Map;

public interface IKeyedDataProvider {
    HashMap<String, String> toDataStringMap();
    default String build() {
        String string = "";
        for(Map.Entry<String, String> entry : toDataStringMap().entrySet()){
            string = string.concat(entry.getKey()+": "+entry.getValue()+",\n");
        }
        return string.concat("[END]");
    }
    default String oneLineBuild() {
        String string = "";
        for(Map.Entry<String, String> entry : toDataStringMap().entrySet()){
            string = string.concat(entry.getKey()+": "+entry.getValue()+", ");
        }
        return string.concat("[END]");
    }
}
