package fires;
public class Moniter{
	private String site;
	private double xcoord;
	private double ycoord;
	public Moniter(String site, double xcoord,double ycoord){
		this.site = site;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
	}
	@Override
	public String toString() {
		return site + "\n" + "(" +xcoord+","+ ycoord+ ")" + "\n" + "-------" + "\n";
	}
	public String getSite(){
		return site;
	}

	public static void main(String[] args){
		Moniter test1 = new Moniter("TestSite",312.2,402.1);
		System.out.println(test1.toString());

	}
}