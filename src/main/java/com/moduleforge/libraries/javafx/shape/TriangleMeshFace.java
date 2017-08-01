package com.moduleforge.libraries.javafx.shape;

import static com.moduleforge.util.GeometryUtil.calculateNormalVectorOfPlaneGivenBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.javatuples.Pair;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/**
 * Immutable class
 * 
 * @author Diego
 *
 */
public class TriangleMeshFace {

   /**
    * A float has about 7 decimal digits. Some developers will use float for
    * floating point numbers
    * 
    * This value is the minimum acceptable distante between two vertex
    */
   public static final float MINIMUM_VERTEX_DISTANCE = 1e-5f;

   private static final List<Point2D> DEFAULT_TEXTURE_VERTICES = Arrays.asList(new Point2D(0.0f, 0.0f), new Point2D(0.0f, 1.0f), new Point2D(1.0f, 0.0f));

   /**
    * The list must be in order
    * 
    * In order means that the counterclockwise direction of the vertices indicate
    * the side of the face
    * 
    * It doesn't really matter what element goes in which position of the list,
    * only the relative ordering. It should be understood as a circular list.
    * 
    */
   private List<Vertex> verticesInOrder;

   private Set<Pair<Vertex, Vertex>> vertexOrdering;

   /**
    * A face has a front and a back.
    * 
    * The front is indicated by a vector of direction which is perpendicular to the
    * plane in which the face lies. This is also called "normal" in mathematical
    * terminology.
    * 
    * The vector goes from (0, 0, 0) to the point returned and its length is 1
    * 
    */
   private Point3D frontDirectionVector;
   
   private List<Point2D> textureVertices;

   private TriangleMeshFace() {
      textureVertices = new ArrayList<>();
      verticesInOrder = new ArrayList<>();
   }
   
   /**
    * Ordered means that the counterclockwise direction of the vertices indicate
    * the side of the face
    * 
    * It doesn't make a difference if the elements are cycled (ie abc vs bca or
    * cab)
    */
   public static TriangleMeshFace fromOrdered(List<Vertex> vertices) {

      if(vertices.size() != TriangleMeshFaceWrapper.TRIANGLE_VERTEX_COUNT) {
         throw new IllegalArgumentException();
      }
      boolean differentVertices = allVerticesAreDifferentEnough(vertices);
      if (!differentVertices) {
         throw new IllegalArgumentException();
      }

      TriangleMeshFace face = new TriangleMeshFace();
      face.verticesInOrder = new ArrayList<>(vertices); // according to the javadoc the order is maintained
      face.textureVertices.addAll(DEFAULT_TEXTURE_VERTICES);
      face.calculateFrontDirection();
      return face;
   }

   public static TriangleMeshFace fromOrdered(List<Vertex> vertices, List<Point2D> textureVertices) {
      if(textureVertices.size() != vertices.size()) {
         throw new IllegalArgumentException();
      }
      if(!textureVerticesWithinRange(textureVertices)) {
         throw new IllegalArgumentException();
      }
      TriangleMeshFace face = fromOrdered(vertices);
      face.textureVertices.clear();
      face.textureVertices.addAll(textureVertices);
      return face;
   }

   private static boolean textureVerticesWithinRange(List<Point2D> textureVertices) {
      for (Point2D p : textureVertices) {
         if((p.getX() > 1) || (p.getX() < 0)) {
            return false;
         }
         if((p.getY() > 1) || (p.getY() < 0)) {
            return false;
         }
      }
      return true;
   }

