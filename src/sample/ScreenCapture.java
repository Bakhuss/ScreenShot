package sample;

import javafx.application.Platform;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
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

    //    private static ArrayList<BufferedImage> bi = null;
//    static TreeMap<Long, BufferedImage> bi = null;
    static HashMap<Long, BufferedImage> bi = null;

//    private static ArrayList<BufferedImage>[] bi = new ArrayList[2];

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
//    static HashMap<String, Integer> countColors;
//    static ArrayList<Integer> arrInt = null;
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


///*
//    public static void getScreen() throws Exception {
//        screening = true;
//
//        SQLHandler screenSave = new SQLHandler();
//
//
//        long t = System.currentTimeMillis();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Date dateNow = new Date();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-kk:mm:ss.SS");
//                String date = dateFormat.format(dateNow);
//                String tableName = "'" + date + "'";
//
//                String sqlNameTable = "CREATE TABLE " + tableName + " (name TEXT REFERENCES MetaData (name), img  BLOB);";
//                String sqlStr = "insert into " + tableName + " (name, img) values (?,?);";
//                System.out.println(sqlStr);
//
//
//                try {
//                    screenSave.connect();
//                    screenSave.getStmt().executeUpdate(sqlNameTable);
//                    screenSave.setPstmt(screenSave.getConnection().prepareStatement(sqlStr));
//                    screenSave.getConnection().setAutoCommit(false);
//                    int i = 0;
//                    long time = 0;
//                    time = System.currentTimeMillis();
//                    do {
//
//
////                        for (int i = 0; i < bi.size(); i++) {
////                    str = "screen/screen" + i + ".jpg";
////                    file = new File(str);
//                        try {
//                            ByteArrayOutputStream baos = null;
//                            baos = new ByteArrayOutputStream();
//                            try {
//                                ImageIO.write(new Robot().createScreenCapture(new Rectangle(xSize, ySize, width, height)), "jpg", baos);
//                            } catch (AWTException e) {
//                                e.printStackTrace();
//                            }
//                            baos.close();
//                            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//                            try {
//                                screenSave.getPstmt().setString(1, "screen" + i);
//                                screenSave.getPstmt().setBinaryStream(2, bais, baos.toByteArray().length);
//                                screenSave.getPstmt().execute();
////                                if (i % 200 == 0) screenSave.getPstmt().executeBatch();
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
//                            i++;
////                        ImageIO.write(bi[i], "jpg", file);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
////                        }
//                    } while (System.currentTimeMillis() - t < TimeScreen * 1000);
//                    screenSave.getConnection().commit();
//
//                    time = System.currentTimeMillis() - time;
//                    System.out.println("SQLsaveTimeAvrg: " + time / i);
//                    System.out.println(i);
//                    screenSave.getConnection().setAutoCommit(true);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                } finally {
//                    screenSave.disconnect();
//                    savingToSql = false;
//                    screening = false;
//                }
//
//
////                    bi.add(new Robot().createScreenCapture(new Rectangle(xSize, ySize, width, height)));
//
//
//            }
//        }).start();
//
//        screening = false;
//
//
//        new Color(-1711137);
//
//    }
//*/
//
///*
//    public static void getScreen() throws Exception {
//        screening = true;
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
//
//        setCountFrames(bi.size());
//
////        screening = false;
//
//
//        new Color(-1711137);
//
//        saveToSQLBase();
//    }
//*/

    public static void getScreen() throws Exception {
        screening = true;
        if (bi == null) bi = new HashMap<>();
        else {
            System.out.println(bi.size());
            bi.clear();
            System.out.println(bi.size());
        }
        Thread[] screenThreads = new Thread[3];

        long time = 0;
        long t = System.currentTimeMillis();

        for (int i = 0; i < screenThreads.length; i++) {
            screenThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    do {
                        try {
                            bi.put(System.currentTimeMillis(), new Robot().createScreenCapture(new Rectangle(xSize, ySize, width, height)));
                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                    } while (System.currentTimeMillis() - t < TimeScreen * 1000);
                }
            });
            screenThreads[i].start();
            System.out.println("Run " + screenThreads[i].getState().toString());
        }

        while (screenThreads[0].isAlive() & screenThreads[1].isAlive() & screenThreads[2].isAlive()) {
        }

        for ( Thread o : screenThreads ) {
            o.join();
        }

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


    public static HashMap<Long, BufferedImage> getBi() {
        return bi;
    }


    public static int getCountFrames() {
        return countFrames;
    }

    public static void setCountFrames(int countFrames) {
        ScreenCapture.countFrames = countFrames;
    }


    public static void saveToSQLBase() {
        System.out.println("SaveToSQLBase");
        savingToSql = true;

        SQLHandler saveToBase = new SQLHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Date dateNow = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-kk:mm:ss.SS");
                String date = dateFormat.format(dateNow);
                String tableName = "'" + date + "'";

//                String sqlNameTable = "CREATE TABLE " + tableName + " (name TEXT REFERENCES MetaData (name), img  BLOB);";
                String sqlStr = "insert into Photo (media_id, photo_id, image) values (?, ?, ?);";
                System.out.println(sqlStr);





                try {
                    saveToBase.connect();
                    saveToBase.setPstmt(saveToBase.getConnection().prepareStatement(sqlStr));

                    String media_type = "'photo'";

                    String sql;
                    ResultSet rs;

                    sql = "select media_type_id from Media_Type where media_type = " + media_type;
                    rs = saveToBase.getStmt().executeQuery(sql);
                    rs.next();
                    int media_type_id = rs.getInt(1);
                    System.out.println(media_type_id);

                    sql = "insert into Name (name) values (" + tableName + ");";
                    saveToBase.getStmt().execute(sql);

                    sql = "select name_id from Name where name = " + tableName;
                    rs = saveToBase.getStmt().executeQuery(sql);
                    rs.next();
                    int name_id = rs.getInt(1);
                    sql = "insert into Info (name_id) values (" + name_id + ")";
                    saveToBase.getStmt().execute(sql);
                    System.out.println("name_id: " + name_id);

                    sql = "select info_id from Info where name_id = " + name_id;
                    rs = saveToBase.getStmt().executeQuery(sql);
                    rs.next();
                    int info_id = rs.getInt(1);
                    sql = "insert into Media (info_id, media_type_id) values (" + info_id + ", " + media_type_id + ")";
                    saveToBase.getStmt().execute(sql);
                    System.out.println("info_id: " + info_id);

                    sql = "select media_id from Media where info_id = " + info_id;
                    rs = saveToBase.getStmt().executeQuery(sql);
                    rs.next();
                    int media_id = rs.getInt(1);
                    System.out.println("media_id: " + media_id);


                    saveToBase.getConnection().setAutoCommit(false);


//                    Object[] bis = bi.values().toArray();
                    System.out.println("Map.Entry");
//                    for ( Map.Entry o : bi.entrySet() ) {
//                        System.out.println( o.getKey() );
//                    }

//                    bi = null;
                    System.out.println(bi.size());
                    long time = System.currentTimeMillis();

                    for ( Map.Entry o : bi.entrySet() ) {

                        try {
                            ByteArrayOutputStream baos = null;
                            baos = new ByteArrayOutputStream();
                            ImageIO.write((BufferedImage) o.getValue(), "jpg", baos);
//                            ImageIO.write(bi.get(i), "jpg", baos);
                            baos.close();
                            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                            try {

                                String str = String.valueOf( o.getKey() );
                                String substr = str.substring(str.length()-6);


                                saveToBase.getPstmt().setInt(1, media_id);
                                saveToBase.getPstmt().setInt(2, Integer.valueOf(substr));
                                saveToBase.getPstmt().setBinaryStream(3, bais, baos.toByteArray().length);
                                saveToBase.getPstmt().addBatch();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
//                        ImageIO.write(bi[i], "jpg", file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    saveToBase.getPstmt().executeBatch();
                    time = System.currentTimeMillis() - time;
                    System.out.println("SQLsaveTimeAvrg: " + time / bi.size());
                    bi = null;
                    saveToBase.getConnection().setAutoCommit(true);





/*

                    for (int i = 0; i < bis.length; i++) {
//                    str = "screen/screen" + i + ".jpg";
//                    file = new File(str);
                        try {
                            ByteArrayOutputStream baos = null;
                            baos = new ByteArrayOutputStream();
                            ImageIO.write((BufferedImage) bis[i], "jpg", baos);
//                            ImageIO.write(bi.get(i), "jpg", baos);
                            baos.close();
                            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                            try {



                                saveToBase.getPstmt().setInt(1, media_id);
                                saveToBase.getPstmt().setInt(2, i);
                                saveToBase.getPstmt().setBinaryStream(3, bais, baos.toByteArray().length);
                                saveToBase.getPstmt().addBatch();

//                                saveToBase.getPstmt().setString(1, "screen" + i);
//                                saveToBase.getPstmt().setBinaryStream(2, bais, baos.toByteArray().length);
//                                saveToBase.getPstmt().addBatch();
//                                if (i % 200 == 0) saveToBase.getPstmt().executeBatch();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
//                        ImageIO.write(bi[i], "jpg", file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    saveToBase.getPstmt().executeBatch();
                    time = System.currentTimeMillis() - time;
                    System.out.println("SQLsaveTimeAvrg: " + time / bis.length);
                    bis = null;
                    saveToBase.getConnection().setAutoCommit(true);

*/

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    saveToBase.disconnect();
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
