package com.moduleforge.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {

   public static <T, R> List<R> apply(Collection<T> coll, Function<? super T, ? extends R> mapper) {
      return coll.stream().map(mapper).collect(Collectors.toList());
   }

   public static int[] toPrimitive(final Integer[] array) {
      if (array == null) {
         throw new IllegalArgumentException();
      }
      if (array.length == 0) {
         return new int[0];
      }
      final int[] result = new int[array.length];
      for (int i = 0; i < array.length; i++) {
         result[i] = array[i].intValue();
      }
      return result;
   }
}
