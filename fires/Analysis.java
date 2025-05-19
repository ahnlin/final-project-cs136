package fires;
import java.util.*; 
import java.io.*; 
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Analysis {
//Initialize a Hashmap testlist that keeps track of moniters, and our Quadtree, firetree
 	public HashMap<String,Moniter> testlist;
 	public QuadtreeImplement firetree = new QuadtreeImplement<Moniter>(-114.0,  42.0,  -124.0, 32.0);
	public Analysis(String filename){
		this.testlist = new HashMap<String,Moniter>();
		String filePath = filename; 
		File file = new File(filePath);
		//Read in the file once to add all the moniters to the quadtree
		try{
			
			Scanner scanner = new Scanner(file);
			scanner.nextLine();

			while (scanner.hasNextLine()) {
				String[] pieces = scanner.nextLine().split(",");
				String moniter = pieces[7].replace("\"", "");
				System.out.println(pieces[21]);
				Double ycoord = Double.valueOf(pieces[pieces.length-2].replace("\"", ""));
				Double xcoord = Double.valueOf(pieces[pieces.length-1].replace("\"", ""));	
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
		//Read in the files again to add the historical data to each moniter
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

		//Analysis test = new Analysis("fires/ad_viz_plotval_data (1).csv");
		Analysis test2 = new Analysis("fires/Cali2023.csv");
		//System.out.println("The Data for Glendora on 01/03/2023 is: "+test.testlist.get("Glendora").getDataFor("01/03/2023"));
		//System.out.println("The Data for Glendora on 01/04/2023 is: "+test.testlist.get("Glendora").getDataFor("01/04/2023"));

		//System.out.println("\n"+test.testlist.keySet());
		//Create a new Jframe to visualize our quadtree in
		QuadtreeView.play(test2.firetree,"05/23/2023",2023);
		
	}
		


}