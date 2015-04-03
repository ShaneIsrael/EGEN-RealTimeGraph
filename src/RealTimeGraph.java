import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;


public class RealTimeGraph extends JFrame
{
	GraphPanel graph;
	private float scaleTime;
	private float scaleValue;
	
	float gMaxVal = 255;
	float gMaxTime = 600; //seconds
	Random r = new Random();
	
	
	public RealTimeGraph()
	{
		createScale();
		
		graph = new GraphPanel(gMaxTime, gMaxVal, scaleTime, scaleValue);
		graph.setValueTitle("Salinity Value");
		//this.setLayout(new BorderLayout())
		this.add(graph);
		this.setBounds(100,100, GraphPanel.width + 100, GraphPanel.height + 150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Real Time Graph");
		this.setVisible(true);
		
		generateTestData();
	}
	
	private void createScale() {
		scaleTime = (float)(GraphPanel.width/gMaxTime);
		scaleValue = (float)(GraphPanel.height/gMaxVal);
		
	}

	private void generateTestData() {
		//generate 3 graphs
		for(int j = 0; j < 1; j++)
		{
			if(j == 0)
				graph.setGraphColor(Color.RED);
			if(j == 1)
				graph.setGraphColor(Color.GREEN);
			if(j == 2)
				graph.setGraphColor(Color.BLUE);
			int lastValue = 245;
			for(int i = 0; i < gMaxTime; i++)
			{
				int add = r.nextInt(7);
				int minus = r.nextInt(8);
				int value = lastValue + add - minus;
				if(value < 0)
					value = 0;
				
				lastValue = value;
				
				graph.addPoint((int)(i*scaleTime),(int)(value*scaleValue));
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			graph.testFinished();
		}
		graph.exportPNG("graph");
	}

	public static void main(String... args)
	{
		new RealTimeGraph();
		
	}
}
