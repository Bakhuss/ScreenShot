package sample;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLHandler {

    private Connection connection;
    private Statement stmt;
    private PreparedStatement pstmt;
    public static String urlDB = null;
    public static String urlJDBC = null;
    static File file;
    static File fileDB;
    private static int connectionCount = 0;

    public void connect() throws SQLException {
        setConnectionCount(getConnectionCount()+1);

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

    public void disconnect() {

        try {
            connection.close();
            setConnectionCount(getConnectionCount()-1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createDB() throws IOException, SQLException {
        fileDB.createNewFile();
        System.out.println("Создание дб");
    }

    public void createTable(String tblName) throws SQLException {
        String tableName = tblName;
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                     " name TEXT NOT NULL,\n" +
                     " date TEXT NOT NULL,\n" +
                     " Colors BIGINT\n" +
                     ");";

        stmt.execute(sql);
    }



    public void getAllTables() throws SQLException {
        ResultSet res = stmt.executeQuery("select name from sqlite_master where type = 'table';");
        ResultSet resFrames = null;

        while (res.next()) {
            if (res.getString(1).equals("MetaData")) continue;
            ViewFromDB table = new ViewFromDB();
            table.setName(res.getString(1));

            String sqlQuery = "'" + table.getName() + "'";
            pstmt = connection.prepareStatement("select count(*) from " + sqlQuery + ";");
            resFrames = pstmt.executeQuery();

            table.setFrames(resFrames.getInt(1));
            Controller.ScreenShots.add(table);
            resFrames.close();
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
