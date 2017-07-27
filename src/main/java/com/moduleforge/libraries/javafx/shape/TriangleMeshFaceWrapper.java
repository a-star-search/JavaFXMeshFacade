package com.moduleforge.libraries.javafx.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.moduleforge.util.Util;

/*
 * We use lists in this class. Don't use collection because apparently the order in the vertices is important for the mesh
 * 
 * And also, a vertex is actually a pair
 * 
 * Also, all the numbers are actually INDICES of actual vertices with actual coordinates
 * 
 * At least, regarding the texture we can just simply put zeros for all values 
 * and we will still get something we can look at.
 *  
 */
class TriangleMeshFaceWrapper {

   public static final int TRIANGLE_VERTEX_COUNT = 3;
   private static final int DEFAULT_TEXTURE = 0;

   private List<VertexIndexPair> vertices;
   private List<VertexIndex> vertexIndeces;

   private TriangleMeshFaceWrapper() {
      //
   }

   public static TriangleMeshFaceWrapper fromOrderedVertices(VertexIndexPair... vertices) {
      assert (vertices.length == TRIANGLE_VERTEX_COUNT);
      TriangleMeshFaceWrapper face = new TriangleMeshFaceWrapper();
      List<VertexIndexPair> v = Arrays.asList(vertices); //maintains order
      face.createList(v);
      return face;
   }

   public static TriangleMeshFaceWrapper fromOrderedVertices(List<VertexIndexPair> vertices) {
      assert (vertices.size() == TRIANGLE_VERTEX_COUNT);
      return TriangleMeshFaceWrapper.fromOrderedVertices(vertices.get(0), vertices.get(1), vertices.get(2));
   }

   /*
    * apply default texture
    */
   public static TriangleMeshFaceWrapper fromOrderedVerticesNoTexture(List<VertexIndex> vertexIndeces) {
      assert (vertexIndeces.size() == TRIANGLE_VERTEX_COUNT);
      List<VertexIndexPair> v = createVerticesApplyingDefaultTexture(vertexIndeces);
      return TriangleMeshFaceWrapper.fromOrderedVertices(v.get(0), v.get(1), v.get(2));
   }

   /*
    * apply default texture
    */
   public static TriangleMeshFaceWrapper fromOrderedVerticesNoTexture(VertexIndex... vertexIndeces) {
      assert (vertexIndeces.length == TRIANGLE_VERTEX_COUNT);
      List<VertexIndex> lVertexIndices = Arrays.asList(vertexIndeces);
      return fromOrderedVerticesNoTexture(lVertexIndices);
   }

   private static List<VertexIndexPair> createVerticesApplyingDefaultTexture(List<VertexIndex> vertexIndeces) {
      List<VertexIndexPair> v = new ArrayList<>(TRIANGLE_VERTEX_COUNT);
      for (int i = 0; i < TRIANGLE_VERTEX_COUNT; i++) {
         v.add(new VertexIndexPair(vertexIndeces.get(i), VertexIndex.from(DEFAULT_TEXTURE)));
      }
      return v;
   }

   /*
    * total number of coordinates that define this class
    */
   public static final int faceTotalCoordinateCount() {
      //three pairs
      return TRIANGLE_VERTEX_COUNT * 2;
   }

   private void createList(List<VertexIndexPair> v) {
      vertices = Collections.unmodifiableList(v);
      createVertexIndeces(v);
      //we do nothing with the texture information
   }

   private void createVertexIndeces(List<VertexIndexPair> v) {
      List<VertexIndex> lVertexIndeces = Util.apply(v, a -> a.getIndex());
      vertexIndeces = Collections.unmodifiableList(new ArrayList<>(lVertexIndeces));
   }

   public List<VertexIndexPair> getVertexIndexPairs() {
      return vertices;
   }

   public List<VertexIndex> getVertexIndeces() {
      return vertexIndeces;
   }

}
