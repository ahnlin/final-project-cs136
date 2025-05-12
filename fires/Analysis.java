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
				Double xcoord = Double.valueOf(pieces[21].replace("\"", ""));
				Double ycoord = Double.valueOf(pieces[22].replace("\"", ""));	
				Moniter mon = new Moniter(moniter,xcoord,ycoord);
				
				boolean in = false;
					for (Moniter item : testlist) {
    					if (item.getSite().equals(moniter)) {
       						 in = true;
       					}
       				}
				if(in == false){
					testlist.add(mon);
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
				String moniter = pieces[7];
				for (Moniter item : testlist) {
    					if (item.getSite().equals(moniter)) {
       						 item.getData().put(date,reading);
       					}
       			}

			}

			
        }
		catch(FileNotFoundException e){
			 System.err.println("File not found: " + e.getMessage());
		}
	}
	public static void main(String[] args){
		Analysis test = new Analysis("fires/ad_viz_plotval_data (1).csv");
		for(int i=0; i<test.testlist.size(); i++){
			System.out.println(test.testlist.get(i).getData().get("01/02/2023"));
			System.out.println(test.testlist.get(i).toString());

		}
	}

}