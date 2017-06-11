package sample;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLHandler {

    public static Connection connection;
    public static Statement stmt;
    public static PreparedStatement pstmt;
    public static String urlDB = null;
    public static String urlJDBC = null;
    static File file;
    static File fileDB;

    public static void connect() throws SQLException {

//        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/imagedb", "root", "sqlpassword");
        file = new File(" ");
        String nameDB = "SQLiteScreenShots.db";
        System.out.println(file.getPath() + " | " + file.getAbsolutePath());
        fileDB = new File(nameDB);
        System.out.println(fileDB.exists());

        urlDB = "jdbc:sqlite:SQLiteScreenShots.db";

        if (!fileDB.exists()) {
            try {
                createDB();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(fileDB.exists());

        connection = DriverManager.getConnection(urlDB);
        stmt = connection.createStatement();
        createTable("MetaData");
    }

    public static void disconnect() {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void createDB() throws IOException, SQLException {
        fileDB.createNewFile();
        System.out.println("Создание дб");
    }

    public static void createTable(String tblName) throws SQLException {
        String tableName = tblName;
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                     " name TEXT NOT NULL,\n" +
                     " date TEXT NOT NULL,\n" +
                     " CountColors BIGINT\n" +
                     ");";

        stmt.execute(sql);
    }



    public static void getAllTables() throws SQLException {
        ResultSet res = SQLHandler.stmt.executeQuery("select name from sqlite_master where type = 'table';");
        ResultSet resFrames = null;

        while (res.next()) {
            if (res.getString(1).equals("MetaData")) continue;
            ViewFromDB table = new ViewFromDB();
            table.setName(res.getString(1));

            String sqlQuery = "'" + table.getName() + "'";
            SQLHandler.pstmt = connection.prepareStatement("select count(*) from " + sqlQuery + ";");
            resFrames = SQLHandler.pstmt.executeQuery();

            table.setFrames(resFrames.getInt(1));
            Controller.ScreenShots.add(table);
            resFrames.close();
        }
        res.close();

    }
}
