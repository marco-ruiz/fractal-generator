# Fractal Generator

This project consists of:
- An abstract framework (which expects concrete transformations implementations) to compute fractals from a user specified geometric pattern.
- An implementation of shape transformations for the simplest of all shapes: lines!
- An abstract framework to update progress of an abstract task (i.e. fractal generator) using abstract workers.
- Two implementations of the update progress workers: polling (asynchronous) and threshold reached (synchronous).
- An Android application using this framework to render fractals.
- A Java Swing application using this framework to render fractals.

## Getting Started

The project contains the source code of two applications in different branches.

1. An Android application in the `android` branch.
2. A Java Swing application in the `master` branch.

### Android Application

![](README/fractal-android.gif)

The source code of the Java Swing client is on the `android` branch. Download or clone it and from within the
project folder execute the command. `mvn compile`. This will create in the `target` folder the **APK** file that you need to upload to your
android device. This file has the form `fractals-*.apk`. Once uploaded, install the **APK** and start using the application.

The objective is to define lines that will form a pattern of lines for the application to recursively apply
self-similarity to the rest of them. Draw lines on your screen (patterns) until you choose to generate the fractal
with the `Generate Fractal` option available in the application menu. Once selected the application will ask you how many recursions
you want to apply to the pattern and that's it! Also the application menu has the option `Change Color` to give you
a selection of colors to switch to in order to create more interesting patterns and fractals.

As a warning, it is better not to define too many lines in the pattern (5 should be fine), nor higher recursion levels
(no more than 7 for a 5 line pattern); as the recursion is memory and processing intensive and the application is prone
to crash if the fractal is too big.

### Swing application

![](README/fractal-swing.gif)

The source code of the Java Swing client is on the `master` branch. Once downloaded execute from within the project folder the following
commands:

```

	mvn compile
	mvn exec:java

```
That will launch the Swing application which is fairly intuitive to use, and which follows the same paradigm used with the android
application (described in the previous section).

## Framework Usage & Design

The generator framework requires concrete implementations of the shapes is going to be using with their respective transformation logic:

```java

	public interface GeometricPattern<SHAPE_TYPE> {
		public void computeConstants(SHAPE_TYPE base);
		public SHAPE_TYPE computeGeometryEquivalentTo(SHAPE_TYPE relativeBase);
	}

```


Having this interface implemented, the framework will generate the fractal using the following logic:
- The first shape of a pattern set is a **base**
- The rest of the shapes are relative transformations to be applied to said **base**.
- The generator recursively applies the same transformations to each of the other shapes.
- Cut off arguments are available to catalyze the process. The level of depth of the recursion can be control with the nuber of iterations argument and if we want only the last level of recursion, the boolean argument **onlyLastIteration** need to be set as true.

``` java

	// Generator constructor
	public GeometricPatternFractalGenerator(
		SHAPE_TYPE base,					// base shape
		List<SHAPE_TYPE> patterns,			// rel transformations to be applied to base
		int numIter,						// recursion depth
		boolean lastIterOnly,				// only gather shapes computed @ last recursion level
		Consumer<Float> progressListener	// progress listener
	){  ...  }

```


The generator computes the fractal asynchrously, updating periodically its progress through the **progressWriter** listener:

```java

	Consumer<Float> progressWriter = this::listenProgress;

	...

	public void listenProgress(float percentProgress)  {
		if (progress == IProgressUpdater.FINISHED_PROGRESS) {
			// The fractal generator has finished computing the fractal
		} else {
			// Update progress status if desired
		}
	}

```


To access the shapes computed by the generator invoke the **getFractal()** method:

```java

    List<SHAPE_TYPE> shapesGenerated = fractalGenerator.getFractal();
    shapesGenerated.stream().forEach(shape -> draw(shape));

```
