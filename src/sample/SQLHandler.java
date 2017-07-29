package sample;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import SQLHandler.*;

public class SQLHandler {

    private Connection connection;
    private Statement stmt;
    private PreparedStatement pstmt;
    public static String urlDB = null;
    public static String urlJDBC = null;
    static File file;
    static File fileDB;
    static String nameDB = "SQLiteScreenShots.db";
    private static int connectionCount = 0;

    public void connect() throws SQLException {

        setConnectionCount(getConnectionCount() + 1);

//        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/imagedb", "root", "sqlpassword");
        file = new File(" ");
        System.out.println(file.getPath() + " | " + file.getAbsolutePath());

        connection = DriverManager.getConnection(DataBaseFile.getUrlDB());

        stmt = connection.createStatement();

    }

    public void disconnect() {

        try {
            connection.close();
            setConnectionCount(getConnectionCount() - 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void createDB() throws IOException, SQLException {
        fileDB.createNewFile();
        System.out.println("Создание дб");
    }


    public void getAllTables() throws SQLException {
        String sql = "select name, count_frames from Info\n" +
                "      inner join Name on Info.name_id = Name.name_id\n" +
                "      inner join Media on Info.info_id = Media.info_id;";
        ResultSet res = stmt.executeQuery(sql);

        while (res.next()) {
            ViewFromDB table = new ViewFromDB();
            table.setName(res.getString(1));
            table.setFrames(res.getInt(2));
            Controller.ScreenShots.add(table);
        }
        res.close();

    }

    public static int getConnectionCount() {
        return connectionCount;
    }

    public static void setConnectionCount(int connectionCount) {
        SQLHandler.connectionCount = connectionCount;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public PreparedStatement getPstmt() {
        return pstmt;
    }

    public void setPstmt(PreparedStatement pstmt) {
        this.pstmt = pstmt;
    }

}
