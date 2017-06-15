package Analysis;


import sample.ViewWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AnalysisWindow extends JFrame {

    BufferedImage bImage = null;
    Colors colors = null;
    Object[] clr = null;

    private HashMap<Color, ArrayList<Integer>> countColors;

    public AnalysisWindow(BufferedImage bImage){
        this.bImage = bImage;
    }

    public void window() {

        setSize(450,600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
        add(jp);
        Panel panel = new Panel();

        JButton qtColors = new JButton("qtColors");
        jp.add(qtColors);
        qtColors.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == 1) {
                    colors = new Colors(bImage);
                    countColors = colors.getCountColors();
                    clr = countColors.keySet().toArray();
                    panel.repaint();
                    System.out.println(panel.getSize());
                    panel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            super.mouseEntered(e);
                            System.out.println(e.getButton());
                        }
                    });
                }
            }
        });


        jp.add(panel);
        panel.setBackground(Color.WHITE);
        Dimension panelSize = panel.getSize();
        System.out.println(panelSize);


        setVisible(true);

    }


    public class Panel extends JPanel {

        public Panel(){

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (clr == null) return;

            System.out.println("clr: " + clr.length);

            int x = 1, y = 1;

            for ( int i = 0; i < clr.length; i++ ) {
                g.setColor( (Color) clr[i]);
                g.fillRect(x, y, 10,10);
                x = x + 11;
                if (x > 435) {
                    x = 1;
                    y = y + 11;
                }
                if (y > 540) break;
            }

        }
    }


}
