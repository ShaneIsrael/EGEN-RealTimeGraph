import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GraphPanel extends JPanel
{

    int xBarMargin = 50;
    int yBarMargin = 50;

    float xMin = 0;
    float xMax;
    float yMin = 0;
    float yMax;

    float TICK_MARGIN = 0.05f;
    int yTickMargin;
    int xTickMargin;

    static int width = 1100;
    static int height = 1100;

    int gWidth = width + 100;
    int gHeight = height + 100;
    BufferedImage graph;
    Graphics2D graph2D;
    Color graphColor = Color.red;
    float scaleY, scaleX;

    ArrayList<Point> points = new ArrayList<Point>();

    ArrayList<Test> tests = new ArrayList<Test>();

    String valueTitle = "";
    String timeTitle = "Time [secs]";

    Font titleFont = new Font(null, Font.BOLD, 14);
    Font tickFont = new Font(null, Font.PLAIN, 12);

    public GraphPanel(float xMax, float yMax, float scaleX, float scaleY)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        this.xMax = xMax * scaleX;
        this.yMax = yMax * scaleY;

        yTickMargin = (int) (TICK_MARGIN * this.yMax);
        xTickMargin = (int) (TICK_MARGIN * this.xMax);
        graph = new BufferedImage(gWidth, gHeight,
            BufferedImage.TYPE_INT_ARGB);
        graph2D = ((BufferedImage) graph).createGraphics();
        graph2D.setBackground(Color.WHITE);
        graph2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        graph2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graph2D.setFont(new Font(null, Font.BOLD, 14));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        graph = new BufferedImage(gWidth, gHeight, BufferedImage.TYPE_INT_ARGB);
        graph2D = graph.createGraphics();

        drawGraph(graph2D, false);
        g2d.drawImage(graph, 0, 0, null);
        

        super.repaint();
        g2d.dispose();
    }

    private void drawGraph(Graphics2D graph2D, boolean debug)
    {
        // Drag graph
        graph2D.setColor(Color.white);
        graph2D.fillRect(0, 0, gWidth, gHeight);
        graph2D.setColor(Color.black);
        graph2D.setStroke(new BasicStroke(3f));
        graph2D.drawLine(0, gHeight - xBarMargin, gWidth + xTickMargin, gHeight
            - xBarMargin); // draws x-axis
        graph2D.drawLine(yBarMargin, gHeight, yBarMargin, gHeight - yBarMargin
            - (int) yMax); // draws y-axis
        graph2D.setStroke(new BasicStroke(1f));
        int tick = xTickMargin;
        graph2D.setFont(tickFont);
        for (int i = 0; i < xMax / xTickMargin; i++) // draws y-graph lines
        {
            graph2D.drawLine(yBarMargin + tick, gHeight - xBarMargin,
                yBarMargin + tick, gHeight - yTickMargin - (int) yMax);
            int tickValue = Math.round(tick / scaleX);
            graph2D.drawString("" + tickValue, yBarMargin + tick, gHeight
                - (yBarMargin / 2));
            tick += xTickMargin;
        }
        tick = yTickMargin;
        for (int i = 0; i < yMax / yTickMargin; i++) // draws x-graph lines
        {
            graph2D.drawLine(xBarMargin, gHeight - xBarMargin - tick, gWidth,
                gHeight - xBarMargin - tick); // draws x-axis\
            int tickValue = Math.round(tick / scaleY);
            graph2D.drawString("" + tickValue, xBarMargin / 2, gHeight
                - xBarMargin - tick);
            tick += yTickMargin;
        }
        graph2D.setFont(titleFont);
        // Draw titles
        graph2D.drawString(timeTitle, gWidth / 2, gHeight - 10);
        Font font = new Font(null, Font.BOLD, 14);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(-90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        graph2D.setFont(rotatedFont);
        graph2D.drawString(valueTitle, 15, gHeight / 2);

        // Draw Points on graph
        drawGraphPoints(points, graphColor);

        for (int i = 0; i < tests.size(); i++)
        {
            drawGraphPoints(tests.get(i).getGraph(), tests.get(i).getColor());
        }
        graph2D.dispose();
    }

    private void drawGraphPoints(ArrayList<Point> p, Color c)
    {
        graph2D.setStroke(new BasicStroke(3f));
        Point firstP;
        if (p.size() > 1)
        {
            firstP = p.get(0);
            for (int i = 1; i < p.size(); i++)
            {
                Point secondP = p.get(i);
                graph2D.setColor(c);
                graph2D.drawLine(firstP.x, firstP.y, secondP.x, secondP.y);
                firstP = secondP;
            }
        }

    }

    public void addPoint(int value, int seconds)
    {
        points.add(new Point(xBarMargin + value, gHeight - yBarMargin - seconds));
    }

    public void setGraphColor(Color c)
    {
        graphColor = c;
    }

    public void testFinished()
    {
        tests.add(new Test(points, graphColor));
        points = new ArrayList<Point>();
    }

    public void setValueTitle(String string)
    {
        // TODO Auto-generated method stub
        valueTitle = string;
    }


    public void exportPNG(String string)
    {
        BufferedImage finalImage = new BufferedImage(graph.getWidth(null), graph.getHeight(null),
            BufferedImage.TYPE_INT_ARGB);
        finalImage.getGraphics().drawImage(graph, 0, 0, null);
        try
        {
            ImageIO.write(finalImage, "png", new File(string+".png"));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("Graph exported successfully!");
    }

}
