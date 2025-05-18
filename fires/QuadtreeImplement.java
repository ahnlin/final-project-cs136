package fires;
import java.util.*; 
import java.io.*;  

public class QuadtreeImplement<Item> implements QuadtreeInterface<Item>{

// we are using a point-region quadtree 
// instance variables 
	public int leaves; 
	public InternalNode root; 
	public int internal; 

// constructor
public QuadtreeImplement(double maxx, double maxy, double minx, double miny) {
	this.leaves = 0; 
	this.root = new InternalNode(null, maxx, maxy, minx, miny); 
	internal = 1; 
}

public QuadtreeImplement(BoundingBox box1) {
	this.leaves = 0; 
	this.root = new InternalNode(null, box1); 
	internal = 1;
}

// parent is used for both internal, leafnode, and empty nodes
public class Node{ 
	public Node parent; 
}


// leaves that hold data corresponding to a specific point (no children)
public class LeafNode extends Node{ 
	public Item data; 
	public double xcord; 
	public double ycord; 

	public LeafNode(Node parent, Item data, double x, double y) {
		this.parent = parent; 
		this.data = data; 
		xcord = x; 
		ycord = y; 
	}

	public String toString() {
		return data + ": (" + xcord + ", " + ycord + ")"; 
	}
}
// internal node has 4 child nodes 
public class InternalNode extends Node {
	public Node northwest; 
	public Node northeast; 
	public Node southwest; 
	public Node southeast; 
	public BoundingBox box; 

		// internal node constructor 
		public InternalNode(Node parent, double maxx, double maxy, double minx, double miny) {
			this.parent = parent; 
			this.box = new BoundingBox(maxx, maxy, minx, miny); 

			this.northwest = new EmptyNode(this);
			this.northeast = new EmptyNode(this);
			this.southwest = new EmptyNode(this);
			this.southeast = new EmptyNode(this); 
		}

		public InternalNode(Node parent, BoundingBox box1) {
			this.parent = parent; 
			northwest = new EmptyNode(parent);
			northeast = new EmptyNode(parent);
			southwest = new EmptyNode(parent);
			southeast = new EmptyNode(parent);
			this.box = box1; 
		}

		public String toString() {
			return "(" + northeast.toString() + ", " + northwest.toString() + ", " + southeast.toString() + ", " + southwest.toString() + ")"; 
		}
}
//empty nodes (no point) 
public class EmptyNode extends Node {
	public EmptyNode(Node parent) {
		this.parent = parent; 
	}

