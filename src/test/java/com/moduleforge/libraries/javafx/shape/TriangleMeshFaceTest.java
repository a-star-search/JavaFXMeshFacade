package com.moduleforge.libraries.javafx.shape;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

import javafx.geometry.Point3D;

public class TriangleMeshFaceTest {

   @Test(expected = IllegalArgumentException.class)
   public final void testFromOrderedListOfVertex_LessThanThreeVertices() {
      List<Vertex> list = new ArrayList<>();
      list.add(Vertex.from(Point3D.ZERO));
      list.add(Vertex.from(Point3D.ZERO));
      TriangleMeshFace.connectedFromOrdered(list);
   }

   @Test(expected = IllegalArgumentException.class)
   public final void testFromOrderedListOfVertex_PointsTooCloseToEachOther() {
      List<Vertex> list = new ArrayList<>();
      list.add(Vertex.from(Point3D.ZERO));
      list.add(Vertex.from(Point3D.ZERO));
      list.add(Vertex.from(new Point3D(100, 100, 100)));
      TriangleMeshFace.connectedFromOrdered(list);
   }

   @Test(expected = IllegalArgumentException.class)
   public final void testFromOrderedListOfVertex_FourVerticesNotInSamePlane() {
      List<Vertex> list = new ArrayList<>();
      list.add(Vertex.from(Point3D.ZERO));
      list.add(Vertex.from(new Point3D(-100, 0, 100)));
      list.add(Vertex.from(new Point3D(100, 100, 100)));
      list.add(Vertex.from(new Point3D(10, 1000, 1)));
      List<TriangleMeshFace> faces = TriangleMeshFace.connectedFromOrdered(list);
      Assert.assertTrue(faces.size() == 2);
   }

   @Test
   public final void testFromOrderedListOfVertex_FourVerticesResultInTwoFaces() {

      List<Vertex> list = new ArrayList<>();
      
      double a = 3, b = -2, c = 5, d = -1;
      
      list.add(Vertex.from(new Point3D(10, 0, getZOfPlane(10, 0, a, b, c, d))));
      list.add(Vertex.from(new Point3D(0, 10, getZOfPlane(0, 10, a, b, c, d))));
      list.add(Vertex.from(new Point3D(10, 10, getZOfPlane(10, 10, a, b, c, d))));
      list.add(Vertex.from(new Point3D(0, -10, getZOfPlane(0, -10, a, b, c, d))));
      
      List<TriangleMeshFace> faces = TriangleMeshFace.connectedFromOrdered(list);
      Assert.assertTrue(faces.size() == 2);
   }
   
//   @Test
   public final void testFromOrderedListOfVertex_FiveVerticesResultInThreeFaces() {
      List<Vertex> list = new ArrayList<>();
      
      double a = 3, b = -2, c = 5, d = -1;
      
      list.add(Vertex.from(new Point3D(10, 0, getZOfPlane(10, 0, a, b, c, d))));
      list.add(Vertex.from(new Point3D(0, 10, getZOfPlane(0, 10, a, b, c, d))));
      list.add(Vertex.from(new Point3D(5, 30, getZOfPlane(5, 30, a, b, c, d))));
      list.add(Vertex.from(new Point3D(0, -10, getZOfPlane(0, -10, a, b, c, d))));
      list.add(Vertex.from(new Point3D(-20, -10, getZOfPlane(-20, -10, a, b, c, d))));
      
      List<TriangleMeshFace> faces = TriangleMeshFace.connectedFromOrdered(list);
      Assert.assertTrue(faces.size() == 3);
   }

   
   // ax + by + cz + d = 0
   //z = -ax/c - by/c - d/c
   public static double getZOfPlane(double x, double y, double a, double b, double c, double d) {
      return (-a*x/c) - (b*y/c) - (d/c);
   }
   
}
