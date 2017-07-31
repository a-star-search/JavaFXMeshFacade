package com.moduleforge.libraries.javafx.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.javatuples.Pair;

import com.moduleforge.util.Util;

/*
 * We use lists in this class. Don't use collection because apparently the order in the vertices is important for the mesh
 * 
 * And also, a vertex is actually a pair
 * 
 * Also, all the numbers are actually INDICES of actual vertices with actual coordinates
 *  
 *  In a pair of vertex indeces the first value is the coordinates, the second is the texture 
 *  
 */
class TriangleMeshFaceWrapper {

   public static final int TRIANGLE_VERTEX_COUNT = 3;
   private static final int DEFAULT_TEXTURE = 0;

   private List<Pair<VertexIndex, VertexIndex>> vertices;
   private List<VertexIndex> vertexIndeces;
   private List<VertexIndex> textureVertexIndeces;

   private TriangleMeshFaceWrapper() {
      //
   }

   @SafeVarargs
   static TriangleMeshFaceWrapper fromOrdered(Pair<VertexIndex, VertexIndex>... vertices) {
      assert (vertices.length == TRIANGLE_VERTEX_COUNT);
      TriangleMeshFaceWrapper face = new TriangleMeshFaceWrapper();
      List<Pair<VertexIndex, VertexIndex>> v = Arrays.asList(vertices); //maintains order
      face.createList(v);
      return face;
   }

   static TriangleMeshFaceWrapper fromOrdered(List<Pair<VertexIndex, VertexIndex>> vertices) {
      assert (vertices.size() == TRIANGLE_VERTEX_COUNT);
      return TriangleMeshFaceWrapper.fromOrdered(vertices.get(0), vertices.get(1), vertices.get(2));
   }

   static TriangleMeshFaceWrapper fromOrdered(List<VertexIndex> indices, List<VertexIndex> textureIndices) {
      assert (indices.size() == TRIANGLE_VERTEX_COUNT);
      assert (textureIndices.size() == TRIANGLE_VERTEX_COUNT);
      return TriangleMeshFaceWrapper.fromOrdered(
            Pair.with(indices.get(0), textureIndices.get(0)),
            Pair.with(indices.get(1), textureIndices.get(1)),
            Pair.with(indices.get(2), textureIndices.get(2)) );
   }

   /*
    * apply default texture
    */
   static TriangleMeshFaceWrapper fromOrderedNoTexture(List<VertexIndex> vertexIndeces) {
      assert (vertexIndeces.size() == TRIANGLE_VERTEX_COUNT);
      List<Pair<VertexIndex, VertexIndex>> v = createVerticesApplyingDefaultTexture(vertexIndeces);
      return TriangleMeshFaceWrapper.fromOrdered(v.get(0), v.get(1), v.get(2));
   }

   /*
    * apply default texture
    */
   public static TriangleMeshFaceWrapper fromOrderedNoTexture(VertexIndex... vertexIndeces) {
      assert (vertexIndeces.length == TRIANGLE_VERTEX_COUNT);
      List<VertexIndex> lVertexIndices = Arrays.asList(vertexIndeces);
      return fromOrderedNoTexture(lVertexIndices);
   }

   private static List<Pair<VertexIndex, VertexIndex>> createVerticesApplyingDefaultTexture(List<VertexIndex> vertexIndeces) {
      List<Pair<VertexIndex, VertexIndex>> v = new ArrayList<>(TRIANGLE_VERTEX_COUNT);
      for (int i = 0; i < TRIANGLE_VERTEX_COUNT; i++) {         
         v.add(Pair.with(vertexIndeces.get(i), VertexIndex.from(DEFAULT_TEXTURE)));
      }
      return v;
   }

   /*
    * total number of coordinates that define this class
    */
   static final int faceTotalCoordinateCount() {
      //three pairs
      return TRIANGLE_VERTEX_COUNT * 2;
   }

   private void createList(List<Pair<VertexIndex, VertexIndex>> v) {
      vertices = Collections.unmodifiableList(v);
      createVertexIndeces(v);
      createTextureVertexIndeces(v);
   }

   private void createVertexIndeces(List<Pair<VertexIndex, VertexIndex>> v) {
      List<VertexIndex> indices = Util.apply(v, a -> a.getValue0());
      vertexIndeces = Collections.unmodifiableList(new ArrayList<>(indices));
   }

   private void createTextureVertexIndeces(List<Pair<VertexIndex, VertexIndex>> v) {
      List<VertexIndex> indices = Util.apply(v, a -> a.getValue1());
      textureVertexIndeces = Collections.unmodifiableList(new ArrayList<>(indices));
   }

   List<Pair<VertexIndex, VertexIndex>> getVertexIndexPairs() {
      return vertices;
   }

   List<VertexIndex> getVertexIndeces() {
      return vertexIndeces;
   }

   List<VertexIndex> getTextureVertexIndeces() {
      return textureVertexIndeces;
   }

}
