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
//        Statement stmtCreate = connection.createStatement();

//        stmt.execute("PRAGMA journal_mode = 'WAL';");

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

        sql = "CREATE TRIGGER IF NOT EXISTS trig_delete_info\n" +
                "        BEFORE DELETE\n" +
                "            ON Name\n" +
                "BEGIN\n" +
                "    DELETE FROM Info\n" +
                "          WHERE Info.name_id = old.name_id;\n" +
                "END;";
        stmt.execute(sql);


        sql = "CREATE TABLE IF NOT EXISTS First_Name (\n" +
                "    first_name_id INTEGER PRIMARY KEY\n" +
                "                          UNIQUE\n" +
                "                          NOT NULL,\n" +
                "    first_name    TEXT    UNIQUE\n" +
                "                          NOT NULL\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Last_Name (\n" +
                "    last_name_id INTEGER PRIMARY KEY\n" +
                "                         UNIQUE\n" +
                "                         NOT NULL,\n" +
                "    last_name    TEXT    UNIQUE\n" +
                "                         NOT NULL\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Surname (\n" +
                "    surname_id INTEGER PRIMARY KEY\n" +
                "                       UNIQUE\n" +
                "                       NOT NULL,\n" +
                "    surname    TEXT    UNIQUE\n" +
                "                       NOT NULL\n" +
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

        sql = "CREATE TRIGGER IF NOT EXISTS trig_in_info_del_from_media\n" +
                "        BEFORE DELETE\n" +
                "            ON Info\n" +
                "BEGIN\n" +
                "    DELETE FROM Media\n" +
                "          WHERE Media.info_id = old.info_id;\n" +
                "END;";
        stmt.execute(sql);




        sql = "CREATE TABLE IF NOT EXISTS Media_Type (\n" +
                "    media_type_id INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          UNIQUE\n" +
                "                          NOT NULL,\n" +
                "    media_type    TEXT    UNIQUE\n" +
                "                          NOT NULL\n" +
                ");";
        stmt.execute(sql);

        sql = "select count(*) from Media_Type;";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        if (rs.getInt(1) != 3) {
            sql = "insert into Media_Type (media_type) values ('photo');";
            stmt.execute(sql);
            sql = "insert into Media_Type (media_type) values ('video');";
            stmt.execute(sql);
            sql = "insert into Media_Type (media_type) values ('audio');";
            stmt.execute(sql);
        }

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

        sql = "CREATE TRIGGER IF NOT EXISTS trig_in_media_del_from_photo\n" +
                "        BEFORE DELETE\n" +
                "            ON Media\n" +
                "BEGIN\n" +
                "    DELETE FROM Photo\n" +
                "          WHERE Photo.media_id = old.media_id;\n" +
                "END;";
        stmt.execute(sql);




        sql = "CREATE TABLE IF NOT EXISTS Photo (\n" +
                "    media_id INTEGER REFERENCES Media (media_id) \n" +
                "                     NOT NULL,\n" +
                "    photo_id INT     NOT NULL,\n" +
                "    image    BLOB    NOT NULL\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TRIGGER IF NOT EXISTS set_media_count_frames\n" +
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

        sql = "CREATE TRIGGER IF NOT EXISTS update_media_count_frames\n" +
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

        sql = "CREATE TRIGGER IF NOT EXISTS trig_in_photo_del_from_pixels\n" +
                "        BEFORE DELETE\n" +
                "            ON Photo\n" +
                "BEGIN\n" +
                "    DELETE FROM Pixels\n" +
                "          WHERE Pixels.photo_id IN (\n" +
                "        SELECT photo_id\n" +
                "          FROM Photo\n" +
                "         WHERE media_id = old.media_id\n" +
                "    );\n" +
                "END;";
        stmt.execute(sql);

        sql = "CREATE INDEX IF NOT EXISTS index_photo_id ON Photo (\n" +
                "    media_id ASC,\n" +
                "    photo_id ASC\n" +
                ");";
        stmt.execute(sql);



        sql = "CREATE TABLE IF NOT EXISTS Pixels (\n" +
                "    media_id     INTEGER REFERENCES Media (media_id),\n" +
                "    photo_id     INTEGER REFERENCES Photo (photo_id) ON DELETE CASCADE\n" +
                "                         NOT NULL,\n" +
                "    pixel_number INTEGER NOT NULL,\n" +
                "    color        INTEGER NOT NULL\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE INDEX IF NOT EXISTS index_pixels_photo_id ON Pixels (\n" +
                "    photo_id ASC\n" +
                ");";
        stmt.execute(sql);

//        stmt.execute("PRAGMA journal_mode = 'DELETE';");

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
