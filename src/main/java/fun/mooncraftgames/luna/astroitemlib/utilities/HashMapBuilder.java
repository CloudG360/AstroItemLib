package fun.mooncraftgames.luna.astroitemlib.utilities;

import java.util.HashMap;

public class HashMapBuilder <K, V> {

    private HashMap<K, V> hashMap;

    private HashMapBuilder(){ hashMap = new HashMap<>(); }

    public HashMapBuilder<K, V> addField(K key, V value){ this.hashMap.put(key, value);return this; }
    public HashMap<K, V> build() { return this.hashMap; }

    public static <K, V> HashMapBuilder<K, V> builder(Class<K> keyclass, Class<V> valueclass) { return new HashMapBuilder<>(); }


}
