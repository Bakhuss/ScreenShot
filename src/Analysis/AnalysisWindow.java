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


    public AnalysisWindow(BufferedImage bImage, String title) {
        this.bImage = bImage;
        this.title = title;
    }

    public void window() {
        colors = new Colors(bImage);
        colors.getCountColors();

        setSize(getWidthW(), getHeightW());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
//        setResizable(false);

        setWidthW(getWidth());
        setHeightW(getHeight());

        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        add(jp);
        Panel panel = new Panel();

        JPanel jpButtons = new JPanel();
        jpButtons.setLayout(new BoxLayout(jpButtons, BoxLayout.X_AXIS));
        jp.add(jpButtons);
        JButton qtColors = new JButton("Кол-во оттенков");
        jpButtons.add(qtColors);
        JButton btEmptyPanel = new JButton("Пустая панель");
        jpButtons.add(btEmptyPanel);

        JTextField jtfNumberCount = new JTextField();
        jtfNumberCount.setMaximumSize(new Dimension(50, 50));
        jpButtons.add(jtfNumberCount);
        JButton bNumberCount = new JButton("ok");
        jpButtons.add(bNumberCount);


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
                                if (b < 0) throw new ClassCastException("Только положительное число !!");
                                if (b > colors.getTempp().size())
                                    throw new ClassCastException("Такого цвета нет на этом изображении!!");
                                if (b == 0) {
                                    emptyWindow.getPoints().clear();
                                    emptyWindow.repaint();
                                    return;
                                }
                                setNumberCount(b - 1);

                                if ( setPointsToEmptyWindow(getNumberCount()) ) {
                                    emptyWindow.repaint();
                                    System.out.println("NumberCount: " + (getNumberCount() + 1));
                                }


                            } catch (ClassCastException ex) {
                                ex.printStackTrace();
                            }

                        }
                    });


                    emptyWindow.repaint();
                }
            }
        });

        qtColors.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == 1) {

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

                panel.repaint();
            }
        });

        setTitle(String.valueOf(colors.getTempp().size()));

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

            int x = 1, y = 1;

            int a = 0;
            for ( Color o : colors.getTempp().values() ) {

                g.setColor(o);
                g.fillRect(x, y, 10, 10);
                x = x + 11;
                if (x > getWidthW() - 10) {
                    x = 1;
                    y = y + 11;
                }
                if (y > getHeightW() - 60) break;


            }


            new Color(-15988736);

        }
    }

    public boolean setPointsToEmptyWindow(int numberCount) {
        ArrayList<Color> cl = new ArrayList<>(colors.getTempp().values());
        if (emptyWindow.getPoints().containsKey(cl.get(numberCount))) return false;

        ArrayList<Integer> aliint = colors.getNKeyByValue(cl.get(numberCount));
        System.out.println("aliint: " + aliint.size() + " Цвет: " + cl.get(numberCount).getRGB());
        emptyWindow.getPoints().put(cl.get(numberCount), aliint);
        return true;
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