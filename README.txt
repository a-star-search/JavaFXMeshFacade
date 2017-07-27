
The Mesh, MeshView and TriangleMesh class interfaces in the JavaFX library are less than ideally designed.

The public interfaces are complex and confusing.

The way to create a mesh involves calling a method with all points (vertices) as parameters, then calling another method with all points of all faces as parameters. The faces are not the points passed before, but the indeces given by the order in which the points were passed as parameters. Furthermore, the texture coordinates are also passed.

Thus, the construction of a TriangleMesh is too error-prone.

In this facade library, the mesh is created with a collection of vertices and that's the only way it can be created ie. not empty). 

Then the faces should be set. There is no need for the user to keep track of any vertex index when setting them up, they can just pass the vertices themselves. 

A vertex is not defined only by its coordinates, but also includes a unique id that is automatically set on creation.

A face can calculate its own normal. Also a face can be constructed with three unordered points and a normal array.

Hopefully this facade interface is much more intutive and usable.
