package Analysis;


import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import sample.SQLHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class Colors {

//                    int alpha = (rgb >> 24) & 0xff;
//                    int red = (rgb >> 16) & 0xff;
//                    int green = (rgb >> 8) & 0xff;
//                    int blue = (rgb) & 0xff;
//                    System.out.println(red + " " + green + " " + blue + " " + alpha);
//                    rgb = blue | (green << 8) | (red << 16) | (alpha << 24);
//                    System.out.println(rbg);


    private HashMap<ArrayList<Integer>, Color> countColors = null;
    private TreeMap<ArrayList<Integer>, Color> tempp = null;

    private ArrayList<Color> pixelColors = null;
    private ArrayList<ArrayList<Integer>> pixelsPoint = null;

    private BufferedImage bi;
    private String title;
    private int photo_id = 0;


    public Colors() {
    }

    public Colors(BufferedImage bi, int photo_id) {
        this.bi = bi;
        this.photo_id = photo_id;
    }


    public void getCountColors2() {
        System.out.println("Colors2");

        int Tread_Count = Runtime.getRuntime().availableProcessors() - 1;
        if (Tread_Count == 0) Tread_Count = 1;
        System.out.println("Tread_Count: " + Tread_Count);
        Thread[] trd = new Thread[Tread_Count];
        SQLHandler[] sendColorsToSQL = new SQLHandler[Tread_Count];


        long time = System.currentTimeMillis();
        for (int i = 0; i < trd.length; i++) {
            final int w = i;
            final int tread_count = Tread_Count;
            trd[i] = new Thread(new Runnable() {
                @Override
                public void run() {

                    int height = bi.getHeight() / tread_count;
                    int pixelNumber = w * height * bi.getWidth() + 1;
                    int iBefor = (w + 1) * height;
                    if (w == trd.length - 1) iBefor = bi.getHeight();

                    try {
                        String sqlStr = "insert into Pixels (photo_id, pixel_number, color) values (?, ?, ?);";
                        sendColorsToSQL[w] = new SQLHandler();
                        sendColorsToSQL[w].connect();
                        sendColorsToSQL[w].setPstmt(sendColorsToSQL[w].getConnection().prepareStatement(sqlStr));
                        sendColorsToSQL[w].getConnection().setAutoCommit(false);
                        for (int i = w * height; i < iBefor; i++) {
                            for (int j = 0; j < bi.getWidth(); j++) {

                                sendColorsToSQL[w].getPstmt().setInt(1, getPhoto_id());
                                sendColorsToSQL[w].getPstmt().setInt(2, pixelNumber);
                                sendColorsToSQL[w].getPstmt().setInt(3, bi.getRGB(j, i));
                                sendColorsToSQL[w].getPstmt().addBatch();
                                pixelNumber++;
//                                sendColorsToSQL[w].getPstmt().executeBatch();
                            }
                        }

//                        sendColorsToSQL[w].getConnection().setAutoCommit(true);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
//                        System.out.println("Disconnect");
//                        sendColorsToSQL[w].disconnect();
                    }

                }
            });
            trd[i].start();
        }

        for (Thread o : trd) {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for ( SQLHandler o : sendColorsToSQL ) {
            try {
                o.getPstmt().executeBatch();
                o.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Disconnect");
                o.disconnect();
            }
        }

        System.out.println("Время записи в БД из Colors2: " + (System.currentTimeMillis() - time));
    }



    public void getCountColors() {
        System.out.println("Colors");
        setCountColorsMap(new HashMap<>());

        tempp = new TreeMap<>(new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                if (o1.size() == o2.size()) return -1;
                return (o2.size() - o1.size());
            }
        });

        long time2 = System.currentTimeMillis();
        int Tread_Count = Runtime.getRuntime().availableProcessors() - 1;
        if (Tread_Count == 0) Tread_Count = 1;
        System.out.println("Tread_Count: " + Tread_Count);
        Thread[] trd = new Thread[Tread_Count];
        HashMap<ArrayList<Integer>, Color>[] tempCountColorsMap = new HashMap[Tread_Count];
        for (int i = 0; i < trd.length; i++) {
            final int w = i;
            final int tread_count = Tread_Count;
            trd[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    int height = bi.getHeight() / tread_count;
                    tempCountColorsMap[w] = new HashMap<>();
                    int pixelNumber = w * height * bi.getWidth() + 1;
                    int iBefor = (w + 1) * height;
                    if (w == trd.length - 1) iBefor = bi.getHeight();
                    for (int i = w * height; i < iBefor; i++) {
                        for (int j = 0; j < bi.getWidth(); j++) {
                            Color rgbC = new Color(bi.getRGB(j, i));

                            if (!tempCountColorsMap[w].containsValue(rgbC)) {
                                tempCountColorsMap[w].put(new ArrayList<>(), rgbC);
                            }
                            getKeyByValue(tempCountColorsMap[w], rgbC).add(pixelNumber);
                            pixelNumber++;

                            if (tempCountColorsMap[w].size() % 15000 == 0) {
                                System.out.println("Size " + w + ": " + tempCountColorsMap[w].size());
                            }
                        }
                    }
                }
            });
            trd[i].start();
        }
        for (Thread o : trd) {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Время анализа количества цветов: " + (System.currentTimeMillis() - time2));


/*

        String tableName = "'" + "Colors_RGB_" + title + "'";
        String sqlNameTable = "CREATE TABLE " + tableName + " (Color INT, PixelNumber INT);";
        String sqlStr = "insert into " + tableName + " (Color, PixelNumber) values (?,?);";
        SQLHandler sendColorsToSQL = new SQLHandler();
        long time = System.currentTimeMillis();
        try {
            sendColorsToSQL.connect();
            System.out.println("Кол-во подключений к БД: " + SQLHandler.getConnectionCount());
            sendColorsToSQL.getStmt().executeUpdate(sqlNameTable);
            sendColorsToSQL.setPstmt(sendColorsToSQL.getConnection().prepareStatement(sqlStr));

            Object[] tempColors = tempCountColorsMap[0].values().toArray();
            System.out.println(tempColors.length);
            for (int i = 0; i < tempColors.length; i++) {
                ArrayList<Integer> tempArray = getKeyByValue(tempCountColorsMap[0], (Color) tempColors[i]);
                for (int j = 0; j < tempArray.size(); j++) {
                    sendColorsToSQL.getConnection().setAutoCommit(false);
                    sendColorsToSQL.getPstmt().setInt(1, ((Color) tempColors[i]).getRGB());
                    sendColorsToSQL.getPstmt().setInt(2, tempArray.get(j));
                    sendColorsToSQL.getPstmt().addBatch();
                }
                sendColorsToSQL.getPstmt().executeBatch();
            }
            sendColorsToSQL.getConnection().setAutoCommit(true);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sendColorsToSQL.disconnect();
        }
        System.out.println("Время записи цветов в БД: " + (System.currentTimeMillis() - time));


*/

        getCountColors2();

        long t2 = System.currentTimeMillis();
        for (HashMap<ArrayList<Integer>, Color> o : tempCountColorsMap) {
            Object[] obj = o.values().toArray();
            for (Object b : obj) {
                Color rgbC = (Color) b;
                if (!getCountColorsMap().containsValue(rgbC)) {
                    getCountColorsMap().put(new ArrayList<>(), rgbC);
                }
                getKeyByValue(getCountColorsMap(), rgbC).addAll(getKeyByValue(o, rgbC));
            }
            o = null;
        }
        System.out.println("Время объединения временных массивов в постоянный: " + (System.currentTimeMillis() - t2));

//        int pixelNumber = 1;
//        System.out.println("bi: " + bi.getWidth() + " " + bi.getHeight());
//        for (int i = 0; i < bi.getHeight(); i++) {
//            for (int j = 0; j < bi.getWidth(); j++) {
//                Color rgbC = new Color(bi.getRGB(j, i));
//
//                if (!getCountColorsMap().containsValue(rgbC)) {
//                    getCountColorsMap().put(new ArrayList<>(), rgbC);
//                }
//                getKeyByValue(rgbC).add(pixelNumber);
//                pixelNumber++;
//
//                if (getCountColorsMap().size() % 30000 == 0) {
//                    System.out.println("Size: " + getCountColorsMap().size());
//                }
//
//            }
//        }

        tempp.putAll(getCountColorsMap());

//        File file = new File("tempp.txt");
//        if (!file.exists()) try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        FileOutputStream fos;
//        ObjectOutputStream oos;
//        try {
//            oos = new ObjectOutputStream(new FileOutputStream("tempp.txt"));
//            oos.writeObject(getCountColorsMap());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        System.out.println(pixelNumber - 1);

        System.out.println(getCountColorsMap().size());
        setCountColorsMap(null);


        new Color(-1315600);


    }


    public static Point getPoint(int pixelNumber, int imageWidth) {
        int x, y;
        y = Math.abs((pixelNumber - 1) / imageWidth);
        x = (pixelNumber - 1) - y * imageWidth;

        return new Point(x, y);
    }


    public ArrayList<Integer> getKeyByValue(HashMap<ArrayList<Integer>, Color> hashMap, Color color) {

        Set<Map.Entry<ArrayList<Integer>, Color>> entrySet = hashMap.entrySet();
        for (Map.Entry<ArrayList<Integer>, Color> pair : entrySet) {
            if (pair.getValue().equals(color)) return pair.getKey();
        }
        return new ArrayList<>();
    }


    public ArrayList<Integer> getNKeyByValue(Color color) {

        Set<Map.Entry<ArrayList<Integer>, Color>> entrySet = getTempp().entrySet();
        for (Map.Entry<ArrayList<Integer>, Color> pair : entrySet) {
            if (pair.getValue().equals(color)) return pair.getKey();
        }
        return new ArrayList<>();
    }


    public void setCountColorsMap(HashMap<ArrayList<Integer>, Color> countColors) {
        this.countColors = countColors;
    }

    public HashMap<ArrayList<Integer>, Color> getCountColorsMap() {
        return countColors;
    }

    public TreeMap<ArrayList<Integer>, Color> getTempp() {
        return tempp;
    }

    public void setTempp(TreeMap<ArrayList<Integer>, Color> tempp) {
        this.tempp = tempp;
    }

    public int getPhoto_id() {
        return photo_id;
    }
}
