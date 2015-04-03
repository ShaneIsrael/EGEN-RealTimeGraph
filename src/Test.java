import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;


public class Test {
	
	ArrayList<Point> graph;
	Color graphColor;
	public Test(ArrayList<Point> points, Color c)
	{
		this.graph = points;
		this.graphColor = c;
	}
	public ArrayList<Point> getGraph()
	{
		return graph;
	}
	public Color getColor()
	{
		return graphColor;
	}
}
