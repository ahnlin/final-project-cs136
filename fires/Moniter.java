package fires;
import java.util.*; 
public class Moniter{
	//instance variables
	private String site;
	private double xcoord;
	private double ycoord;
	private HashMap<String,Double> data;

	//constructor
	public Moniter(String site, double xcoord,double ycoord){
		this.site = site;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.data = new HashMap<String,Double>();
	}

	
	@Override
	public String toString() {
		return site;
	}

	public String getSite(){
		return site;
	}
	public HashMap<String,Double> getData(){
		return data;
	}
	public double getDataFor(String date){
		if (data.get(date) == null){
			return -1.0;
		}
		return data.get(date);
	}
	public double getX(){
		return xcoord;
	}
	public double getY(){
		return ycoord;
	}

	public static void main(String[] args){
		Moniter test1 = new Moniter("TestSite",312.2,402.1);
		System.out.println(test1.toString());

	}
}