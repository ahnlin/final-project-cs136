package fires;
import javax.swing.*;
import java.awt.*;
public class QuadtreeView extends JPanel {
	QuadtreeImplement<?> tree;
	QuadtreeView(QuadtreeImplement<?> tree){
		this.tree = tree;
	}
     @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawNode(g, tree.root);
    }
    

	private void drawNode(Graphics g, QuadtreeImplement<?>.Node node){
		if((node instanceof QuadtreeImplement.InternalNode)){
			QuadtreeImplement<?>.InternalNode intNode = (QuadtreeImplement<?>.InternalNode) node;
			BoundingBox intBox = intNode.box;
            int width = getWidth();
            int height = getHeight();
            int xMin = (int) (intBox.minx / 10.0 * width);
            int xMax = (int) (intBox.maxx / 10.0 * width);
            int yMin = (int) ((10.0 - intBox.miny) / 10.0 * height);
            int yMax = (int) ((10.0 - intBox.maxy) / 10.0 * height);
            int xMid = (int) (intBox.midx / 10.0 * width);
            int yMid = (int) ((10.0 - intBox.midy) / 10.0 * height);
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
            int width = getWidth();
            int height = getHeight();
            int x = (int) (leafNode.xcord / 10.0 * width);
            int y = (int) ((10.0 - leafNode.ycord) / 10.0 * height);

            g.setColor(Color.RED);
            g.fillOval(x - 3, y - 3, 6, 6);
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