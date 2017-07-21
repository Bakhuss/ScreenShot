package sample;


import Analysis.AnalysisWindow;
import Analysis.Colors;
import javafx.scene.input.KeyCode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewWindow extends JFrame {

    int width;
    int height;
    String title;
    int name_id;
    int info_id;
    int media_id;
    Image img;
    ImageIcon image;
    boolean hasEmptyPhoto = true;

    ArrayList<BufferedImage> bi;
    int currentScreenCount;
    Rectangle currentJpRect;
    Settings settings = null;
    AnalysisWindow analysisWindow = null;
    Panal jp = new Panal();


    public ViewWindow(ArrayList<BufferedImage> bi, String title, int name_id, int info_id, int media_id) {

        this.currentScreenCount = 0;
        this.bi = bi;
        this.title = title;
        this.name_id = name_id;
        this.info_id = info_id;
        this.media_id = media_id;

        newWindow();
        classMethod();
        System.out.println("Analysis");
        analysis();
    }

    public void newWindow() {
        System.out.println("ViewWindow title: " + title);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        if (isUndecorated()) setSize(bi.get(currentScreenCount).getWidth(), bi.get(currentScreenCount).getHeight());
        else setSize(bi.get(currentScreenCount).getWidth() + 16, bi.get(currentScreenCount).getHeight() + 39);
        setLocationRelativeTo(null);

        img = bi.get(currentScreenCount);
//        jp = new Panal();
        add(jp);
        currentJpRect = jp.getBounds();
        setTitle();
        repaint();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                System.out.println(e.getKeyCode());
                if (e.isControlDown() && e.getKeyCode()==65) {
                    System.out.println("Ctrl + A");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String sql = "select photo_id, image from Photo where photo_id = (\n" +
                                    "    select photo_id from Photo where media_id = " + getMedia_id() + "\n" +
                                    "    except\n" +
                                    "    select Pixels.photo_id from Pixels limit 1\n" +
                                    ");";
                            BufferedImage biTemp = null;
                            InputStream is = null;
                            Colors colors = null;

                            SQLHandler getPhotoIdFromSQL = new SQLHandler();

                            try {
                                getPhotoIdFromSQL.connect();
                                getPhotoIdFromSQL.getStmt().execute("PRAGMA journal_mode = WAL;");
                                while (hasEmptyPhoto) {
                                    ResultSet rs = getPhotoIdFromSQL.getStmt().executeQuery(sql);
                                    if (rs.next()) {
                                        int photo_id = rs.getInt(1);
                                        System.out.println("photo_id: " + photo_id);
//                                        sql = "select photo_id, image from Photo where photo_id = " + photo_id + ";";
//                                        rs = getPhotoIdFromSQL.getStmt().executeQuery(sql);
                                        is = rs.getBinaryStream(2);
                                        try {
                                            biTemp = ImageIO.read(is);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        colors = new Colors(biTemp, photo_id);
                                        colors.getCountColors2();
                                    } else hasEmptyPhoto = false;
                                }
                                System.out.println("Анализ всех изображений для media_id = " + getMedia_id() + " завершён");
                                getPhotoIdFromSQL.getStmt().execute("PRAGMA journal_mode = DELETE;");
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                                System.out.println("Возникла проблема при анализе всех изображение для media_id = " + getMedia_id());
                            } finally {

                                getPhotoIdFromSQL.disconnect();
                            }

                        }
                    }).start();

                } else {
                    if (e.getKeyCode() == 65) {
                        System.out.println("e.getKeyCode(): " + e.getKeyCode());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int photo_id = 0;
                                String sql = "select photo_id from Photo where media_id = " + getMedia_id() + " order by photo_id asc limit " + currentScreenCount + ",1;";
                                SQLHandler getPhotoIdFromSQL = new SQLHandler();

                                try {
                                    getPhotoIdFromSQL.connect();
                                    ResultSet rs = getPhotoIdFromSQL.getStmt().executeQuery(sql);
                                    rs.next();
                                    photo_id = rs.getInt(1);
                                    System.out.println("photo_id: " + photo_id);

                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                } finally {
                                    getPhotoIdFromSQL.disconnect();
                                }

                                analysisWindow = new AnalysisWindow(getBi().get(currentScreenCount), photo_id);
                                analysisWindow.window();
                            }
                        }).start();

                    }
                }



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
                if (analysisWindow != null) analysisWindow.dispose();
                bi.clear();
                Controller.getTablesName().remove(title);
                Controller.getTablesName().trimToSize();
                System.out.println("tablesNameSize: " + Controller.getTablesName().size());
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
        SQLHandler dropFrame;
        SQLHandler dropTable;
        JFrame frame;

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
            jlSettings[0].setText("Delete this frame");
            jlSettings[1].setText(" ");
            jlSettings[2].setText("Delete all frames");

            jlSettings[0].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);

                    if (e.getButton() == 1) {
                        frame = (JFrame) component;
                        deleteCurrentFrame();
                        settings.dispose();
                        if (getBi().size() < 1) frame.dispose();
                        else jp.repaint();
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

                    if (e.getButton() == 1) {
                        frame = (JFrame) component;
                        deleteAllFrames();
                        settings.dispose();
                        frame.dispose();
                    }
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

        void deleteCurrentFrame() {
            System.out.println("Delete current Frame " + title + " | " + (currentScreenCount+1) );
            dropFrame = new SQLHandler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String sqlQuery = "select rowid, photo_id from Photo where media_id = " + getMedia_id() + " order by photo_id asc limit " + currentScreenCount + ",1;";
                    ResultSet res = null;
                    long time = System.currentTimeMillis();
                    try {
                        dropFrame.connect();
                        System.out.println("TransactionIsolation: " + dropFrame.getConnection().getTransactionIsolation());
                        res = dropFrame.getStmt().executeQuery(sqlQuery);
                        res.next();
                        int rowid = res.getInt(1);
                        int photo_id = res.getInt(2);

                        sqlQuery = "select count(*) from Pixels where photo_id = " + photo_id + ";";
                        res = dropFrame.getStmt().executeQuery(sqlQuery);
                        res.next();
                        System.out.println("count(*) from Pixels where photo_id = " + photo_id + ": " + res.getInt(1));
                        if (res.getInt(1) != 0) {
                            sqlQuery = "delete from Pixels where photo_id = " + photo_id + ";";
                            dropFrame.getStmt().execute(sqlQuery);
                        }

                        sqlQuery = "delete from Photo where rowid = " + rowid + ";";
                        dropFrame.getStmt().execute(sqlQuery);

                        getBi().remove(currentScreenCount);
                        System.out.println("BI: " + getBi().size());
                        if (currentScreenCount > getBi().size()-1) currentScreenCount = getBi().size()-1;
                        if (currentScreenCount < 0) currentScreenCount = 0;
                        System.out.println("currentScreenCount after delete frame: " + (currentScreenCount+1) );
                        System.out.println("Метод deleteCurrentFrame() выполнен успешно:\n" +
                                           "запись " + (currentScreenCount+1) + " | " + title + " удалена из БД.");

                    } catch (SQLException e) {
                        System.out.println("Ошибка при выполнении метода deleteCurrentFrame():\n" +
                                           "запись " + (currentScreenCount+1) + " | " + title + " не удалось удалить из БД.");
                        e.printStackTrace();
                    } finally {
                        dropFrame.disconnect();
                    }
                    System.out.println("Время работы метода deleteCurrentFrame(): " + (System.currentTimeMillis() - time) );
                }
            }).start();
        }

        void deleteAllFrames() {
            System.out.println("deleteAllFrames");
            System.out.println("Name: " + getName_id() + " Info: " + getInfo_id() + " Media: " + getMedia_id());

            String sqlPhotoId = "select distinct Photo.photo_id from Photo\n" +
                    "    inner join Pixels on Photo.photo_id = Pixels.photo_id\n" +
                    "    where media_id = " + getMedia_id() + ";";
            String sqlPixels = "delete from Pixels where photo_id in (?);";


            String sqlPhoto = "delete from Photo where media_id = " + getMedia_id();
            String sqlMedia = "delete from Media where media_id = " + getMedia_id();
            String sqlInfo = "delete from Info where info_id = " + getInfo_id();
            String sqlName = "delete from Name where name_id = " + getName_id();

            dropTable = new SQLHandler();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    try {
                        dropTable.connect();
                        System.out.println("Delete Table: " + title);
//                        dropTable.setPstmt(dropTable.getConnection().prepareStatement(sqlPixels));
                        ResultSet rs = dropTable.getStmt().executeQuery(sqlPhotoId);
                        String sqlPhotoIdInPixels = "n";
                        while (rs.next()) {
                            sqlPhotoIdInPixels = sqlPhotoIdInPixels + rs.getInt(1) + ",";
                        }
                        if (sqlPhotoIdInPixels.length() != 1) {
                            sqlPhotoIdInPixels = sqlPhotoIdInPixels.substring(1, sqlPhotoIdInPixels.length() - 1);
                            System.out.println("sqlPhotoIdInPixels: " + sqlPhotoIdInPixels);
                            String sqlDeletePixels = "delete from Pixels where photo_id in(" + sqlPhotoIdInPixels + ");";
                            dropTable.getStmt().execute(sqlDeletePixels);
                        }
                        System.out.println("sqlPhotoIdInPixels.length(): " + sqlPhotoIdInPixels.length());

                        dropTable.getStmt().execute(sqlPhoto);
                        dropTable.getStmt().execute(sqlMedia);
                        dropTable.getStmt().execute(sqlInfo);
                        dropTable.getStmt().execute(sqlName);
                        System.out.println("Метод deleteAllFrames() выполнен успешно:\n" +
                                           "запись " + title + " удалена из БД.");
                    } catch (SQLException e) {
                        System.out.println("Ошибка при выполнении метода deleteAllFrames():\n" +
                                           "запись " + title + " не удалось удалить из БД.");
                        e.printStackTrace();
                    } finally {
                        dropTable.disconnect();
                        System.out.println("Title from deleteAllFrames()" + title);
                        Controller.getTablesName().remove(title);
                    }
                    System.out.println("Время работы метода deleteAllFrames(): " + (System.currentTimeMillis() - time) );
                }
            }).start();

        }
    }

    public ArrayList<BufferedImage> getBi() {
        return bi;
    }

    public void classMethod() {
        System.out.println("getFrames: " + ViewWindow.getFrames().length);
        for (Frame o : ViewWindow.getFrames()) {
            System.out.println(o.getTitle());
        }
    }


    public void analysis() {
//        Colors colors = new Colors(getBi());
//        colors.getCountColors(0);
    }

    public void color() {
        new Color(-6968132);
    }


    public int getMedia_id() {
        return media_id;
    }

    public int getName_id() {
        return name_id;
    }

    public int getInfo_id() {
        return info_id;
    }

}
