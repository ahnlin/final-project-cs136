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
}
//empty nodes (no point) 
public class EmptyNode extends Node {
	public EmptyNode(Node parent) {
		this.parent = parent; 
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
}