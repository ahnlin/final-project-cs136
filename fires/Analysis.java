package fires;
import java.util.*; 
import java.io.*; 

public class Analysis{

 	public ArrayList<Moniter> testlist;
	public Analysis(String filename){
		this.testlist = new ArrayList<Moniter>();
		String filePath = filename; 
		File file = new File(filePath);
		

		try{
			
			Scanner scanner = new Scanner(file);
			scanner.nextLine();

			while (scanner.hasNextLine()) {
				String[] pieces = scanner.nextLine().split(",");
				String moniter = pieces[7];
				Double xcoord = Double.valueOf(pieces[20]);

				Double ycoord = Double.valueOf(pieces[21]);	
				System.out.println(xcoord);
            }
		}
		catch(FileNotFoundException e){
			 System.err.println("File not found: " + e.getMessage());
		}
	}
	public static void main(String[] args){
		Analysis test = new Analysis("fires/ad_viz_plotval_data (1).csv");
		
	}

}