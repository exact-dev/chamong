package com.project.chamong.utils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeduplicationUtils {
  public static <T> List<T> removeDuplication(final List<T> list, Function<? super T,?> key){
    return list.stream().filter(removeDuplication(key)).collect(Collectors.toList());
  }
  
  private static <T> Predicate<T> removeDuplication(Function<? super T, ?> key) {
    final Set<Object> set = ConcurrentHashMap.newKeySet();
    return predicate -> set.add(key.apply(predicate));
  }
  
}
