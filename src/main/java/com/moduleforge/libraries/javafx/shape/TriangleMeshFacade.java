package com.moduleforge.libraries.javafx.shape;

import static com.moduleforge.util.Util.apply;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;

/**
 * 
 * A more intuitive TriangleMesh than JavaFX's
 * 
 * Texture =======
 * 
 * Texture is intuitive and easy the understand. At least it would be if someone
 * would have taken the time to explain it in the javadoc.
 * 
 * A texture is given by a set of points in a bidimensional space of dimension
 * one by one.
 * 
 * The texture points are added first to the mesh object.
 * 
 * Then on a second call to the method in which all points are added, sets of
 * three texture points are matched with faces.
 * 
 * In that way, part of a bidimensional space is matched with the surface of the
 * polygon.
 * 
 * When the MeshView object is created and a material (that may contain an
 * image) assigned to it, the material is matched with the one by one texture
 * space.
 * 
 */
public class TriangleMeshFacade {

   private Map<String, Integer> vertexIdentifierToIndexMap;
   private Map<Point2D, Integer> textureVertexToIndexMap;
   private TriangleMeshWrapper delegate;

   private TriangleMeshFacade() {
      vertexIdentifierToIndexMap = new HashMap<>();
      delegate = TriangleMeshWrapper.fromOrdered(new Point3D[0]);
   }

   /**
    * the vertices need not be in order
    * 
    * the faces need not to be in order
    */
   public static TriangleMeshFacade from(Set<Vertex> vertices, Set<TriangleMeshFace> faces) {
      boolean sameVerticesInSets = checkSameVerticesInBothSets(vertices, faces);
      if (!sameVerticesInSets)
         throw new IllegalArgumentException();
      TriangleMeshFacade mesh = new TriangleMeshFacade();
      List<Point2D> texturePoints = extractTexturePoints(faces);
      mesh.delegate = TriangleMeshWrapper.fromOrdered(apply(vertices, a -> a.getCoordinates()), texturePoints);
      mesh.vertexIdentifierToIndexMap = makeVertexIdentifierToIndexMap(apply(vertices, a -> a.getIdentifier()));
      mesh.textureVertexToIndexMap = makeTextureVertexToIndexMap(texturePoints);
      mesh.setFaces(faces);
      List<Integer> faceSmoothingGroups = new ArrayList<>();
      for (int i = 0; i < faces.size(); i++) {
         faceSmoothingGroups.add(0);
      }
      mesh.applyFaceSmoothingGroups(faceSmoothingGroups);
      return mesh;
   }

   private void applyFaceSmoothingGroups(List<Integer> faceSmoothingGroups) {
      delegate.applyFaceSmoothingGroups(faceSmoothingGroups);
   }

   private static Map<Point2D, Integer> makeTextureVertexToIndexMap(List<Point2D> texturePoints) {
      Map<Point2D, Integer> map = new HashMap<>();
      for (int i = 0; i < texturePoints.size(); i++) {
         map.put(texturePoints.get(i), Integer.valueOf(i));
      }
      return map;
   }

   private static List<Point2D> extractTexturePoints(Set<TriangleMeshFace> faces) {
      Set<Point2D> resultSet = new HashSet<>();
      for (TriangleMeshFace face : faces) {
         resultSet.addAll(face.getTextureVerticesWithCreationOrdering());
      }
      List<Point2D> result = new ArrayList<>(resultSet);
      return result;
   }

   private static boolean checkSameVerticesInBothSets(Collection<Vertex> vertices,
         Collection<TriangleMeshFace> orderedFaces) {
      for (TriangleMeshFace face : orderedFaces) {
         Set<Vertex> faceVertices = face.getVertices();
         for (Vertex v : faceVertices) {
            if (!vertices.contains(v)) {
               return false;
            }
         }
      }
      return true;
   }

   private static Map<String, Integer> makeVertexIdentifierToIndexMap(Collection<String> identifiers) {
      Map<String, Integer> map = new HashMap<>();
      int index = 0;
      for (String vertexID : identifiers) {
         map.put(vertexID, Integer.valueOf(index));
         index++;
      }
      return map;
   }

   private void setFaces(Set<TriangleMeshFace> faces) {
      List<TriangleMeshFaceWrapper> llFaces = lowLevelFaceToHighLevelFace(new ArrayList<>(faces));
      delegate.setFaces(llFaces);
   }

   private List<TriangleMeshFaceWrapper> lowLevelFaceToHighLevelFace(List<TriangleMeshFace> faces) {
      List<TriangleMeshFaceWrapper> llFaces = new ArrayList<>();
      for (TriangleMeshFace face : faces) {
         TriangleMeshFaceWrapper newFace = lowLevelFaceToHighLevelFace(face);
         llFaces.add(newFace);
      }
      return llFaces;
   }

   private TriangleMeshFaceWrapper lowLevelFaceToHighLevelFace(TriangleMeshFace face) {
      List<Vertex> vertices = face.getVerticesWithCreationOrdering();
      List<VertexIndex> vertexIndices = new ArrayList<>();
      for (Vertex vertex : vertices) {
         int index = vertexToIndex(vertex);
         vertexIndices.add(VertexIndex.from(index));
      }

      List<Point2D> textureVertices = face.getTextureVerticesWithCreationOrdering();
      if (textureVertices.isEmpty()) {
         return TriangleMeshFaceWrapper.fromOrderedNoTexture(vertexIndices);
      }

      List<VertexIndex> textureVertexIndices = new ArrayList<>();
      for (Point2D vertex : textureVertices) {
         int index = textureVertexToIndex(vertex);
         textureVertexIndices.add(VertexIndex.from(index));
      }

      TriangleMeshFaceWrapper newFace = TriangleMeshFaceWrapper.fromOrdered(vertexIndices, textureVertexIndices);
      return newFace;
   }

   private int textureVertexToIndex(Point2D vertex) {
      return textureVertexToIndexMap.get(vertex).intValue();
   }

   private int vertexToIndex(Vertex vertex) {
      Integer index = this.vertexIdentifierToIndexMap.get(vertex.getIdentifier());
      return index.intValue();
   }

   public final TriangleMesh toTriangleMesh() {
      return delegate.toTriangleMesh();
   }

}
