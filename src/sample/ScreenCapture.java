package sample;

import javafx.application.Platform;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.Timer;

public class ScreenCapture {

/*

int argb = myBufferedImage.getRGB(x, y);
int alpha = (argb >> 24) & 0xff;
int red = (argb >> 16) & 0xff;
int green = (argb >>  8) & 0xff;
int blue = (argb ) & 0xff;

*/

        private static ArrayList<BufferedImage> bi = null;
//    private static ArrayList<BufferedImage>[] bi = new ArrayList[2];
    ;

    BufferedOutputStream bos = null;
    static File file = null;
    FileOutputStream fos;

    private static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    static int maxWidth = (int) size.getWidth();
    static int maxHeight = (int) size.getHeight();
    private static int width = maxWidth;
    private static int height = maxHeight;
    private static int xSize = 0;
    private static int ySize = 0;

    static String str = null;
    static HashMap<String, Integer> countColors;
    static ArrayList<Integer> arrInt = null;
    private static int TimeScreen = 0;
    private static int countFrames = 0;
    private static boolean savingToSql = false;

    static Date dateNow = null;

    private static boolean screening = false;

    private static int timeOneFrame = 40;
    static double timeProg;


//    public static void getScreen() throws Exception {
//        screening = true;
//        countFrames = 0;
//        setDateNow(new Date());
//        Thread[] thr = new Thread[2];
//
//
//        long t = System.currentTimeMillis();
//        do {
//            for (int i = 0; i < bi.length; i++) {
//                if (bi[i] == null) bi[i] = new ArrayList<>();
//                else bi[i].clear();
//            }
//            while (System.currentTimeMillis() - t < TimeScreen * 1000) {
//                for (int i = 0; i < 20; i++) {
//                    bi[0].add(new Robot().createScreenCapture(new Rectangle(getXSize(), getYSize(), getWidth(), getHeight())));
//                }
//                new SQL(bi[0]);
//            };
//
//
////            thr[0] = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    System.out.println("\nr1");
////                    new SQL(bi[0]);
//////                    try {
//////                        new SQL(bi[0]);
////////                        thr[1].join();
//////                    } catch (InterruptedException e) {
//////                        e.printStackTrace();
//////                    }
////                }
////            });
////            thr[0].start();
//
////            synchronized (countFrames) {
//            setCountFrames(getCountFrames() + bi[0].size());
////            }
//
//            while (System.currentTimeMillis() - t < TimeScreen * 1000) {
//                for (int i = 0; i < 20; i++) {
//                    bi[1].add(new Robot().createScreenCapture(new Rectangle(getXSize(), getYSize(), getWidth(), getHeight())));
//                }
//                new SQL(bi[1]);
//            };
//
//
////            System.out.println(thr[0].getState());
//
////            thr[1] = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    System.out.println("\nr2");
////
////                    try {
////                        new SQL(bi[1]);
////                        thr[0].join();
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }
////            });
////            thr[1].start();
//
////            synchronized (countFrames) {
//            setCountFrames(getCountFrames() + bi[1].size());
////            }
//
//
//        } while (System.currentTimeMillis() - t < TimeScreen * 1000);
//
//        SQLHandler.disconnect();
//        System.out.println(getCountFrames());
//        Controller.getMemoryInfo();
//
//
//
///*
//        if (bi == null) bi = new ArrayList<>();
//        else {
//            System.out.println(bi.size());
//            bi.clear();
//            System.out.println(bi.size());
//        }
//
//        long time = 0;
////        long t = System.currentTimeMillis();
//
//        long t = System.currentTimeMillis();
//
//        do {
//            bi.add(new Robot().createScreenCapture(new Rectangle(xSize, ySize, width, height)));
//        } while (System.currentTimeMillis() - t < TimeScreen * 1000);
//
//
//        time += System.currentTimeMillis() - t;
//
//        long t1 = time / bi.size();
//        System.out.println("t: " + t1);
//        System.out.println(bi.size());
//        timeOneFrame = (int) t1;
//*/
//
//
//        screening = false;
//        new Color(-1711137);
//
//    }



    public static void getScreen() throws Exception {
        screening = true;
        if (bi == null) bi = new ArrayList<>();
        else {
            System.out.println(bi.size());
            bi.clear();
            System.out.println(bi.size());
        }

        long time = 0;
//        long t = System.currentTimeMillis();

        long t = System.currentTimeMillis();

        do {
            bi.add(new Robot().createScreenCapture(new Rectangle(xSize, ySize, width, height)));
        } while (System.currentTimeMillis() - t < TimeScreen * 1000);


        time += System.currentTimeMillis() - t;

        long t1 = time / bi.size();
        System.out.println("t: " + t1);
        System.out.println(bi.size());
        timeOneFrame = (int) t1;

        setCountFrames(bi.size());

//        screening = false;


        new Color(-1711137);

        saveToSQLBase();
    }




    public static ArrayList<BufferedImage> getBi() {
        return bi;
    }


    public static int getCountFrames() {
        return countFrames;
    }

    public static void setCountFrames(int countFrames) {
        ScreenCapture.countFrames = countFrames;
    }

