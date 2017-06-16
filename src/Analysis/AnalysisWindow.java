package Analysis;


import sample.ViewWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class AnalysisWindow extends JFrame {

    BufferedImage bImage = null;
    String title = null;
    Colors colors = null;
    int widthW = 450;
    int heightW = 600;
    EmptyWindow emptyWindow = null;
    int numberCount;


    public AnalysisWindow(BufferedImage bImage, String title){
        this.bImage = bImage;
        this.title = title;
    }

    public void window() {
        colors = new Colors(bImage);
        colors.getCountColors();

        setSize(getWidthW(),getHeightW());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
//        setResizable(false);

        setWidthW(getWidth());
        setHeightW(getHeight());

        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
        add(jp);
        Panel panel = new Panel();

        JPanel jpButtons = new JPanel();
        jpButtons.setLayout(new BoxLayout(jpButtons,BoxLayout.X_AXIS));
        jp.add(jpButtons);
        JButton qtColors = new JButton("Кол-во оттенков");
        jpButtons.add(qtColors);
        JButton btEmptyPanel = new JButton("Пустая панель");
        jpButtons.add(btEmptyPanel);

        JTextField jtfNumberCount = new JTextField();
        jtfNumberCount.setMaximumSize(new Dimension(50,50));
        jpButtons.add(jtfNumberCount);
        JButton bNumberCount = new JButton("ok");
        jpButtons.add(bNumberCount);


//        bNumberCount.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
//                try {
//                    int b = Integer.valueOf(jtfNumberCount.getText());
//                    if ( b < 0 ) throw new  ClassCastException("Только положительное число !!");
//                    if (b > colors.getSizes().size()-1) throw new ClassCastException("Такого цвета нет на этом изображении!!");
//                    if (b == 0) {
//                        emptyWindow.getPoints().clear();
//                        emptyWindow.repaint();
//                        return;
//                    }
//                    setNumberCount( b - 1 );
//                    setPointsToEmptyWindow(getNumberCount());
//                    System.out.println("NumberCount: " + (getNumberCount() + 1) );
//                    emptyWindow.repaint();
//                } catch (ClassCastException ex) {
//                    ex.printStackTrace();
//                }
//
//            }
//        });

        btEmptyPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == 1) {

                    System.out.println(getbImage().getWidth() + " " + getbImage().getHeight());
                    emptyWindow = new EmptyWindow(getbImage().getWidth(), getbImage().getHeight(), getAnalysTitle());
                    emptyWindow.window();
                    bNumberCount.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            super.mousePressed(e);
                            try {
                                int b = Integer.valueOf(jtfNumberCount.getText());
                                if ( b < 0 ) throw new  ClassCastException("Только положительное число !!");
                                if (b > colors.getSizes().size()) throw new ClassCastException("Такого цвета нет на этом изображении!!");
                                if (b == 0) {
                                    emptyWindow.getPoints().clear();
                                    emptyWindow.repaint();
                                    return;
                                }
                                setNumberCount( b - 1 );
                                setPointsToEmptyWindow(getNumberCount());
                                System.out.println("NumberCount: " + (getNumberCount() + 1) );
                                emptyWindow.repaint();
                            } catch (ClassCastException ex) {
                                ex.printStackTrace();
                            }

                        }
                    });


//                    ArrayList<Color> cl = colors.getKeyByValue(0);
//
//                    ArrayList<Integer> aliint = colors.getCountColorsMap().get(cl.get(0));
//                    emptyWindow.getPoints().put(cl.get(0), aliint);
//
//                    cl = colors.getKeyByValue(4);
//                    System.out.println("Цвет5: " + cl);
//                    aliint = colors.getCountColorsMap().get(cl.get(0));
//                    emptyWindow.getPoints().put(cl.get(0), aliint);

                    emptyWindow.repaint();
                }
            }
        });

        qtColors.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == 1) {
//                    colors = new Colors(bImage);

                    colors.getCountColors();
//                    setTitle( String.valueOf(colors.getCountColorsMap().size()) );

//                    jp.add(panel);
                    panel.setBackground(Color.WHITE);
                    Dimension panelSize = panel.getSize();
                    System.out.println(panelSize);


                    setWidthW(getWidth());
                    setHeightW(getHeight());
                    panel.repaint();

                    setVisible(true);


                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setWidthW(getWidth());
                setHeightW(getHeight());
//                System.out.println("Width: " + getWidth() + " " + getHeight());

                panel.repaint();
            }
        });

        setTitle( String.valueOf(colors.getCountColorsMap().size()) );

        jp.add(panel);
        panel.setBackground(Color.WHITE);

        panel.repaint();
        setVisible(true);
        System.out.println(panel.getSize());
    }


    public class Panel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
//            Object[] clr = colors.getCountColorsMap().keySet().toArray();

//            System.out.println("clr: " + clr.length);
//            System.out.println(getWidthW() + " " + getHeightW());

            int x = 1, y = 1;

            int a = 0;
            for (int i = 0; i < colors.getSizes().size()-1; i++) {

                a += colors.getKeyByValue(i).size();

                for ( Color o : colors.getKeyByValue(i) ) {
//                    System.out.println("Цвет на AnalysisWindow: " + o.getRGB());
                    g.setColor(o);
                    g.fillRect(x, y, 10,10);
                    x = x + 11;
                    if (x > getWidthW()-10) {
                        x = 1;
                        y = y + 11;
                    }
                    if (y > getHeightW()-60) break;
                }
                i += colors.getKeyByValue(i).size()-1;

            }
//            System.out.println("colorsSizeKeyByValue: " + a);

            new Color(-15988736);
//            for ( int i = 0; i < clr.length; i++ ) {
//                g.setColor( (Color) clr[i]);
//                g.fillRect(x, y, 10,10);
//                x = x + 11;
//                if (x > getWidthW()-10) {
//                    x = 1;
//                    y = y + 11;
//                }
//                if (y > getHeightW()-60) break;
//            }

        }
    }

    public void setPointsToEmptyWindow(int numberCount) {
        ArrayList<Color> cl = colors.getKeyByValue(numberCount);

        ArrayList<Integer> aliint = colors.getCountColorsMap().get(cl.get(0));
        emptyWindow.getPoints().put(cl.get(0), aliint);
//        System.out.println(cl.get(0).getRGB());
    }


    public BufferedImage getbImage() {
        return bImage;
    }

    public int getWidthW() {
        return widthW;
    }

    public void setWidthW(int widthW) {
        this.widthW = widthW;
    }

    public int getHeightW() {
        return heightW;
    }

    public void setHeightW(int heightW) {
        this.heightW = heightW;
    }

    public String getAnalysTitle() {
        return title;
    }

    public int getNumberCount() {
        return numberCount;
    }

    public void setNumberCount(int numberCount) {
        this.numberCount = numberCount;
    }
}
