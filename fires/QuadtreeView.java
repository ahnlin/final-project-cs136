package fires;
import javax.swing.*;
import java.awt.*;
public class QuadtreeView extends JPanel {
	QuadtreeImplement<?> tree;
	QuadtreeView(QuadtreeImplement<?> tree){
		this.tree = tree;
	}
    //Allows us to draw on the Jframe
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawNode(g, tree.root);
    }
    //Picks the type of Node and draws appropriate thing for that node
	private void drawNode(Graphics g, QuadtreeImplement<?>.Node node){
		if((node instanceof QuadtreeImplement.InternalNode)){
			QuadtreeImplement<?>.InternalNode intNode = (QuadtreeImplement<?>.InternalNode) node;
			BoundingBox intBox = intNode.box;
            int width = getWidth();
            int height = getHeight();
            double rootMinX = tree.root.box.minx;
            double rootMinY = tree.root.box.miny;
            double rootMaxX = tree.root.box.maxx;
            double rootMaxY = tree.root.box.maxy;
            int xMin = (int) ((intBox.minx - rootMinX)/(rootMaxX-rootMinX)*width);
            int xMax = (int) ((intBox.maxx - rootMinX)/(rootMaxX-rootMinX)*width);
            int yMin = (int) ((rootMaxY - intBox.miny)/(rootMaxY-rootMinY)*height);
            int yMax = (int) ((rootMaxY - intBox.maxy)/(rootMaxY-rootMinY)*height);
            int xMid = (int) ((intBox.midx - rootMinX)/(rootMaxX-rootMinX)*width);
            int yMid = (int) ((rootMaxY - intBox.midy)/(rootMaxY-rootMinY)*height);
            g.setColor(Color.BLACK);
            g.drawLine(xMid, yMax, xMid, yMin);
            g.drawLine(xMin, yMid, xMax, yMid);

            drawNode(g, intNode.northwest);
            drawNode(g, intNode.northeast);
            drawNode(g, intNode.southwest);
            drawNode(g, intNode.southeast);

		}
        else if (node instanceof QuadtreeImplement.LeafNode) {
            QuadtreeImplement<?>.LeafNode leafNode = (QuadtreeImplement<?>.LeafNode) node;
            BoundingBox box = tree.root.box;
            int width = getWidth();
            int height = getHeight();
            double x = leafNode.xcord;
            double y = leafNode.ycord;
            int screensX = (int) ((x - box.minx) / (box.maxx - box.minx) * width);
            int screensY = (int) ((box.maxy - y) / (box.maxy - box.miny) * height);

            g.setColor(Color.RED);
            g.fillOval(screensX - 2, screensY - 2, 4, 4);
        }


	}
	public static void main(String[] args) {
        QuadtreeImplement<Integer> tree = new QuadtreeImplement<>(10.0, 10.0, 0.0, 0.0);
        tree.insert(1, 2.0, 2.0);
        tree.insert(2, 8.0, 8.0);
        tree.insert(3, 6.0, 6.0);
        tree.insert(4, 3.0, 7.0);
        tree.insert(5, 5.0, 5.0);

        JFrame frame = new JFrame("Quadtree View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(new QuadtreeView(tree));
        frame.setVisible(true);
    }

}