/*
    public static void getCountColors() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("CountColors");
                countColors = new HashMap<>();
                long time2 = System.currentTimeMillis();
                for (BufferedImage o : bi) {
                    for (int i = 0; i < o.getHeight(); i++) {
                        for (int j = 0; j < o.getWidth(); j++) {
                            int rgb = o.getRGB(j, i);
                            String str = "rgb: " + rgb;
                            if (countColors.containsKey(str)) {
                                int actualCount = countColors.get(str);
                                actualCount = actualCount + 1;
                                countColors.put(str, actualCount);
                            } else {
                                countColors.put(str, 1);
                            }
                        }

//                    int red = (rgb >> 16) & 0xff;
//                    int green = (rgb >> 8) & 0xff;
//                    int blue = (rgb) & 0xff;
//                    System.out.println(red + " " + green + " " + blue);

                    }
                }
                System.out.println("Время анализа количества цветов: " + (System.currentTimeMillis() - time2));
                getMaxCountColor();

            }
        }).start();

    }
*/


    public static void getMaxCountColor() {
        long t3 = System.currentTimeMillis();
        int max = 0;
        int r = 0;
        int r1 = 0;
        arrInt = new ArrayList<>(countColors.values());
        for (int o : arrInt) {
            if (max < o) {
                max = o;
                r1 = r;
//                System.out.println("r1: " + r1);
            }
//            if ( (r != 0) && (r % 30 == 0) ) System.out.println();
//            System.out.print(max + " | " + o + " ");
            r++;
        }
        System.out.println("Время поиска макс. цвета: " + (System.currentTimeMillis() - t3));

        String st = (String) countColors.keySet().toArray()[r1];
        System.out.println("Цвет: " + st);
        System.out.println("Количество: " + max);
    }



    public static void saveToSQLBase() {
        savingToSql = true;
        new Thread(new Runnable() {
            @Override
            public void run() {

                Date dateNow = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-kk:mm:ss.SS");
                String date = dateFormat.format(dateNow);
                String tableName = "'" + date + "'";

                String sqlNameTable = "CREATE TABLE " + tableName + " (name TEXT REFERENCES MetaData (name), img  BLOB);";
                String sqlStr = "insert into " + tableName + " (name, img) values (?,?);";
                System.out.println(sqlStr);

                try {

                    SQLHandler.connect();
                    SQLHandler.stmt.execute(sqlNameTable);
                    SQLHandler.pstmt = SQLHandler.connection.prepareStatement(sqlStr);
                    SQLHandler.connection.setAutoCommit(false);


                    long time = System.currentTimeMillis();

                    for (int i = 0; i < bi.size(); i++) {
//                    str = "screen/screen" + i + ".jpg";
//                    file = new File(str);
                        try {

                            ByteArrayOutputStream baos = null;
                            baos = new ByteArrayOutputStream();
                            ImageIO.write(bi.get(i), "jpg", baos);
                            baos.close();
                            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

//                            System.out.println("1 " + bi.size());

                            try {
                                SQLHandler.pstmt.setString(1, "screen" + i);
                                SQLHandler.pstmt.setBinaryStream(2, bais, baos.toByteArray().length);
                                SQLHandler.pstmt.addBatch();
                                if (i % 200 == 0) SQLHandler.pstmt.executeBatch();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

//                        ImageIO.write(bi[i], "jpg", file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    SQLHandler.pstmt.executeBatch();

                    time = System.currentTimeMillis() - time;
                    System.out.println("SQLsaveTime: " + time / bi.size());


                    bi = null;

                    SQLHandler.connection.setAutoCommit(true);

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    SQLHandler.disconnect();
                    savingToSql = false;
                    screening = false;
                }
            }
        }).start();
    }



    public static void setTimeScreen(int TimeScreen) {
        ScreenCapture.TimeScreen = TimeScreen;
    }

    public static int getTimeScreen() {
        return TimeScreen;
    }

    public static int getTimeOneFrame() {
        return timeOneFrame;
    }

    public static Dimension getSize() {
        return size;
    }

    public static double getTimeProg() {
        return timeProg;
    }

    public static void setTimeProg(double timeProg) {
        ScreenCapture.timeProg = timeProg;
    }

    public static int getMaxWidth() {
        return maxWidth;
    }

    public static int getMaxHeight() {
        return maxHeight;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        ScreenCapture.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        ScreenCapture.height = height;
    }

    public static int getXSize() {
        return xSize;
    }

    public static void setXSize(int xSize) {
        ScreenCapture.xSize = xSize;
    }

    public static int getYSize() {
        return ySize;
    }

    public static void setYSize(int ySize) {
        ScreenCapture.ySize = ySize;
    }

    public static boolean isScreening() {
        return screening;
    }

    public static boolean isSavingToSql() {
        return savingToSql;
    }

    public static Date getDateNow() {
        return dateNow;
    }

    public static void setDateNow(Date dateNow) {
        ScreenCapture.dateNow = dateNow;
    }
}
