package fires;

public interface QuadtreeInterface<Item>{
	
	// returns true if the quadtree is empty
	public abstract boolean isEmpty();

	// returns the number of non-empty nodes
	public abstract int size();

	// adds object to quadtree 
	//public abstract void insert(Item obj);

/*Is this to get a full branch or just one entry or one moniter?*/
	
	//public abstract Item get(Item obj);

}