package fires;

public interface QuadtreeInterface<Item>{
	
	//returns whether or not the quadtree is empty
	public abstract boolean isEmpty();

	//returns number of items in quadtree
	public abstract int size();

	// adds object to quadtree 
	public abstract void insert(Item obj, double xcord, double ycord);

	//locates closest object given (x,y)
	public abstract List<LeafNode> get(double xcval, double yval);

}