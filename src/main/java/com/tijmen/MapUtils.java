package com.tijmen;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MapUtils {
    /**
     * Removes an element K as key, and also removes K from all the sets. Expensive!
     */
    public static <K> Map<K, Set<K>> removeWithSet(Map<K, Set<K>> map, K element) {
        Map<K, Set<K>> copy = map.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(element))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        copy.replaceAll((key, set) -> SetUtils.remove(set, element));
        return copy;
    }

    /**
     * Removes an element K as key, and also removes K from all the sets. Expensive!
     */
    public static <K, V> Map<K, Map<K, V>> removeWithMap(Map<K, Map<K, V>> map, K element) {
        Map<K, Map<K, V>> copy = map.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(element))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        copy.replaceAll((key, subMap) -> MapUtils.remove(subMap, element));
        return copy;
    }

    public static <K, V> Map<K, V> remove(Map<K, V> map, K element) {
        return map.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(element))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
