package com.moduleforge.libraries.javafx.shape;

import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

import com.google.common.annotations.VisibleForTesting;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;

/*
 * 
 * This class is a wrapper of TriangleMesh, whose interface is too complex.
 * 
 * I do not to extend TriangleMesh so that I can control
 * what methods to expose
 * 
 * Interestingly enough I cannot extend Mesh, which would have been
 * prettier.
 * 
 * 
 */
class TriangleMeshWrapper {

   private TriangleMesh _delegate;

   private TriangleMeshWrapper() {
      _delegate = new TriangleMesh();
      applyDefaultTexture();
   }

   private TriangleMeshWrapper(TriangleMesh delegate) {
      this();
      _delegate = delegate;
   }

   /*
    * the order of the vertices is important because this order is saved as an
    * index per vertex that is later used when setting up the faces
    */
   static final TriangleMeshWrapper fromOrdered(List<Point3D> vertices) {
      TriangleMeshWrapper mesh = new TriangleMeshWrapper();
      mesh.setVertices(vertices);
      return mesh;
   }

   static final TriangleMeshWrapper fromOrdered(List<Point3D> vertices, List<Point2D> textureVertices) {
      TriangleMeshWrapper mesh = new TriangleMeshWrapper();
      mesh.setVertices(vertices);
      mesh.setTextureVertices(textureVertices);
      return mesh;
   }

   /*
    * the order of the vertices is important because this order is saved as an
    * index per vertex that is later used when setting up the faces
    */
   static final TriangleMeshWrapper fromOrdered(Point3D... vertices) {
      TriangleMeshWrapper mesh = new TriangleMeshWrapper();
      mesh.setVertices(vertices);
      return mesh;
   }

   private void setTextureVertices(List<Point2D> vertices) {
      float[] elements = new float[vertices.size() * 2];
      int index = 0;
      for (Point2D point : vertices) {
         elements[index] = (float) point.getX();
         index++;
         elements[index] = (float) point.getY();
         index++;
      }
      _delegate.getTexCoords().clear();
      _delegate.getTexCoords().addAll(elements);
   }

   /*
    * the order of the faces is important
    */
   void setFaces(List<TriangleMeshFaceWrapper> faces) {
      int[] array = makeCoordinateArrayFrom(faces);
      _delegate.getFaces().addAll(array);
   }

   private void setVertices(List<Point3D> vertices) {
      float[] elements = new float[vertices.size() * 3];
      int index = 0;
      for (Point3D point : vertices) {
         elements[index] = (float) point.getX();
         index++;
         elements[index] = (float) point.getY();
         index++;
         elements[index] = (float) point.getZ();
         index++;
      }
      _delegate.getPoints().addAll(elements);
   }

   private void setVertices(Point3D... vertices) {
      setVertices(Arrays.asList(vertices));
   }

   @VisibleForTesting
   static int[] makeCoordinateArrayFrom(List<TriangleMeshFaceWrapper> faces) {
      int arraySize = TriangleMeshFaceWrapper.faceTotalCoordinateCount() * faces.size();
      int index = 0;
      int[] array = new int[arraySize];
      for (TriangleMeshFaceWrapper face : faces) {
         for (Pair<VertexIndex, VertexIndex> vertex : face.getVertexIndexPairs()) {
            array[index] = vertex.getValue0().value;
            index++;
            array[index] = vertex.getValue1().value;
            index++;
         }
      }
      return array;
   }

   private void applyDefaultTexture() {
      _delegate.getTexCoords().addAll(0, 0);
   }

   public final TriangleMesh toTriangleMesh() {
      return this._delegate;
   }
}
