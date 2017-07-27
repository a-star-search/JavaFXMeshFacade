package com.moduleforge.util;

import org.junit.Assert;
import org.junit.Test;

import javafx.geometry.Point3D;

/**
 * I used wolfram alpha for all or most of these tests
 *
 */
public class GeometryUtilTest {

   private static final double DELTA_SQUARED = 0.01f * 0.01f;

   @Test
   public void testCalculateNormalVectorOfPlaneGivenByThreePoints_LengthIsOne() {

      Point3D pointA = new Point3D(4, -7, 9);
      Point3D pointB = new Point3D(-2, -2, 3);
      Point3D pointC = new Point3D(65, 2, 0);

      Point3D normalVector = GeometryUtil.calculateNormalVectorOfPlaneGivenByThreePoints(pointA, pointB, pointC);

      double x = normalVector.getX();
      double y = normalVector.getY();
      double z = normalVector.getZ();
      
      double length = Math.sqrt(x*x+y*y+z*z);
      double delta = 1.0 - length;
      
      Assert.assertTrue(delta * delta < DELTA_SQUARED);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCalculateNormalVectorOfPlaneGivenByThreePoints_WhenTwoPointsAreEqual() {

      Point3D pointA = new Point3D(1, -7, 4);
      Point3D pointB = new Point3D(-1, 2, 3);
      Point3D pointC = new Point3D(-1, 2, 3);

      GeometryUtil.calculateNormalVectorOfPlaneGivenByThreePoints(pointA, pointB, pointC);
   }

   @Test
   public void testCalculateNormalVectorOfPlaneGivenByThreePoints_WhenTwoPointsAreSlightlyDifferent() {

      Point3D pointA = new Point3D(1, -7, 4);
      Point3D pointB = new Point3D(-1, 2, 3);
      Point3D pointC = new Point3D(-1, 2, (3.0+1e-6));

      GeometryUtil.calculateNormalVectorOfPlaneGivenByThreePoints(pointA, pointB, pointC);
      
      return; //passes successfully with no exception
   }

   /*
    * use wolphram alpha like this
    * 
    * https://www.wolframalpha.com/input/?i=norm+%7B12,+-5,+4%7D
    */
   @Test
   public void testCalculateVectorMagnitude_ForRandomVector() {

      Point3D pointA = new Point3D(12, -5, 4);

      double expectedMagnitudePointA = 13.601f; // as per wolfram alpha
      double magnitudePointA = GeometryUtil.calculateVectorMagnitude(pointA);

      double deltaPointA = expectedMagnitudePointA - magnitudePointA;

      deltaPointA = deltaPointA * deltaPointA;

      Assert.assertTrue(deltaPointA < DELTA_SQUARED);
   }

   @Test
   public void testCalculateVectorMagnitude_ForSubUnitVector() {

      Point3D pointA = new Point3D(0.1, 0.3, -0.5);

      double expectedMagnitudePointA = 0.5916f; // as per wolfram alpha
      double magnitudePointA = GeometryUtil.calculateVectorMagnitude(pointA);

      double deltaPointA = expectedMagnitudePointA - magnitudePointA;

      deltaPointA = deltaPointA * deltaPointA;

      Assert.assertTrue(deltaPointA < DELTA_SQUARED);
   }

   @Test
   public void testCrossProduct() {

      Point3D vectorA = new Point3D(1.0 / 4, -1.0 / 2, 1.0);
      Point3D vectorB = new Point3D(1.0 / 3, 1.0, -2.0 / 3);

      Point3D product = GeometryUtil.crossProduct(vectorA, vectorB);
      Point3D expectedProduct = new Point3D(-2.0 / 3, 1.0 / 2, 5.0 / 12);

      double deltaProductX = product.getX() - expectedProduct.getX();
      double deltaProductY = product.getY() - expectedProduct.getY();
      double deltaProductZ = product.getZ() - expectedProduct.getZ();

      deltaProductX = deltaProductX * deltaProductX;
      deltaProductY = deltaProductY * deltaProductY;
      deltaProductZ = deltaProductZ * deltaProductZ;

      Assert.assertTrue(deltaProductX < DELTA_SQUARED);
      Assert.assertTrue(deltaProductY < DELTA_SQUARED);
      Assert.assertTrue(deltaProductZ < DELTA_SQUARED);
   }

}
