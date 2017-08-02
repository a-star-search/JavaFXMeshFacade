package com.moduleforge.libraries.javafx.shape;

import java.util.UUID;

import javafx.geometry.Point3D;

/**
 * This class is immutable
 */
public class Vertex {
   
   private String identifier;
   
   private Point3D coordinates;
   
   private Vertex() {
      identifier = UUID.randomUUID().toString();
      coordinates = Point3D.ZERO;
   }

   public static Vertex from(Point3D coordinates) {
      return from(coordinates.getX(), coordinates.getY(), coordinates.getZ());
   }

   public static Vertex from(double x, double y, double z) {
      Vertex v = new Vertex();
      v.coordinates = new Point3D(x, y, z);
      return v;
   }
   
   public String getIdentifier() {
      return identifier;
   }
   
   public Point3D getCoordinates() {
      return coordinates;
   }
   
   public double getX() {
      return coordinates.getX();
   }
   
   public double getY() {
      return coordinates.getY();
   }
   
   public double getZ() {
      return coordinates.getZ();
   }
   
   @Override 
   public boolean equals(Object v) {
      if (v instanceof Vertex)
         return identifier.equals(((Vertex)v).identifier);
      return false;
   }
   
   @Override 
   public int hashCode() {
      return this.identifier.hashCode();
   }
}
