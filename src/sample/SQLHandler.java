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
        String sql;

        sql = "CREATE TABLE IF NOT EXISTS Name (\n" +
                "    name_id       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          UNIQUE\n" +
                "                          NOT NULL,\n" +
                "    name          TEXT,\n" +
                "    first_name_id INTEGER REFERENCES First_Name (first_name_id),\n" +
                "    last_name_id  INTEGER REFERENCES Last_Name (last_name_id),\n" +
                "    surname_id    INTEGER REFERENCES Surname (surname_id) \n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Info (\n" +
                "    info_id INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                    UNIQUE\n" +
                "                    NOT NULL,\n" +
                "    name_id INTEGER REFERENCES Name (name_id) \n" +
                "                    NOT NULL\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Media (\n" +
                "    media_id      INTEGER NOT NULL\n" +
                "                          UNIQUE\n" +
                "                          PRIMARY KEY AUTOINCREMENT,\n" +
                "    info_id       INTEGER REFERENCES Info (info_id) \n" +
                "                          NOT NULL,\n" +
                "    media_type_id INTEGER REFERENCES Media_Type (media_type_id) \n" +
                "                          NOT NULL,\n" +
                "    count_frames  INTEGER\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Photo (\n" +
                "    media_id INTEGER REFERENCES Media (media_id) \n" +
                "                     NOT NULL,\n" +
                "    photo_id INT     NOT NULL,\n" +
                "    image    BLOB    NOT NULL\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TRIGGER IF NOT EXISTS set_media_count_frames1\n" +
                "         AFTER INSERT\n" +
                "            ON Photo\n" +
                "BEGIN\n" +
                "    UPDATE Media\n" +
                "       SET count_frames = (\n" +
                "               SELECT count( * ) \n" +
                "                 FROM Photo\n" +
                "                WHERE Photo.media_id = NEW.media_id\n" +
                "           )\n" +
                "     WHERE Media.media_id = NEW.media_id;\n" +
                "END;";
        stmt.execute(sql);

        sql = "CREATE TRIGGER IF NOT EXISTS update_media_count_frames1\n" +
                "         AFTER DELETE\n" +
                "            ON Photo\n" +
                "BEGIN\n" +
                "    UPDATE Media\n" +
                "       SET count_frames = (\n" +
                "               SELECT count( * ) \n" +
                "                 FROM Photo\n" +
                "                WHERE Photo.media_id = OLD.media_id\n" +
                "           )\n" +
                "     WHERE Media.media_id = OLD.media_id;\n" +
                "END;";
        stmt.execute(sql);

        sql = "CREATE INDEX IF NOT EXISTS index_photo_id ON Photo (\n" +
                "    media_id ASC,\n" +
                "    photo_id ASC\n" +
                ");";
        stmt.execute(sql);



        sql = "CREATE TABLE IF NOT EXISTS Pixels (\n" +
                "    photo_id     TEXT,\n" +
                "    pixel_number INTEGER NOT NULL,\n" +
                "    color        INTEGER NOT NULL\n" +
                ");";

        stmt.execute(sql);

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
