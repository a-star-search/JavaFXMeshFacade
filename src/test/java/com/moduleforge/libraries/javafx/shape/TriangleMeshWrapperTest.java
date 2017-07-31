package com.moduleforge.libraries.javafx.shape;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;

public class TriangleMeshWrapperTest {

   @Test
   @SuppressWarnings("unchecked")
   public void testGetCoordinateArrayFromTriangleMeshFaces() {
      
      int[] expectedCoordinates = {
                     0, 0, 2, 0, 1, 0, // Front left face
                     0, 0, 1, 0, 3, 0, // Front right face
                     0, 0, 3, 0, 4, 0, // Back right face
                     0, 0, 4, 0, 2, 0, // Back left face
                     4, 0, 1, 0, 2, 0, // Bottom rear face
                     4, 0, 3, 0, 1, 0}; // Bottom front face
      List<TriangleMeshFaceWrapper> faces = new ArrayList<>();
      
      Pair<VertexIndex, VertexIndex>[] frontLeftFaceVertices = new Pair[3];
      frontLeftFaceVertices[0] = Pair.with(VertexIndex.from(0), VertexIndex.from(0));
      frontLeftFaceVertices[1] = Pair.with(VertexIndex.from(2), VertexIndex.from(0));
      frontLeftFaceVertices[2] = Pair.with(VertexIndex.from(1), VertexIndex.from(0));
      TriangleMeshFaceWrapper frontLeftFace = TriangleMeshFaceWrapper.fromOrdered(frontLeftFaceVertices);
      

      Pair<VertexIndex, VertexIndex>[] frontRightFaceVertices = new Pair[3];
      frontRightFaceVertices[0] = Pair.with(VertexIndex.from(0), VertexIndex.from(0));
      frontRightFaceVertices[1] = Pair.with(VertexIndex.from(1), VertexIndex.from(0));
      frontRightFaceVertices[2] = Pair.with(VertexIndex.from(3), VertexIndex.from(0));
      TriangleMeshFaceWrapper frontRightFace = TriangleMeshFaceWrapper.fromOrdered(frontRightFaceVertices);

      Pair<VertexIndex, VertexIndex>[] backRightFaceVertices = new Pair[3];
      backRightFaceVertices[0] = Pair.with(VertexIndex.from(0), VertexIndex.from(0));
      backRightFaceVertices[1] = Pair.with(VertexIndex.from(3), VertexIndex.from(0));
      backRightFaceVertices[2] = Pair.with(VertexIndex.from(4), VertexIndex.from(0));
      TriangleMeshFaceWrapper backRightFace = TriangleMeshFaceWrapper.fromOrdered(backRightFaceVertices);

      Pair<VertexIndex, VertexIndex>[] backLeftFaceVertices = new Pair[3];
      backLeftFaceVertices[0] = Pair.with(VertexIndex.from(0), VertexIndex.from(0));
      backLeftFaceVertices[1] = Pair.with(VertexIndex.from(4), VertexIndex.from(0));
      backLeftFaceVertices[2] = Pair.with(VertexIndex.from(2), VertexIndex.from(0));
      TriangleMeshFaceWrapper backLeftFace = TriangleMeshFaceWrapper.fromOrdered(backLeftFaceVertices);

      Pair<VertexIndex, VertexIndex>[] bottomRearFaceVertices = new Pair[3];
      bottomRearFaceVertices[0] = Pair.with(VertexIndex.from(4), VertexIndex.from(0));
      bottomRearFaceVertices[1] = Pair.with(VertexIndex.from(1), VertexIndex.from(0));
      bottomRearFaceVertices[2] = Pair.with(VertexIndex.from(2), VertexIndex.from(0));
      TriangleMeshFaceWrapper bottomRearFace = TriangleMeshFaceWrapper.fromOrdered(bottomRearFaceVertices);

      Pair<VertexIndex, VertexIndex>[] bottomFrontFaceVertices = new Pair[3];
      bottomFrontFaceVertices[0] = Pair.with(VertexIndex.from(4), VertexIndex.from(0));
      bottomFrontFaceVertices[1] = Pair.with(VertexIndex.from(3), VertexIndex.from(0));
      bottomFrontFaceVertices[2] = Pair.with(VertexIndex.from(1), VertexIndex.from(0));
      TriangleMeshFaceWrapper bottomFrontFace = TriangleMeshFaceWrapper.fromOrdered(bottomFrontFaceVertices);

      faces.add(frontLeftFace);
      faces.add(frontRightFace);
      faces.add(backRightFace);
      faces.add(backLeftFace);
      faces.add(bottomRearFace);
      faces.add(bottomFrontFace);
      int[] coordinates = TriangleMeshWrapper.makeCoordinateArrayFrom(faces);
      
      Assert.assertArrayEquals( expectedCoordinates, coordinates );
      
   }

}
