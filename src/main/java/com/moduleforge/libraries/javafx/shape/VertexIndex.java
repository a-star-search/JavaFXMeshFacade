package com.moduleforge.libraries.javafx.shape;

import java.util.HashMap;
import java.util.Map;

class VertexIndex {
   
   private static final Map<Integer, VertexIndex> cache = new HashMap<>();
   
   final int value;
   
   private VertexIndex(int value) {
      this.value = value;
   }
   
   static VertexIndex from(int value) {
      VertexIndex cached = cache.get(Integer.valueOf(value));
      if (cached != null)
         return cached;

      VertexIndex vertexIndex = new VertexIndex(value);
      cache(value, vertexIndex);
      return vertexIndex;
   }
   
   private static void cache(int value, VertexIndex vertexIndex) {
      cache.put(Integer.valueOf(value), vertexIndex);
   }
}
