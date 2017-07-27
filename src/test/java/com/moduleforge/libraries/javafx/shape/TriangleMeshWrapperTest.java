package com.moduleforge.libraries.javafx.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class TriangleMeshWrapperTest {

   @Test
   public void testGetCoordinateArrayFromTriangleMeshFaces() {
      
      int[] expectedCoordinates = {
                     0, 0, 2, 0, 1, 0, // Front left face
                     0, 0, 1, 0, 3, 0, // Front right face
                     0, 0, 3, 0, 4, 0, // Back right face
                     0, 0, 4, 0, 2, 0, // Back left face
                     4, 0, 1, 0, 2, 0, // Bottom rear face
                     4, 0, 3, 0, 1, 0}; // Bottom front face
      List<TriangleMeshFaceWrapper> faces = new ArrayList<>();
      
      VertexIndexPair[] frontLeftFaceVertices = new VertexIndexPair[3];
      frontLeftFaceVertices[0] = new VertexIndexPair(VertexIndex.from(0), VertexIndex.from(0));
      frontLeftFaceVertices[1] = new VertexIndexPair(VertexIndex.from(2), VertexIndex.from(0));
      frontLeftFaceVertices[2] = new VertexIndexPair(VertexIndex.from(1), VertexIndex.from(0));
      TriangleMeshFaceWrapper frontLeftFace = TriangleMeshFaceWrapper.fromOrderedVertices(frontLeftFaceVertices);
      

      VertexIndexPair[] frontRightFaceVertices = new VertexIndexPair[3];
      frontRightFaceVertices[0] = new VertexIndexPair(VertexIndex.from(0), VertexIndex.from(0));
      frontRightFaceVertices[1] = new VertexIndexPair(VertexIndex.from(1), VertexIndex.from(0));
      frontRightFaceVertices[2] = new VertexIndexPair(VertexIndex.from(3), VertexIndex.from(0));
      TriangleMeshFaceWrapper frontRightFace = TriangleMeshFaceWrapper.fromOrderedVertices(frontRightFaceVertices);

      VertexIndexPair[] backRightFaceVertices = new VertexIndexPair[3];
      backRightFaceVertices[0] = new VertexIndexPair(VertexIndex.from(0), VertexIndex.from(0));
      backRightFaceVertices[1] = new VertexIndexPair(VertexIndex.from(3), VertexIndex.from(0));
      backRightFaceVertices[2] = new VertexIndexPair(VertexIndex.from(4), VertexIndex.from(0));
      TriangleMeshFaceWrapper backRightFace = TriangleMeshFaceWrapper.fromOrderedVertices(backRightFaceVertices);

      VertexIndexPair[] backLeftFaceVertices = new VertexIndexPair[3];
      backLeftFaceVertices[0] = new VertexIndexPair(VertexIndex.from(0), VertexIndex.from(0));
      backLeftFaceVertices[1] = new VertexIndexPair(VertexIndex.from(4), VertexIndex.from(0));
      backLeftFaceVertices[2] = new VertexIndexPair(VertexIndex.from(2), VertexIndex.from(0));
      TriangleMeshFaceWrapper backLeftFace = TriangleMeshFaceWrapper.fromOrderedVertices(backLeftFaceVertices);

      VertexIndexPair[] bottomRearFaceVertices = new VertexIndexPair[3];
      bottomRearFaceVertices[0] = new VertexIndexPair(VertexIndex.from(4), VertexIndex.from(0));
      bottomRearFaceVertices[1] = new VertexIndexPair(VertexIndex.from(1), VertexIndex.from(0));
      bottomRearFaceVertices[2] = new VertexIndexPair(VertexIndex.from(2), VertexIndex.from(0));
      TriangleMeshFaceWrapper bottomRearFace = TriangleMeshFaceWrapper.fromOrderedVertices(bottomRearFaceVertices);

      VertexIndexPair[] bottomFrontFaceVertices = new VertexIndexPair[3];
      bottomFrontFaceVertices[0] = new VertexIndexPair(VertexIndex.from(4), VertexIndex.from(0));
      bottomFrontFaceVertices[1] = new VertexIndexPair(VertexIndex.from(3), VertexIndex.from(0));
      bottomFrontFaceVertices[2] = new VertexIndexPair(VertexIndex.from(1), VertexIndex.from(0));
      TriangleMeshFaceWrapper bottomFrontFace = TriangleMeshFaceWrapper.fromOrderedVertices(bottomFrontFaceVertices);

      faces.add(frontLeftFace);
      faces.add(frontRightFace);
      faces.add(backRightFace);
      faces.add(backLeftFace);
      faces.add(bottomRearFace);
      faces.add(bottomFrontFace);
      int[] coordinates = TriangleMeshWrapper.makeCoordinateArrayFromTriangleMeshFaces(faces);
      
      System.out.println(Arrays.toString(coordinates));
      System.out.println(Arrays.toString(expectedCoordinates));
      
      Assert.assertArrayEquals( expectedCoordinates, coordinates );
      
   }

}
