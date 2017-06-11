package sample;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewWindow extends JFrame {

    int width;
    int height;
    String title;
    Image img;
    ImageIcon image;
    JPanel jp;
    ArrayList<BufferedImage> bi;
    int currentScreenCount;
    Rectangle currentJpRect;
    Settings settings = null;


    public ViewWindow(ArrayList<BufferedImage> bi, String title) {

        this.currentScreenCount = 0;
        this.bi = bi;
        this.title = title;

        newWindow();
    }

    public void newWindow() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        if (isUndecorated()) setSize(bi.get(currentScreenCount).getWidth(), bi.get(currentScreenCount).getHeight());
        else setSize(bi.get(currentScreenCount).getWidth() + 16, bi.get(currentScreenCount).getHeight() + 39);
        setLocationRelativeTo(null);

        img = bi.get(currentScreenCount);
        jp = new Panal();
        add(jp);
        currentJpRect = jp.getBounds();
        setTitle();
        repaint();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                switch (e.getKeyCode()) {
                    case 39:
                        currentScreenCount++;
                        break;
                    case 37:
                        currentScreenCount--;
                        break;
                    default:
                        return;
                }

                if (currentScreenCount < 0) {
                    currentScreenCount = 0;
                    return;
                }
                if (currentScreenCount > bi.size() - 1) {
                    currentScreenCount = bi.size() - 1;
                    return;
                }
                repaint();
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);

                switch ((int) e.getPreciseWheelRotation()) {
                    case -1:
                        currentScreenCount++;
                        break;
                    case 1:
                        currentScreenCount--;
                        break;
                    default:
                        return;
                }

                if (currentScreenCount < 0) {
                    currentScreenCount = 0;
                    return;
                }
                if (currentScreenCount > bi.size() - 1) {
                    currentScreenCount = bi.size() - 1;
                    return;
                }
                repaint();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (settings != null) settings.dispose();
                bi.clear();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                settings.dispose();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if (e.getButton() == 1 && settings != null) settings.dispose();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if (e.getButton() == 3) {
                    if (settings != null) settings.dispose();
                    int x = e.getComponent().getX() + e.getX();
                    int y = e.getComponent().getY() + e.getY();
                    settings = new Settings(x, y, e.getComponent());
                }
            }
        });

        setVisible(true);
    }

    public void setTitle() {
        String fullSize = "full " + bi.get(currentScreenCount).getWidth() + "x" + bi.get(currentScreenCount).getHeight();
        String currentSize = "current " + currentJpRect.width + "x" + currentJpRect.height;

        setTitle("fr: " + (currentScreenCount + 1) + " | " + title + " Size: " + fullSize + " | " + currentSize);
    }


    public class Panal extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            img = bi.get(currentScreenCount);
            currentJpRect = jp.getBounds();
            setTitle();
            g.drawImage(img, 0, 0, currentJpRect.width, currentJpRect.height, null);
        }
    }

    public class Settings extends JFrame {

        public Settings(int x, int y, Component component) {

            setBounds(x, y, 110, 75);
            setUndecorated(true);
            JPanel jpSettings = new JPanel();
            jpSettings.setLayout(new BoxLayout(jpSettings, BoxLayout.Y_AXIS));
            add(jpSettings);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowDeactivated(WindowEvent e) {
                    super.windowDeactivated(e);

                    settings.dispose();
                }
            });

            JLabel[] jlSettings = new JLabel[3];
            for (int i = 0; i < jlSettings.length; i++) {
                jlSettings[i] = new JLabel();
                jlSettings[i].setFont(new Font("name", Font.ITALIC, 13));
                jpSettings.add(jlSettings[i]);
            }
            jlSettings[0].setText("Delete all frames");
            jlSettings[1].setText(" ");
            jlSettings[2].setText("Delete this frame");

            jlSettings[0].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);

                    if (e.getButton() == 1) {
                        JFrame frame = (JFrame) component;

                        try {
                            String sqlQuery = "drop table '" + title + "';";
                            SQLHandler.connect();
                            System.out.println("Delete " + title);


//                            try {
//                                Thread.sleep(50);
//                            } catch (InterruptedException e1) {
//                                e1.printStackTrace();
//                            }

                            if (SQLHandler.connection.isClosed()) SQLHandler.connect();
//                            SQLHandler.pstmt.close();
                            System.out.println("Connection closed");

                            if (SQLHandler.connection.isValid(1)) {
                                SQLHandler.disconnect();
                                SQLHandler.connect();
                                SQLHandler.pstmt.close();
                            }

                            SQLHandler.stmt.executeUpdate(sqlQuery);
                            ResultSet res = SQLHandler.stmt.executeQuery("select * from sqlite_master where type = 'table' and name = '" + title + "';");


                            if (res.next()) {
                                System.out.println("Не удалось удалить. Попробуйте снова.");
                            }


//                            if (!res.next()) {
//                                settings.dispose();
//                                frame.dispose();
//                            } else {
//                                System.out.println("Не удалось удалить. Попробуйте снова.");
//                            }



                            res.close();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        } finally {
                            SQLHandler.disconnect();
                            settings.dispose();
                            frame.dispose();
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);

                    jlSettings[0].setFont(new Font("Name", Font.BOLD, 13));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);

                    jlSettings[0].setFont(jlSettings[1].getFont());
                }
            });

            jlSettings[2].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    jlSettings[2].setFont(new Font("Name", Font.BOLD, 13));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    jlSettings[2].setFont(jlSettings[1].getFont());
                }
            });

            setVisible(true);

        }
    }

}
