package com.moduleforge.util;

import com.google.common.annotations.VisibleForTesting;

import javafx.geometry.Point3D;

public class GeometryUtil {

   /**
    * Given three points it returns a vector that is normal to the plane that
    * contains those points.
    * 
    * IMPORTANT CONSIDERATIONS:
    * 
    * 1) The value returned is a three dimensional point and the vector should be
    * understood to go from (0, 0, 0) to the point returned
    * 
    * 2) The distance of the point returned to (0, 0, 0) is exactly 1
    * 
    * 3) The direction of the vector is such that it is pointing in the viewer's
    * direction, the points passed as parameters are given in a counter clockwise
    * order
    * 
    */
   public static Point3D calculateNormalVectorOfPlaneGivenBy(Point3D first, Point3D second, Point3D third) {

      boolean twoOrMorePointsAreEqual = pointsAreEqual(first, second);
      twoOrMorePointsAreEqual |= pointsAreEqual(second, third);
      
      if(twoOrMorePointsAreEqual)
         throw new IllegalArgumentException("At least two of the points were equal");
      
      
      Point3D vector1 = calculateVector(first, second);
      Point3D vector2 = calculateVector(first, third);

      Point3D normalVector = crossProduct(vector1, vector2);
      double vectorMagnitude = calculateVectorMagnitude(normalVector);
      Point3D unitNormalVector = new Point3D(
            normalVector.getX() / vectorMagnitude,
            normalVector.getY() / vectorMagnitude,
            normalVector.getZ() / vectorMagnitude );
      return unitNormalVector;
   }

   /*
    * better create my own method for this, 
    * I don't trust javafx 3d api with much 
    */
   private static boolean pointsAreEqual(Point3D first, Point3D second) {
      double smallestAcceptableDelta = 1e-8; // this is somewhat arbitrary
      double deltaX = first.getX() - second.getX();
      double deltaY = first.getY() - second.getY();
      double deltaZ = first.getZ() - second.getZ();
      
      return ((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ)) < (smallestAcceptableDelta * smallestAcceptableDelta); 
   }

   @VisibleForTesting
   static double calculateVectorMagnitude(Point3D normalVector) {
      double x = normalVector.getX();
      double y = normalVector.getY();
      double z = normalVector.getZ();

      double magnitude = Math.sqrt(x * x + y * y + z * z);
      return magnitude;
   }

   /**
    * We use the formula below for the cross product of two vectors
    * 
    * https://en.wikipedia.org/wiki/Cross_product#Matrix_notation
    * 
    */
   @VisibleForTesting
   static Point3D crossProduct(Point3D first, Point3D second) {
      double ux = first.getX();
      double uy = first.getY();
      double uz = first.getZ();

      double vx = second.getX();
      double vy = second.getY();
      double vz = second.getZ();

      double w1 = uy * vz - uz * vy;
      double w2 = vx * uz - vz * ux;
      double w3 = ux * vy - uy * vx;
      
      return new Point3D(w1, w2, w3);
   }

   /**
    * Returns a vector that goes from (0, 0, 0) to the point returned
    */
   @VisibleForTesting
   static Point3D calculateVector(Point3D first, Point3D second) {
      double x = second.getX() - first.getX();
      double y = second.getY() - first.getY();
      double z = second.getZ() - first.getZ();
      return new Point3D(x, y, z);
   }

}
