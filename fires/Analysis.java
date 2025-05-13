package fires;
import java.util.*; 
import java.io.*; 
import javax.swing.*;
import java.awt.*;

public class Analysis {



 	public HashMap<String,Moniter> testlist;
 	public QuadtreeImplement firetree = new QuadtreeImplement<Moniter>(-114.131211,  42.009518,  -124.409591, 32.534156);
	public Analysis(String filename){
		this.testlist = new HashMap<String,Moniter>();
		String filePath = filename; 
		File file = new File(filePath);
		

		try{
			
			Scanner scanner = new Scanner(file);
			scanner.nextLine();

			while (scanner.hasNextLine()) {
				String[] pieces = scanner.nextLine().split(",");
				String moniter = pieces[7].replace("\"", "");
				Double ycoord = Double.valueOf(pieces[21].replace("\"", ""));
				Double xcoord = Double.valueOf(pieces[22].replace("\"", ""));	
				Moniter mon = new Moniter(moniter,xcoord,ycoord);
				
				if(!testlist.containsKey(moniter)){
					testlist.put(moniter,mon);
					firetree.insert(mon,xcoord,ycoord);
				}
			
            }
		}
		catch(FileNotFoundException e){
			 System.err.println("File not found: " + e.getMessage());
		}

	
		try{
			
			Scanner scanner = new Scanner(file);
			scanner.nextLine();

			while (scanner.hasNextLine()) {
				String[] pieces = scanner.nextLine().split(",");
				String date = pieces[0].replace("\"", "");
				Double reading = Double.valueOf(pieces[4].replace("\"", ""));
				String moniter = pieces[7].replace("\"", "");
				testlist.get(moniter).getData().put(date,reading);
			}

			
        }
		catch(FileNotFoundException e){
			 System.err.println("File not found: " + e.getMessage());
		}

	}
	public static void main(String[] args){

		Analysis test = new Analysis("fires/ad_viz_plotval_data (1).csv");
		System.out.println(test.testlist.get("Glendora").getData().get("01/03/2023"));
		System.out.println(test.firetree.root.toString());
		
	}
		


}