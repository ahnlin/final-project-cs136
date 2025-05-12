package fires;
import java.util.*; 
public class Moniter{
	private String site;
	private double xcoord;
	private double ycoord;
	private HashMap<String,Double> data;
	public Moniter(String site, double xcoord,double ycoord){
		this.site = site;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.data = new HashMap<String,Double>();
	}
	@Override
	public String toString() {
		return site + "\n" + "(" +xcoord+","+ ycoord+ ")" + "\n" + "-------";
	}
	public String getSite(){
		return site;
	}
	public HashMap<String,Double> getData(){
		return data;
	}

	public static void main(String[] args){
		Moniter test1 = new Moniter("TestSite",312.2,402.1);
		System.out.println(test1.toString());

	}
}