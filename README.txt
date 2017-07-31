
The Mesh, MeshView and TriangleMesh class interfaces in the JavaFX library are less than ideally designed. These interfaces are complex, error-prone and not readable.

The way to create a mesh involves calling a method with all points (vertices) as parameters, then calling another method with all points of all faces as parameters. 
The faces are not the points passed before, but the indeces given by the order in which the points were passed as parameters. Furthermore, the texture coordinates are 
also passed mixed with the geometrical vertex indices.

In this facade library, the mesh is created with a collection of vertices and that's the only way it can be created (ie. not empty). 

Then the faces should be set. There is no need for the user to keep track of any vertex index when setting them up, they can just pass the vertices themselves. If you
prefer to use indices instead of variables, that choice is still yours by using a list.

A face can calculate its own normal. Also a face can be constructed with three unordered points and a normal array if you find it easier.

The library also allows to set other safeguards, such as the minimum distance between two vertices, which can matter when they are two close and can't be reliable used 
to calculate the face.

Hopefully this facade interface is more intutive and usable.
