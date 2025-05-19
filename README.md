# FINAL PROJECT : FOREST FIRE MAPPING 

The data type we have chosen is the Quadtree. A quadtree is a 2-dimensional data structure that decomposes a space in a recursive manner. Each node represents a rectangular area in a plane and has up to four child nodes. We recognize that Quadtrees are typically considered a data structure not an ADT. However, we are struggling to find an existing ADT that encapsulates the specific behaviors we hope to implement. To work around this, our ADT will be a quadtree, and the specific implementation of a quadtree, the point region quadtree, will be our data structure. We chose quadtrees because we wanted to visually represent 2D data efficiently. The organization into regions allows for faster and more targeted searches compared to methods that might have to check every element in a dataset. 

# POINT-REGION QUADTREES

PR Quadtrees recursively subdivide the regions into 4 equal-sized subregions until no region has more than one point in it (key feature of point-region quadtrees and why we are using it). A subregion with no points in it is known as an empty node, whereas a subregion with 1 item is known as a leaf node. The benefit of the leaf node capacity being 1 is that we are able to efficiently store data without collisions. The 3rd possible node is known as an internal node, and they are single nodes with 4 pointers to its respective children. Each one of them is represented by a boundary box. This box works to define the spatial dimension of the region that the node covers. Bounding boxes are useful because they help us check whether a point falls within a region. 

# OUR DATA 

Our data comes from the United States Environmental Protection Agency. This dataset allows users to examine numerous pollutants (we plan on focusing on PM2.5), years (1999-2025), geographic areas, and monitor sites. We are particularly interested in examining the California region, but are open to altering this as we move forward with this project. One thing that is nice about the dataset is that we can choose different parameters on the EPA website before we download instead of paring down larger files. The current example file that is California PM2.5 Levels in 2023 but the program is easily adaptable to other regions/years/pollutants.


# FILE ORGANIZATION 

Analysis.java - where we injest data and run analysis <br />
QuadtreeInterface.java - interface for the quadtree <br />
QuadtreeImplement.java - our implementation of quadtree that uses interface <br />
Moniter.java - Moniter class to store as points in a quadtree. Each one represents a monitor and stores historical data <br />
QuadtreeView.java - the file that creates the graphics, visual representation of quadtree, and our user interface.

# FILE ORGANIZATION 

All the code and data is in the fires folder - clone the repository into your directory.
To run make a bin, compile and run the Analysis file.
'''bash
mkdir bin
javac -d bin fires/*.java
java -cp bin fires.Analysis
'''
