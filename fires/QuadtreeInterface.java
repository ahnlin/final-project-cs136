package fires;

public interface QuadtreeInterface<Item>{
	

	public abstract boolean isEmpty();

	public abstract int size();

	public abstract void insert(Item obj);

/*Is this to get a full branch or just one entry or one moniter?*/
	
	public abstract Item get(Item obj);

