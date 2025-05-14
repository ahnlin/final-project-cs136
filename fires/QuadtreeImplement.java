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
			northwest = new EmptyNode(parent);
			northeast = new EmptyNode(parent);
			southwest = new EmptyNode(parent);
			southeast = new EmptyNode(parent); 
			this.box = new BoundingBox(maxx, maxy, minx, miny); 
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
	if (!root.box.in(xcord, ycord)) {
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

		if (box.northwestbox().in(xcord, ycord)) {
			region.northwest = insertHelper(region.northwest, current, obj, box.northwestbox(), xcord, ycord); 
		}

		else if (box.northeastbox().in(xcord, ycord)) {
			region.northeast = insertHelper(region.northeast, current, obj, box.northeastbox(), xcord, ycord);
		}

		else if (box.southwestbox().in(xcord, ycord)) {
			region.southwest = insertHelper(region.southwest, current, obj, box.southwestbox(), xcord, ycord);
		}

		else if(box.southeastbox().in(xcord, ycord)) {
			region.southeast = insertHelper(region.southeast, current, obj, box.southeastbox(), xcord, ycord);
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
			newIN = (InternalNode) insertHelper(newIN, null, old.data,box, old.xcord, old.ycord);
			
			// new obj into internal 
			newIN = (InternalNode) insertHelper(newIN, null,obj,box, xcord, ycord);

			return newIN;

		}
		
	}
	System.out.println("Another big issue!"); 
	return null; 
}

public List<LeafNode> get(double xval, double yval) {
	List<LeafNode> compiled = new ArrayList<>(); 
	getHelper(root, xval, yval, compiled); 
	return compiled; 
	}

public void getHelper(Node current, double xcord, double ycord, List<LeafNode> compiled) {
	if (current instanceof QuadtreeImplement.EmptyNode) {
		return; 
	}
	if (current instanceof QuadtreeImplement.LeafNode) {
		LeafNode found = (LeafNode) current; 
		if (found.xcord == xcord && found.ycord == ycord) {
			compiled.add(found); 
			return;
		}
	}
	else if (current instanceof QuadtreeImplement.InternalNode) {
		InternalNode box = (InternalNode) current; 
		if (box.northwest instanceof QuadtreeImplement.LeafNode) {
			LeafNode found = (LeafNode) box.northwest; 
			compiled.add(found); 
		}
		if (box.northeast instanceof QuadtreeImplement.LeafNode) {
			LeafNode found = (LeafNode) box.northeast; 
			compiled.add(found); 
		}
		if (box.southwest instanceof QuadtreeImplement.LeafNode) {
			LeafNode found = (LeafNode) box.southwest; 
			compiled.add(found); 
		}
		if (box.southeast instanceof QuadtreeImplement.LeafNode) {
			LeafNode found = (LeafNode) box.southeast; 
			compiled.add(found); 
		}
		return; 
	}
	return; 
}


public static void main(String[] args) {
	    QuadtreeImplement quadtree = new QuadtreeImplement<Integer>(10.0, 10.0, 0.0, 0.0);

        // Insert some points
        quadtree.insert(0, 1.0, 2.0);  
        quadtree.insert(1, 3.0, 4.0);  
        quadtree.insert(2, 5.0, 6.0); 
        System.out.println(quadtree.get(3.0,4.0)); 

        // Insert outside bounding box
        System.out.println(quadtree.root.toString()); 

}
}
