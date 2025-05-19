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
	public BoundingBox box;

	public LeafNode(Node parent, Item data, double x, double y,BoundingBox box) {
		this.parent = parent; 
		this.data = data; 
		this.box = box;
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

			this.northwest = new EmptyNode(this,box.northwestbox());
			this.northeast = new EmptyNode(this,box.northeastbox());
			this.southwest = new EmptyNode(this,box.southwestbox());
			this.southeast = new EmptyNode(this,box.southeastbox()); 
		}

		public InternalNode(Node parent, BoundingBox box) {
			this.parent = parent; 
			this.box = box; 
			northwest = new EmptyNode(this,box.northwestbox());
			northeast = new EmptyNode(this,box.northeastbox());
			southwest = new EmptyNode(this,box.southwestbox());
			southeast = new EmptyNode(this,box.southeastbox()); 
		}

		public String toString() {
			return "(" + northeast.toString() + ", " + northwest.toString() + ", " + southeast.toString() + ", " + southwest.toString() + ")"; 
		}
}
//empty nodes (no point) 
public class EmptyNode extends Node {
	public BoundingBox box;
	public EmptyNode(Node parent,BoundingBox box) {
		this.box = box;
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
		EmptyNode myNode = (EmptyNode) current;
		BoundingBox myBox = myNode.box;
		LeafNode discovered = new LeafNode(prev, obj, xcord, ycord, myBox); 
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
    		compiled.add(found);
		}
		// if not a leaf node it must be an empty node so look at the siblings ndoes 
		else if (current instanceof QuadtreeImplement.EmptyNode) {
			// get the parent node (internal node) and access each region (nw, ne, sw, se)
			if (current.parent == null) {
       			System.out.println("Parent is null - issue.");
       		}
			Node parent = current.parent; 
			if (parent instanceof QuadtreeImplement.InternalNode) {
				InternalNode internal1 = (InternalNode) parent; 
				// use check leaf to know if any of them are leaf nodes or contain leaf nodes in their subdivisions (don't bother checking current empty node)
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
// created for efficiency, check if given node is a leafnode or if leaf nodes exist inside of them (recursive check) 
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
    quadtree.insert(1, 40.0, 45.0);   
	quadtree.insert(2, 15.0, 70.0);
	quadtree.insert(3, 70.0, 10.0);
	quadtree.insert(4, 69.0, 50.0);
	quadtree.insert(5, 55.0, 80.0);
	quadtree.insert(6, 80.0, 90.0);


    System.out.println("Querying for (60.0, 75.0):");
    System.out.println(quadtree.get(123.0, 11.0)); 

		

        // Insert outside bounding box
        //System.out.println(quadtree.root.toString()); 

}
}
