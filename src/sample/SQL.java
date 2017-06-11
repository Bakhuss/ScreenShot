package sample;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SQL {

    public SQL(ArrayList<BufferedImage> bi) {

        Thread[] threads = new Thread[1];

        Date dateNow = ScreenCapture.getDateNow();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-kk:mm:ss.SS");
        String date = dateFormat.format(dateNow);
        String tableName = "'" + date + "'";

        String sqlNameTable = "CREATE TABLE IF NOT EXISTS " + tableName + " (name TEXT REFERENCES MetaData (name), img  BLOB);";
        String sqlStr = "insert into " + tableName + " (name, img) values (?,?);";
        System.out.println(sqlStr);

        try {
            SQLHandler.connect();

            SQLHandler.stmt.execute(sqlNameTable);
            SQLHandler.pstmt = SQLHandler.connection.prepareStatement(sqlStr);
            SQLHandler.connection.setAutoCommit(false);

            long time = System.currentTimeMillis();
            for (int i = 0; i < bi.size(); i++) {
                ByteArrayOutputStream baos = null;
                ByteArrayInputStream bais = null;
//                    str = "screen/screen" + i + ".jpg";
//                    file = new File(str);
                try {

                    baos = new ByteArrayOutputStream();
                    ImageIO.write(bi.get(i), "jpg", baos);
                    baos.close();
                    bais = new ByteArrayInputStream(baos.toByteArray());

//                  System.out.println("1 " + bi.size());

//                  ImageIO.write(bi[i], "jpg", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                  System.out.println("2 " + bi.size());

                SQLHandler.pstmt.setString(1, "screen" + i);
                SQLHandler.pstmt.setBinaryStream(2, bais, baos.toByteArray().length);
                SQLHandler.pstmt.addBatch();
            }

            SQLHandler.pstmt.executeBatch();
            SQLHandler.connection.setAutoCommit(true);
            SQLHandler.pstmt.close();
            time = System.currentTimeMillis() - time;
            System.out.println("SQLsaveTime: " + time / bi.size());





        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Disconnect");
//            SQLHandler.disconnect();
        }

    }

}