   /*
    * if the vertices are as close to each other as the floating point type allows, that can bring some
    * problems
    */
   private static boolean allVerticesAreDifferentEnough(List<Vertex> vertices) {
      
      for (int i = 0; i < vertices.size(); i++) {
         for (int j = i + 1; j < vertices.size(); j++) {
            double firstPointX = vertices.get(i).getCoordinates().getX();
            double secondPointX = vertices.get(j).getCoordinates().getX();
            double xdelta = Math.abs(firstPointX - secondPointX);
            
            double firstPointY = vertices.get(i).getCoordinates().getY();
            double secondPointY = vertices.get(j).getCoordinates().getY();
            double ydelta = Math.abs(firstPointY - secondPointY);
            
            double firstPointZ = vertices.get(i).getCoordinates().getZ();
            double secondPointZ = vertices.get(j).getCoordinates().getZ();
            double zdelta = Math.abs(firstPointZ - secondPointZ);
            
            if(xdelta < MINIMUM_VERTEX_DISTANCE && ydelta < MINIMUM_VERTEX_DISTANCE && zdelta< MINIMUM_VERTEX_DISTANCE) {
               return false;
            }
            
         }
      }
      return true;
   }

   /**
    * Ordered means that the counterclockwise direction of the vertices indicate
    * the side of the face
    * 
    * It doesn't make a difference if the elements are cycled (ie abc vs bca or
    * cab)
    */
   public static TriangleMeshFace fromOrdered(Vertex... vertices) {
      assert (vertices.length == TriangleMeshFaceWrapper.TRIANGLE_VERTEX_COUNT);
      return fromOrdered(Arrays.asList(vertices)); // order is maintained
   }

   private void calculateFrontDirection() {
      Point3D firstPoint = verticesInOrder.get(0).getCoordinates();
      Point3D secondPoint = verticesInOrder.get(1).getCoordinates();
      Point3D thirdPoint = verticesInOrder.get(2).getCoordinates();
      frontDirectionVector = 
            calculateNormalVectorOfPlaneGivenBy(firstPoint, secondPoint, thirdPoint);
   }

   Set<Pair<Vertex, Vertex>> getVerticesOrder() {
      if (vertexOrdering != null)
         return vertexOrdering;
      vertexOrdering = new HashSet<>();
      Pair<Vertex, Vertex> pair1 = new Pair<>(verticesInOrder.get(0), verticesInOrder.get(1));
      vertexOrdering.add(pair1);
      Pair<Vertex, Vertex> pair2 = new Pair<>(verticesInOrder.get(1), verticesInOrder.get(2));
      vertexOrdering.add(pair2);
      Pair<Vertex, Vertex> pair3 = new Pair<>(verticesInOrder.get(2), verticesInOrder.get(0));
      vertexOrdering.add(pair3);
      return vertexOrdering;
   }

   /**
    * @return the list of vertices with the same ordering as when this object was
    *         created
    */
   List<Vertex> getVerticesWithCreationOrdering() {
      return Collections.unmodifiableList(verticesInOrder);
   }

   List<Point2D> getTextureVerticesWithCreationOrdering() {
      return Collections.unmodifiableList(textureVertices);
   }

   /**
    * @return the list of vertices with the same ordering as when this object was
    *         created
    */
   public Set<Vertex> getVertices() {
      return new HashSet<>(verticesInOrder);
   }

   /**
    * A face has a front and a back. The front is indicated by a vector of
    * direction The vector goes from (0, 0, 0) to the point returned
    * 
    * The length of the vector is normalized to 1
    * 
    * Counter clockwise point order indicates vector direction
    */
   public Point3D getFrontDirectionVector() {
      return frontDirectionVector;
   }

   /**
    * switch front and back faces
    * 
    * @return
    */
   public TriangleMeshFace invertFace() {
      TriangleMeshFace inverted = new TriangleMeshFace();
      double x = -frontDirectionVector.getX();
      double y = -frontDirectionVector.getY();
      double z = -frontDirectionVector.getZ();
      inverted.frontDirectionVector = new Point3D(x, y, z);
      inverted.verticesInOrder = verticesInOrder.stream().collect(Collectors.toList());
      Collections.reverse(inverted.verticesInOrder);
      return inverted;
   }

}
