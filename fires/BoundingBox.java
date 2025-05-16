package fires;

public class BoundingBox{
// bounding boxes are used to partition the space of the quadtree -- each bounding box is divided into 4 even subsections

	//instance variables 
	public double maxx; 
	public double maxy; 
	public double minx; 
	public double miny; 
	public double midx; 
	public double midy; 

	// constructor 
	public BoundingBox(double maxx, double maxy, double minx, double miny) {
		this.maxx = maxx; 
		this.maxy = maxy; 
		this.minx = minx; 
		this.miny = miny; 

		midx = (maxx + minx) / 2; 
		midy = (maxy + miny) / 2;

	}

	public BoundingBox northwestbox() {
		return new BoundingBox(midx, maxy, minx, midy); 
	}

	public BoundingBox northeastbox() {
		return new BoundingBox(maxx, maxy, midx, midy); 
	}

	public BoundingBox southwestbox() {
		return new BoundingBox(midx, midy, minx, miny); 
	}

	public BoundingBox southeastbox() {
		return new BoundingBox(maxx, midy, midx, miny); 

	}

	// check if a point is in the box 
	public boolean contains(double xcord, double ycord) {
		if (xcord >= minx && xcord <= maxx && ycord >= miny && ycord <= maxy) {
			return true; 
		}
		return false; 
	}

}