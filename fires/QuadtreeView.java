package fires;
import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class QuadtreeView extends JPanel {

	//Instatnce variable of the tree we want to draw
    public QuadtreeImplement<Moniter> tree;
    public BoundingBox box;
    public String date;
    public int year;
    private boolean extrapolateMode;
    private Image background; 

    //constructor
	QuadtreeView(QuadtreeImplement<Moniter> tree,String date){
        this.extrapolateMode = false;
		this.tree = tree;
        this.date = date;
        this.box = tree.root.box;
        this.year = Integer.parseInt(date.split("/")[2]);
        this.background = new ImageIcon("fires/CaliMap.png").getImage();

	}

    //Allows us to draw on the Jframe
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        drawNode(g, tree.root);
    }

    //sets ranges of colors based on pm2.5 levels
    public static Color airToColor(double air){
        if(air < 0.0){
            return Color.GRAY;
        }
        else if(air <= 9.0){
            return Color.GREEN;
        }
        else if(air <= 35.4){
            return Color.YELLOW;
        }
        else if(air <= 55.4){
            //darker orange
            return new Color(255,140,0);
        }
        else if(air <= 125.4){
            return Color.RED;
        }
        else if (air <= 125.4){
            //Purple
            return new Color(128,0,128);
        }
        return Color.GRAY;
    }

    //Did some research into an easier way then manually converting dates to days, which is simple but tedious
    //I imported the time package I hope this okay, its use is kind of irrelevant to the quadtree and just for visualization
    //conversion from date to day so we can print the day
    public static int dateToDay(String myDate){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(myDate, format);
        return date.getDayOfYear();

    }
    //conversion from day of the year to date so we can extract the data for that date
    public static String dayToDate(int myDay,int year){
        LocalDate date = LocalDate.ofYearDay(year, myDay);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(format);
    }
    //so we can scroll through dates and it repaints for each one
    public void setDate(String newdate){
        this.date = newdate;
        repaint();
    }
    //so we can change the mode with out Jbutton
    public void changeMode(){
        extrapolateMode = !extrapolateMode;
        repaint();
    }
    //So we can access mode in static play method
    public String modeStatus(){
        String mode = "Reading Mode";
        if(extrapolateMode==true){
            mode = "Extrapolate Mode";
        }
        return mode;
    }

    //helper function to fill in a bounding box a given color
    public void fillBox(BoundingBox currentBox,Color color,Graphics g){
        int width = getWidth();
        int height = getHeight();
        double rootMinX = box.minx;
        double rootMinY = box.miny;
        double rootMaxX = box.maxx;
        double rootMaxY = box.maxy;

        //Scaling based on width so we can draw the box on our Jframe
        int xMin = (int) ((currentBox.minx - rootMinX)/(rootMaxX-rootMinX)*width);
        int xMax = (int) ((currentBox.maxx - rootMinX)/(rootMaxX-rootMinX)*width);
        int yMin = (int) ((rootMaxY - currentBox.miny)/(rootMaxY-rootMinY)*height);
        int yMax = (int) ((rootMaxY - currentBox.maxy)/(rootMaxY-rootMinY)*height);
        g.setColor(color);
        g.fillRect(xMin,yMax,xMax-xMin,yMin-yMax);

    }
    //Picks the type of Node and draws appropriate thing for that node
	private void drawNode(Graphics g, QuadtreeImplement<Moniter>.Node node){
		
        //for internal nodes we want to just draw lines to represent their divisions
        if((node instanceof QuadtreeImplement.InternalNode)){
			QuadtreeImplement<Moniter>.InternalNode intNode = (QuadtreeImplement<Moniter>.InternalNode) node;
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
            
            //recursively calling drawNode to branch down to other nodes
            drawNode(g, intNode.northwest);
            drawNode(g, intNode.northeast);
            drawNode(g, intNode.southwest);
            drawNode(g, intNode.southeast);

            //drawing the lines (after recursion so they show up on top)
            g.setColor(Color.BLACK);
            g.drawLine(xMid, yMax, xMid, yMin);
            g.drawLine(xMin, yMid, xMax, yMid);
		}

        //for leaf nodes we want to color them in with smoke data and insert the point that is the leaf node
        else if (node instanceof QuadtreeImplement.LeafNode){
            QuadtreeImplement<Moniter>.LeafNode leafNode = (QuadtreeImplement<Moniter>.LeafNode) node;
            BoundingBox leafBox = leafNode.box;
            int width = getWidth();
            int height = getHeight();
            double x = leafNode.xcord;
            double y = leafNode.ycord;
            //scaling x and y coordinates of the point to our screen
            int screensX = (int) ((x - box.minx) / (box.maxx - box.minx) * width);
            int screensY = (int) ((box.maxy - y) / (box.maxy - box.miny) * height);
            
            //coloring in box
            fillBox(leafBox,airToColor(leafNode.data.getDataFor(date)),g);
            
            //plotting point
            g.setColor(Color.BLUE);
            g.fillOval(screensX - 2, screensY - 2, 4, 4);
            
        }
        
        //if extrapolate mode is on we want to color the empty nodes with the average color of nearby nodes
        else if (node instanceof QuadtreeImplement.EmptyNode && (extrapolateMode == true)){
            QuadtreeImplement<Moniter>.EmptyNode emptyNode = (QuadtreeImplement<Moniter>.EmptyNode) node;
            BoundingBox emptyBox = emptyNode.box;
            
            //uses get function to find leafnodes nearby empty one and then averages their pm2.5 values
            List<QuadtreeImplement<Moniter>.LeafNode> myList = tree.get((emptyBox.maxx + emptyBox.minx)/2,(emptyBox.maxy + emptyBox.miny)/2);
            double sum = 0.0;
            int count = 1;
            for(QuadtreeImplement<Moniter>.LeafNode cur: myList){
                //excludes the monitors without readings
                if(cur.data.getDataFor(date)>0.0){
                    sum += cur.data.getDataFor(date);
                    count+=1;
                }
            }
            fillBox(emptyBox,airToColor(sum/count),g);


        }
    }

    //play method so we can create our visualization from the Analysis file
    public static void play(QuadtreeImplement<Moniter> tree,String date,int year){
        
        //making the elements of our viewer
        QuadtreeView view = new QuadtreeView(tree,date);
        JFrame frame = new JFrame("Quadtree View");
        JSlider slider = new JSlider(0, 364, 0);
        JButton toggle = new JButton(view.modeStatus());
        JLabel dateBox = new JLabel(date);

        //change listener for date slider -> updates the label too
        slider.addChangeListener(e -> {
        String newDate = dayToDate(slider.getValue()+1,year);
        view.setDate(newDate);
        dateBox.setText(newDate);
        });

        //action listener for mode button
        toggle.addActionListener(e -> {
        view.changeMode();
        toggle.setText(view.modeStatus());
        });

        //control panel to add all of the buttons to
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.add(slider);
        buttons.add(dateBox);
        buttons.add(toggle);

        //viewer setup
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);
        frame.setSize(800, 800);
        frame.setVisible(true);
        
    }


	
	public static void main(String[] args) {
        
    }

}