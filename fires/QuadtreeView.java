package fires;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class QuadtreeView extends JPanel {
	//Instatnce variable of the tree we want to draw
    QuadtreeImplement<Moniter> tree;
    String date;
    int year;
    //constructor
	QuadtreeView(QuadtreeImplement<Moniter> tree,String date){
		this.tree = tree;
        this.date = date;
        this.year = Integer.parseInt(date.split("/")[2]);

	}
    //Allows us to draw on the Jframe
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawNode(g, tree.root);
    }
    public static Color airToColor(double air){
        if(air < 0.0){
            return Color.BLACK;
        }
        else if(air < 12.0){
            return Color.YELLOW;
        }
        else if(air < 35.0){
            return Color.ORANGE;
        }
        else{
            return Color.RED;
        }
    }

    //Did some research into an easier way then manually converting dates to days, which is simple but tedious
    //I imported the time package I hope this okay, its use is kind of irrelevant to the quadtree and just for visualization
    public static int dateToDay(String myDate){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(myDate, format);
        return date.getDayOfYear();

    }
    public static String dayToDate(int myDay,int year){
        LocalDate date = LocalDate.ofYearDay(year, myDay);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(format);
    }
    public void setDate(String newdate){
        this.date = newdate;
        repaint();
    }
    //Picks the type of Node and draws appropriate thing for that node
	private void drawNode(Graphics g, QuadtreeImplement<Moniter>.Node node){
		if((node instanceof QuadtreeImplement.InternalNode)){
			QuadtreeImplement<Moniter>.InternalNode intNode = (QuadtreeImplement<Moniter>.InternalNode) node;
			BoundingBox box = tree.root.box;
            BoundingBox intBox = intNode.box;
            int width = getWidth();
            int height = getHeight();
            double rootMinX = box.minx;
            double rootMinY = box.miny;
            double rootMaxX = box.maxx;
            double rootMaxY = box.maxy;
            int xMin = (int) ((intBox.minx - rootMinX)/(rootMaxX-rootMinX)*width);
            int xMax = (int) ((intBox.maxx - rootMinX)/(rootMaxX-rootMinX)*width);
            int yMin = (int) ((rootMaxY - intBox.miny)/(rootMaxY-rootMinY)*height);
            int yMax = (int) ((rootMaxY - intBox.maxy)/(rootMaxY-rootMinY)*height);
            int xMid = (int) ((intBox.midx - rootMinX)/(rootMaxX-rootMinX)*width);
            int yMid = (int) ((rootMaxY - intBox.midy)/(rootMaxY-rootMinY)*height);
            drawNode(g, intNode.northwest);
            drawNode(g, intNode.northeast);
            drawNode(g, intNode.southwest);
            drawNode(g, intNode.southeast);
            g.setColor(Color.BLACK);
            g.drawLine(xMid, yMax, xMid, yMin);
            g.drawLine(xMin, yMid, xMax, yMid);

      

		}
        else if (node instanceof QuadtreeImplement.LeafNode) {
            QuadtreeImplement<Moniter>.LeafNode leafNode = (QuadtreeImplement<Moniter>.LeafNode) node;
            BoundingBox box = tree.root.box;
            BoundingBox leafBox = leafNode.box;

            int width = getWidth();
            int height = getHeight();

            double x = leafNode.xcord;
            double y = leafNode.ycord;
            int screensX = (int) ((x - box.minx) / (box.maxx - box.minx) * width);
            int screensY = (int) ((box.maxy - y) / (box.maxy - box.miny) * height);
            double rootMinX = box.minx;
            double rootMinY = box.miny;
            double rootMaxX = box.maxx;
            double rootMaxY = box.maxy;
            int xMin = (int) ((leafBox.minx - rootMinX)/(rootMaxX-rootMinX)*width);
            int xMax = (int) ((leafBox.maxx - rootMinX)/(rootMaxX-rootMinX)*width);
            int yMin = (int) ((rootMaxY - leafBox.miny)/(rootMaxY-rootMinY)*height);
            int yMax = (int) ((rootMaxY - leafBox.maxy)/(rootMaxY-rootMinY)*height);
            int xMid = (int) ((leafBox.midx - rootMinX)/(rootMaxX-rootMinX)*width);
            int yMid = (int) ((rootMaxY - leafBox.midy)/(rootMaxY-rootMinY)*height);


           
            g.setColor(airToColor(leafNode.data.getDataFor(date)));
            g.fillRect(xMin,yMax,xMax-xMin,yMin-yMax);
            g.setColor(Color.BLUE);
            g.fillOval(screensX - 2, screensY - 2, 4, 4);
        }
    }
    public static void play(QuadtreeImplement<Moniter> tree,String date,int year){
        QuadtreeView view = new QuadtreeView(tree,date);
        
        JFrame frame = new JFrame("Quadtree View");
        JSlider slider = new JSlider(0, 364, 0);

        

        slider.addChangeListener(e -> {
        String newDate = dayToDate(slider.getValue()+1,year);
        System.out.println(newDate);
        view.setDate(newDate);
        });


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.CENTER);
        frame.add(slider, BorderLayout.SOUTH);
        frame.setSize(800, 800);
        frame.setVisible(true);
        
    }


	
	public static void main(String[] args) {
        /*QuadtreeImplement<Integer> tree = new QuadtreeImplement<>(10.0, 10.0, 0.0, 0.0);
        tree.insert(1, 2.0, 2.0);
        tree.insert(2, 8.0, 8.0);
        tree.insert(3, 6.0, 6.0);
        tree.insert(4, 3.0, 7.0);
        tree.insert(5, 5.0, 5.0);*/

        
    }

}