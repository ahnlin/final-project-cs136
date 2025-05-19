# FINAL PROJECT : FOREST FIRE MAPPING 

The data type we have chosen is the Quadtree. A quadtree is a 2-dimensional data structure that decomposes a space recursively. Each node represents a rectangular area in a plane and has up to four child nodes. We recognize that Quadtrees are typically considered a data structure, not an ADT. However, we are struggling to find an existing ADT that encapsulates the specific behaviors we hope to implement. To work around this, our ADT will be a quadtree, and our particular implementation of a quadtree, the point region quadtree, will be our data structure. We chose quadtrees because we wanted to visually represent 2D data efficiently. The organization into regions allows for faster and more targeted searches compared to using a data structure where we might have to check every element in a dataset. 

# POINT-REGION QUADTREES

PR Quadtrees recursively subdivide the regions into 4 equal-sized subregions until no region has more than one point in it. This is a key feature of point-region quadtrees and why we are using it. A subregion with no points in it is known as an empty node, whereas a subregion with 1 item is known as a leaf node. The benefit of the leaf node capacity being 1 is that we are able to efficiently store data without collisions. The 3rd possible node is known as an internal node. They each have 4 pointers to their respective children. Each internal node is represented by a boundary box. This box works to define the spatial dimension of the region that the node covers. Bounding boxes are useful because they help us check whether a point falls within a region. 

# OUR DATA 

Our data comes from the United States Environmental Protection Agency. This dataset allows users to examine numerous pollutants (we plan on focusing on PM2.5, as wildfires are significant source of PM2.5 pollution), years (1999-2025), geographic areas, and monitor sites. We are particularly interested in examining the California region, but are open to altering this as we continue developing this project. This dataset allows us to choose different parameters on the EPA website before we download instead of paring down larger files, making it much easier to use. The current example file is California PM2.5 Levels in 2023, but our program is easily adaptable to other regions/years/pollutants.


# FILE ORGANIZATION 

Analysis.java - where we injest data and run analysis <br />
QuadtreeInterface.java - interface for the quadtree <br />
QuadtreeImplement.java - our implementation of quadtree that uses interface <br />
Moniter.java - Moniter class to store as points in a quadtree. Each one represents a monitor and stores historical data <br />
QuadtreeView.java - the file that creates the graphics, visual representation of quadtree, and our user interface

# USING OUR CODE

All the code and data are in the fires folder - clone the repository into your directory.
To run, make a bin, compile, and run the Analysis file.
```bash
mkdir bin
```
```bash
javac -d bin fires/*.java
```
```bash
java -cp bin fires.Analysis
```
If this is done correctly, a window should pop up with a visualization of the California PM2.5 Levels. Move the slider to see the different days, and press the button to switch between reading mode and extrapolation mode. Reading mode fills each leaf node with a color representing each moniters smoke levels. Extrapolate mode uses the get method to average surrounding leaf nodes' data. This node's accuracy could definitely be improved upon, but a trial version is currently implemented. <br />

All levels are colored according to IQ Airs air quality chart:<br />

![image](https://github.com/user-attachments/assets/50354bfd-b1f0-464d-bbcd-0126ce295d99)

