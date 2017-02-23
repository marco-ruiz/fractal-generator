# Fractal Generator

This project includes a small framework to compute fractals from a geometric pattern. The first shape of a pattern sets a *base* and the rest of the shapes define the relative transformations that can be applied to said *base*. With this convention as input, the framework is capable of applying the same transformations to each of the other shapes recursively and in this way, generating the geometric fractal.

The framework is abstract enough to accept any shapes, as long as their mathematical transformations between any two shapes are explicitly defined. Currently, the project has implemented a line transformation.

The framework is self contained, so any UI technology can be built on top of it to interact with it. Currently, the project has a modest Swing client application. A D3.js UI is in the works.


Input Pattern
-
![](/README/pattern.png)


Fractal Generated
-
![](/README/fractal.png)