	public String toString() {
		return "empty node";
	}
}

// returns true if the quadtree is empty
public boolean isEmpty() {
	return leaves == 0; 
}

// returns the number of non-empty nodes
public int size() {
	return leaves; 
}

public void insert(Item obj, double xcord, double ycord) {
	// check if in root boudning box 
	if (!root.box.contains(xcord, ycord)) {
		return; 
	}

	root = (InternalNode) insertHelper((Node) root, null, obj, root.box, xcord, ycord);
	leaves++;
	return;
}

public Node insertHelper(Node current, Node prev, Item obj, BoundingBox box, double xcord, double ycord) {
	// base case if current is empty node  
	if (current instanceof QuadtreeImplement.EmptyNode) {
		LeafNode discovered = new LeafNode(prev, obj, xcord, ycord); 
		return discovered; 
	}
	// check if at internal node if so subdivide necessary boxes (recursive call)
	else if (current instanceof QuadtreeImplement.InternalNode) {
		InternalNode region = (InternalNode) current; 

		if (region.box.northwestbox().contains(xcord, ycord)) {
			Node child = insertHelper(region.northwest, current, obj, box.northwestbox(), xcord, ycord); 
			child.parent = region; 
			region.northwest = child; 
		}

		else if (region.box.northeastbox().contains(xcord, ycord)) {
			Node child = insertHelper(region.northeast, current, obj, box.northeastbox(), xcord, ycord);
			child.parent = region; 
			region.northeast = child; 
		}

		else if (region.box.southwestbox().contains(xcord, ycord)) {
			Node child = insertHelper(region.southwest, current, obj, box.southwestbox(), xcord, ycord);
			child.parent = region; 
			region.southwest = child; 
		}

		else if(region.box.southeastbox().contains(xcord, ycord)) {
			Node child = insertHelper(region.southeast, current, obj, box.southeastbox(), xcord, ycord);
			child.parent = region; 
			region.southeast = child; 
		}

		else {
			System.out.println("Big issue!"); 
		}

		return region; 
	}
	// if we have reached a leafnode we have to make it an internal node and subdivide it 
	else if (current instanceof QuadtreeImplement.LeafNode) {
		LeafNode old = (LeafNode) current; 

		if (xcord == old.xcord && ycord == old.ycord) {
			old.data = obj; 
			old.xcord = xcord; 
			old.ycord = ycord; 
		}
		
		else {
			InternalNode newIN = new InternalNode(prev, box); 
			internal++; 

			// old obj into internal 
			insertHelper(newIN, newIN, old.data,box, old.xcord, old.ycord);
			
			// new obj into internal 
			insertHelper(newIN, newIN,obj,box, xcord, ycord);

			return newIN;

		}
		
	}
	System.out.println("Another big issue!"); 
	return null; 
}

public List<LeafNode> get(double xval, double yval) {
	// list of leaf nodes where i will compile leafnodes found 
	List<LeafNode> compiled = new ArrayList<>(); 
	
	// starting at the root node, traverse through the quadtree and examine the internal nodes by sorting using the points 
	Node current = root; 
	while (current instanceof QuadtreeImplement.InternalNode) {
		InternalNode internal = (InternalNode) current; 
		BoundingBox box = internal.box;
		if (internal.box.northwestbox().contains(xval, yval)) {
			current = internal.northwest;
		} else if (internal.box.northeastbox().contains(xval, yval)) {
			current = internal.northeast;
		} else if (internal.box.southwestbox().contains(xval, yval)) {
			current = internal.southwest;
		} else if (internal.box.southeastbox().contains(xval, yval)) {
			current = internal.southeast;
		}
		else {
			System.out.println("issue!"); 
			return compiled; 
		}
	}
	// once we've located the specific region it is in we can see if it is a leaf or empty node

		// if the current node is a leaf node check if it has this specifc x and y cord 
		if (current instanceof QuadtreeImplement.LeafNode) {
			LeafNode found = (LeafNode) current; 
			double EPSILON = 1e-6;

		if (Math.abs(found.xcord - xval) < EPSILON && Math.abs(found.ycord - yval) < EPSILON) {
    		compiled.add(found);
		}
		}
		// if not a leaf node it must be an empty node so look at the siblings ndoes 
		else if (current instanceof QuadtreeImplement.EmptyNode) {
			// get the parent node (internal node) and access each region (nw, ne, sw, se)
			if (current.parent == null) {
       			System.out.println("Parent is null - issue.");
       		}
			Node parent = current.parent; 
			if (parent instanceof QuadtreeImplement.InternalNode) {
				System.out.println("Parent is InternalNode — checking siblings");
				InternalNode internal1 = (InternalNode) parent; 
				// use check leaf to know if any of them are leaf nodes
        		if (internal1.northwest != current) {
        			checkLeaf(internal1.northwest, compiled); 
        		}
        		if (internal1.northeast != current) {
				    checkLeaf(internal1.northeast, compiled); 

        		}
        		if (internal1.southwest != current) {
               		checkLeaf(internal1.southwest, compiled); 
        		}
        		if (internal1.southeast != current) {
        			checkLeaf(internal1.southeast, compiled); 
        		}
			}
		}
	// return the list of leafnodes found 
	return compiled; 
}
// created for efficiency, check if given node is a leafnode or if leaf nodes exist inside of them 
public void checkLeaf(Node node, List<LeafNode> compiled) {
	if (node instanceof QuadtreeImplement.LeafNode) {
    	LeafNode found = (LeafNode) node; 
        compiled.add(found);
    }
    else if (node instanceof QuadtreeImplement.InternalNode) {
    	InternalNode internal = (InternalNode) node; 
    	checkLeaf(internal.northwest, compiled);
        checkLeaf(internal.northeast, compiled);
        checkLeaf(internal.southwest, compiled);
        checkLeaf(internal.southeast, compiled);
    }
}

public static void main(String[] args) {
	    QuadtreeImplement quadtree = new QuadtreeImplement<Integer>(128.0, 128.0, 0.0, 0.0);

        // Insert some points
    quadtree.insert(1, 32.0, 96.0);   
	quadtree.insert(2, 96.0, 96.0);
	quadtree.insert(3, 112.0, 32.0);

    

    System.out.println("Querying for (96.0, 16.0):");
    System.out.println(quadtree.get(32.0, 32.0)); 

        // Insert outside bounding box
        System.out.println(quadtree.root.toString()); 

}
}
