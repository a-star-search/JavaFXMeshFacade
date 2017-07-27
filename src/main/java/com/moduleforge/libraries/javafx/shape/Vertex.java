package com.moduleforge.libraries.javafx.shape;

import java.util.UUID;

import javafx.geometry.Point3D;

public class Vertex {
   
   private String identifier;
   
   private Point3D coordinates;
   
   private Vertex() {
      identifier = UUID.randomUUID().toString();
      coordinates = Point3D.ZERO;
   }

   public static Vertex fromCoordinates(Point3D coordinates) {
      Vertex v = new Vertex();
      v.coordinates = new Point3D(coordinates.getX(), coordinates.getY(), coordinates.getZ());
      return v;
   }
   
   public String getIdentifier() {
      return identifier;
   }
   
   public Point3D getCoordinates() {
      return coordinates;
   }
}
