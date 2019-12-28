package com.tijmen;

import java.util.Set;
import java.util.stream.Collectors;

public class SetUtils {
    public static <T> Set<T> remove(Set<T> set, T element) {
        return set.stream()
                .filter(it -> !it.equals(element))
                .collect(Collectors.toSet());
    }
}
