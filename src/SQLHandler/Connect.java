package SQLHandler;

import java.io.File;
import java.sql.*;

public class Connect {

    private Connection connection;
    private Statement stmt;
    private PreparedStatement pstmt;
    public static String urlDB = null;
    static File fileDB;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(DataBaseFile.getUrlDB());
        stmt = connection.createStatement();



    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
