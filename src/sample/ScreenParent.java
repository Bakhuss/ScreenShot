package sample;


import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ScreenParent {
    static JFrame frame;
    private static JPanel jp;
    private static MouseMotionListener screenParentMotion;

    private static int startX, startY;
    private static int endX, endY;


    public static void getScreenParent() {

        startX = ScreenCapture.getXSize();
        startY = ScreenCapture.getYSize();
        endX = startX + ScreenCapture.getWidth();
        endY = startY + ScreenCapture.getHeight();

        frame = new JFrame();
        jp = new Panal();

        if (!frame.isVisible()) {

            Main.getPrimaryStage().setIconified(true);
            frame.setSize(ScreenCapture.getSize());
            frame.setResizable(false);
            frame.setUndecorated(true);

            frame.setOpacity(0.5f);
            frame.setBackground(new Color(0, 0, 0, 100));

            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == 27) {  // KeyCode 27 == Esc

                        frame.dispose();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(e.getKeyCode());
                                Main.primaryStage.setIconified(false);
                            }
                        });
                    }
                }
            });

            jp.setOpaque(false);
            frame.add(jp);
            frame.setAlwaysOnTop(true);

            frame.setVisible(true);
        }

        frame.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
//                System.out.println(e.getLocationOnScreen());
                startX = e.getXOnScreen();
                startY = e.getYOnScreen();
                frame.addMouseMotionListener(screenParentMotion);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getXOnScreen() == getStartX() && e.getYOnScreen() == getStartY()) return;
                frame.removeMouseMotionListener(screenParentMotion);
                frame.dispose();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Main.primaryStage.setIconified(false);
                    }
                });
            }
        });


        screenParentMotion = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
//                System.out.println(e.getLocationOnScreen());
                endX = e.getXOnScreen();
                endY = e.getYOnScreen();
                jp.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };
    }

    public static class Panal extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.CYAN);
            g.fillRect(startX, startY, endX - startX, endY - startY);
        }
    }

    public static int getStartX() {
        return startX;
    }

    public static int getStartY() {
        return startY;
    }

    public static int getEndX() {
        return endX;
    }

    public static int getEndY() {
        return endY;
    }
}
