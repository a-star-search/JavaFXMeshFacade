package com.moduleforge.libraries.javafx.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.moduleforge.util.GeometryUtil;

import javafx.geometry.Point3D;

public class TriangleMeshFace {
   
   /*
    * The list must be in order!!
    * 
    * In order means that the counterclockwise direction of the vertices
    * indicate the side of the face
    * 
    * The stupid name TriMesh is avoid confussion with JavaFX classes Mesh or TriangleMesh
    */
   private List<Vertex> verticesInOrder;

   /**
    * A face has a front and a back.
    * The front is indicated by a vector of direction
    * The vector goes from (0, 0, 0) to the point returned 
    * 
    * This is also called "normal" in mathematical terminology
    */
   private Point3D frontDirectionVector;

   /**
    * ordered means that the counterclockwise direction of the vertices
    * indicate the side of the face
    */
   public static TriangleMeshFace fromOrderedVertices(List<Vertex> vertices) {

      assert (vertices.size() == TriangleMeshFaceWrapper.TRIANGLE_VERTEX_COUNT);
      TriangleMeshFace face = new TriangleMeshFace();
      face.verticesInOrder = new ArrayList<>(vertices); //according to the javadoc the order is maintained
      
      face.calculateFrontDirection();
      return face;
   }

   public static TriangleMeshFace fromOrderedVertices(Vertex... vertices) {
      assert (vertices.length == TriangleMeshFaceWrapper.TRIANGLE_VERTEX_COUNT);
      return fromOrderedVertices(Arrays.asList(vertices)); //order is maintained
   }
   
   private void calculateFrontDirection() {
      Point3D firstPoint = verticesInOrder.get(0).getCoordinates();
      Point3D secondPoint = verticesInOrder.get(1).getCoordinates();
      Point3D thirdPoint = verticesInOrder.get(2).getCoordinates();
      GeometryUtil.calculateNormalVectorOfPlaneGivenByThreePoints(firstPoint, secondPoint, thirdPoint);
   }

   public List<Vertex> getOrderedVertices() {
      return Collections.unmodifiableList(verticesInOrder);
   }
   
   /**
    * A face has a front and a back.
    * The front is indicated by a vector of direction
    * The vector goes from (0, 0, 0) to the point returned 
    * 
    * The length of the vector is normalized to 1
    * 
    * Counter clockwise point order indicates vector direction
    */
   public Point3D getFrontDirectionVector() {
      return frontDirectionVector;
   }
   
}
