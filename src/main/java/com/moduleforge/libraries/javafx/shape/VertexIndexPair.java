package com.moduleforge.libraries.javafx.shape;

/*
 * A vertex is just a pair of indices of vertices (isn't javaFX api lovely...)
 * 
 * Can't find a better class name for this nonsense
 */

class VertexIndexPair extends javafx.util.Pair<VertexIndex, VertexIndex> {

   public VertexIndexPair(VertexIndex vertexIndex, VertexIndex textureVertexIndex) {
      super(vertexIndex, textureVertexIndex);
   }

   public VertexIndex getIndex() {
      return getKey();
   }

   public VertexIndex getTextureIndex() {
      return getValue();
   }
}
