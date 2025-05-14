package fires;

public interface QuadtreeInterface<Item>{
	

	public abstract boolean isEmpty();

	public abstract int size();

	// adds object to quadtree 
	public abstract void insert(Item obj, double xcord, double ycord);

/*Is this to get a full branch or just one entry or one moniter?*/
	public abstract Item get(double xcval, double yval);

}