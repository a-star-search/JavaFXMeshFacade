package com.moduleforge.libraries.javafx.shape;

import static com.moduleforge.util.Util.apply;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;

/*
 * A mesh that is not a mess
 * 
 * The stupid name TriMesh is avoid confussion with JavaFX classes Mesh or TriangleMesh
 */
public class TriangleMeshFacade {

   private Map<String, Integer> vertexIdentifierToIndexMap;
   private TriangleMeshWrapper delegate;
   
   private TriangleMeshFacade() {
      vertexIdentifierToIndexMap = new HashMap<>();
      delegate = TriangleMeshWrapper.makeFrom(new Point3D[0]);
   }
   
   public static TriangleMeshFacade makeFrom(Collection<Vertex> vertices) {
      TriangleMeshFacade mesh = new TriangleMeshFacade();
      mesh.delegate = TriangleMeshWrapper.makeFrom(apply(vertices, a -> a.getCoordinates()));
      mesh.vertexIdentifierToIndexMap = 
            makeVertexIdentifierToIndexMap(apply(vertices, a -> a.getIdentifier()));
      return mesh;
   }
   
   private static Map<String, Integer> makeVertexIdentifierToIndexMap(Collection<String> identifiers) {
      Map<String, Integer> map = new HashMap<>();
      int index = 0;
      for(String vertexID : identifiers) {
         map.put(vertexID, Integer.valueOf(index));
         index++;
      }
      return map;
   }

   /*
    * the order of the faces is important
    */
   public void setFaces(List<TriangleMeshFace> faces) {
      List<TriangleMeshFaceWrapper> llFaces = 
            lowLevelFaceToHighLevelFace(faces);
      delegate.setFaces(llFaces);
   }

   private List<TriangleMeshFaceWrapper> lowLevelFaceToHighLevelFace(List<TriangleMeshFace> faces){
      List<TriangleMeshFaceWrapper> llFaces = new ArrayList<>();
      for (TriangleMeshFace face : faces) {
         TriangleMeshFaceWrapper newFace = lowLevelFaceToHighLevelFace(face);
         llFaces.add(newFace);
      }
      return llFaces;
   }

   private TriangleMeshFaceWrapper lowLevelFaceToHighLevelFace(TriangleMeshFace face) {
      List<Vertex> vertices = face.getOrderedVertices();
      List<VertexIndex> orderedVertexIndices = new ArrayList<>();
      for (Vertex vertex : vertices) {
         int index = vertexToIndex(vertex);
         orderedVertexIndices.add(VertexIndex.from(index));  
      }
      TriangleMeshFaceWrapper newFace = TriangleMeshFaceWrapper.fromOrderedVerticesNoTexture(orderedVertexIndices); 
      return newFace;
   }

   private int vertexToIndex(Vertex vertex) {
      Integer index = this.vertexIdentifierToIndexMap.get(vertex.getIdentifier());
      return index.intValue();
   }

   public final TriangleMesh toTriangleMesh() {
      return delegate.toTriangleMesh();
   }
   
}
