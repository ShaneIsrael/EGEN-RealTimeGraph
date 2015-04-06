import java.awt.Color;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

public class RealTimeGraph extends JFrame
{
    GraphPanel graph;
    private float scaleTime;
    private float scaleValue;

    float gMaxVal = 255;
    float gMaxTime = 600; //seconds
    Random r = new Random();

    /**
     * 
     * @param maxYAxisValue
     *          The max value a piece of data can be.
     * @param maxRunTime
     *          (seconds) How long you want the test to run for, this is also the max value for your X-axis
     */
    public RealTimeGraph(float maxYAxisValue, float maxRunTime, int size)
    {
        GraphPanel.width = size;
        GraphPanel.height = size;
        
        gMaxVal = maxYAxisValue;
        gMaxTime = maxRunTime;
        createScale();

        graph = new GraphPanel(gMaxTime, gMaxVal, scaleTime, scaleValue);
        graph.setValueTitle("Salinity Value");
        //this.setLayout(new BorderLayout())
        this.add(graph);
        this.setBounds(100, 100, GraphPanel.width + 100, GraphPanel.height + 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Real Time Graph");
        this.setVisible(true);

    }

    private void createScale()
    {
        scaleTime = (float) (GraphPanel.width / gMaxTime);
        scaleValue = (float) (GraphPanel.height / gMaxVal);

    }

    public void generateTestData()
    {
        //generate 3 graphs
        for (int j = 0; j < 3; j++)
        {
            if (j == 0)
                graph.setGraphColor(Color.RED);
            if (j == 1)
                graph.setGraphColor(Color.GREEN);
            if (j == 2)
                graph.setGraphColor(Color.BLUE);
            int lastValue = (int) (gMaxVal - 5);
            for (int i = 0; i < gMaxTime; i++)
            {
                int add = r.nextInt(4);
                int minus = r.nextInt(5);
                float p = r.nextFloat();
                
                int value = 0;
                if(p <= .49)
                    value = lastValue + add;
                else
                    value = lastValue - minus;
                
                if (value < 0)
                    value = 0;

                lastValue = value;

                graphData((int) (i), (int) (value));
                try
                {
                    Thread.sleep(10);
                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            graph.testFinished();
        }
        graph.exportPNG("graph");
    }
    
    public void graphData(float time, float value)
    {
        graph.addPoint((int)(time*scaleTime),(int)(value*scaleValue));
    }

    public static void main(String... args)
    {
        RealTimeGraph graph = new RealTimeGraph(200, 30, 1500);
        //graph.generateTestData();
        Scanner scanner = new Scanner(System.in);
        
        String in = "";
        long startTime = System.currentTimeMillis();
        while(!in.equals("exit"))
        {
            System.out.print("Enter a value: ");
            in = scanner.next();
            if(!in.equals("exit"))
            {
                long currentTime = System.currentTimeMillis();
                int seconds = (int)(currentTime - startTime)/1000;
                graph.graphData(seconds, Integer.parseInt(in));
            }
        }

    }
}
