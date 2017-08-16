package com.moduleforge.libraries.javafx.shape;

import static com.moduleforge.libraries.geometry.GeometryUtil.calculateNormalOfPlaneGivenBy;
import static com.moduleforge.libraries.util.Util.apply;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.javatuples.Pair;

import com.google.common.base.Preconditions;
import com.moduleforge.libraries.geometry.GeometryUtil;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/**
 * Immutable class
 * 
 * @author Diego
 *
 */
public class TriangleMeshFace {

   private static final List<Point2D> DEFAULT_TEXTURE_VERTICES = Arrays.asList(new Point2D(0.0f, 0.0f),
         new Point2D(0.0f, 1.0f), new Point2D(1.0f, 0.0f));

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
    * The faces created are all connected to each other.
    * 
    * All the vertices must lay on the same plane. They must also have an ordering,
    * so that the direction of the face can be stablished. As you can suppose all
    * faces created in this way should face the same direction
    * 
    */
   public static List<TriangleMeshFace> connectedFromOrdered(List<Vertex> verticesOnPlane) {

      Preconditions.checkNotNull(verticesOnPlane);
      if (verticesOnPlane.size() < TriangleMeshFaceWrapper.TRIANGLE_VERTEX_COUNT) {
         throw new IllegalArgumentException("There should be a minimum of three vertices.");
      }
      boolean differentVertices = allVerticesAreDifferentEnough(verticesOnPlane);
      if (!differentVertices) {
         throw new IllegalArgumentException("Some of the vertices are equal or almost equal.");
      }

      List<Point3d> coordinatesOnPlane = apply(verticesOnPlane, a -> toPoint3d(a.getCoordinates()));
      if (!GeometryUtil.inSamePlane(coordinatesOnPlane)) {
         throw new IllegalArgumentException("The vertices do not lay on a plane.");
      }

      List<Vertex> verticesCopy = new ArrayList<>();
      verticesCopy.addAll(verticesOnPlane);
      List<TriangleMeshFace> faces = new ArrayList<>();
      fromOrderedRecursive(verticesCopy, faces);
      return faces;
   }

   private static void fromOrderedRecursive(List<Vertex> vertices, List<TriangleMeshFace> faces) {
      List<Vertex> nextTriangle = Arrays.asList(new Vertex[] { vertices.get(0), vertices.get(1), vertices.get(2) });
      TriangleMeshFace nextFace = fromOrderedTriplet(nextTriangle);
      faces.add(nextFace);
      boolean isTheLastTriangle = vertices.size() == TriangleMeshFaceWrapper.TRIANGLE_VERTEX_COUNT; 
      if (isTheLastTriangle) {
         return;
      }
      int indexSecondElement = 1;
      vertices.remove(indexSecondElement); // The second element only belongs to the triangle just created
      fromOrderedRecursive(vertices, faces);
   }

   /**
    * Ordered means that the counterclockwise direction of the vertices indicate
    * the side of the face.
    * 
    * To be clear, 'ordered' in the method's name is redundant because a face is
    * given by a set of vertices and the direction of the face. If the only
    * argument are the vertices, it's obvious that they must also contain the
    * order. But better redundant than confusing.
    * 
    * It doesn't make a difference in how the face will eventually be displayed on
    * a screen if the elements are cycled (ie abc vs bca or cab).
    */
   public static TriangleMeshFace fromOrderedTriplet(List<Vertex> vertices) {

      Preconditions.checkNotNull(vertices);
      if (vertices.size() != TriangleMeshFaceWrapper.TRIANGLE_VERTEX_COUNT) {
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
      if (textureVertices.size() != vertices.size()) {
         throw new IllegalArgumentException();
      }
      if (!textureVerticesWithinRange(textureVertices)) {
         throw new IllegalArgumentException();
      }
      TriangleMeshFace face = fromOrderedTriplet(vertices);
      face.textureVertices.clear();
      face.textureVertices.addAll(textureVertices);
      return face;
   }

   private static boolean textureVerticesWithinRange(List<Point2D> textureVertices) {
      for (Point2D p : textureVertices) {
         if ((p.getX() > 1) || (p.getX() < 0)) {
            return false;
         }
         if ((p.getY() > 1) || (p.getY() < 0)) {
            return false;
         }
      }
      return true;
   }

   /*
    * if the vertices are as close to each other as the floating point type allows,
    * that can bring some problems
    */
   private static boolean allVerticesAreDifferentEnough(List<Vertex> vertices) {

      for (int i = 0; i < vertices.size(); i++) {
         for (int j = i + 1; j < vertices.size(); j++) {
            Point3D outerLoopVertex = vertices.get(i).getCoordinates();
            Point3D innerLoopVertex = vertices.get(j).getCoordinates();
            if (!GeometryUtil.differentEnough(toPoint3d(outerLoopVertex), toPoint3d(innerLoopVertex))) {
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
      return fromOrderedTriplet(Arrays.asList(vertices)); // order is maintained
   }

   private void calculateFrontDirection() {
      Point3D firstPoint = verticesInOrder.get(0).getCoordinates();
      Point3D secondPoint = verticesInOrder.get(1).getCoordinates();
      Point3D thirdPoint = verticesInOrder.get(2).getCoordinates();
      Vector3d normal = calculateNormalOfPlaneGivenBy(toPoint3d(firstPoint), toPoint3d(secondPoint), toPoint3d(thirdPoint));
      frontDirectionVector = new Point3D(normal.getX(), normal.getY(), normal.getZ()); 
            
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
   public List<Vertex> getVerticesWithCreationOrdering() {
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

   private static Point3d toPoint3d(Point3D point) {
      Point3d result = new Point3d(point.getX(), point.getY(), point.getZ());
      return result;
   }

}
