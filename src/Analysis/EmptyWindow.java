package Analysis;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class EmptyWindow extends JFrame {

    int width;
    int height;
    String title = null;
    DotPanel dotPanel = null;
    HashMap<Color, ArrayList<Integer>> points = null;
//    ArrayList<Point> points = null;

    public EmptyWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void window() {
        points = new HashMap<>();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(width + 16, height + 39);
        setTitle(getEmpTitle());
        setLocationRelativeTo(null);

        dotPanel = new DotPanel();
        dotPanel.setBackground(Color.WHITE);
        add(dotPanel);

        setVisible(true);
        System.out.println("dotPanelSize: " + dotPanel.getWidth() + " " + dotPanel.getHeight());
    }



    public class DotPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPoints() == null) {
                dotPanel.setBackground(Color.WHITE);
                return;
            }

            Object[] objects = getPoints().keySet().toArray();
            for (int i = 0; i < objects.length; i++) {
//                System.out.println("objectsSize: " + objects.length);
                Color cl = (Color) objects[i];
                g.setColor(cl);
//                System.out.println( getPoints().get(cl) );

                Iterator iter = getPoints().get(cl).listIterator();
                while (iter.hasNext()) {
                    int a = (int) iter.next();
                    Point point = Colors.getPoint(a,getEmpWidth());
                    g.drawRect(point.x, point.y, 1, 1);
//                    System.out.println(String.valueOf(iter));
                }
            }

        }
    }



    public int getEmpWidth() {
        return width;
    }

    public int getEmpHeight() {
        return height;
    }


    public String getEmpTitle() {
        return title;
    }

    public DotPanel getDotPanel() {
        return dotPanel;
    }

    public HashMap<Color, ArrayList<Integer>> getPoints() {
        return points;
    }
}
