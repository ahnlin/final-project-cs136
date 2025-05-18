package fires;
import java.util.*; 
import java.io.*; 
import javax.swing.*;
import java.awt.*;

public class Analysis {
//Initialize a Hashmap testlist that keeps track of moniters, and our Quadtree, firetree
 	public HashMap<String,Moniter> testlist;
 	public QuadtreeImplement firetree = new QuadtreeImplement<Moniter>(-117.5,  34.5,  -119, 33.5);
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

		Analysis test = new Analysis("fires/ad_viz_plotval_data (1).csv");
		System.out.println("The Data for Glendora on 01/03/2023 is: "+test.testlist.get("Glendora").getData().get("01/03/2023"));
		System.out.println("The Data for Glendora on 01/04/2023 is: "+test.testlist.get("Glendora").getData().get("01/04/2023"));

		System.out.println("\n"+test.testlist.keySet());
		//Create a new Jframe to visualize our quadtree in
		JFrame frame = new JFrame("Quadtree View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(new QuadtreeView(test.firetree));
        frame.setVisible(true);
		
	}
		


}