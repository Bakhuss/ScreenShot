package SQLHandler;

import javax.swing.text.html.Option;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Stream;

public class DataBaseFile {
    static File fileDB;
    private static String urlDB = "jdbc:sqlite:SQLiteScreenShots.db";


    public static String getUrlDB() {
        return urlDB;
    }

    public static void setUrlDB(File fileDB) {
        DataBaseFile.urlDB = "jdbc:sqlite:" + fileDB.getAbsolutePath();
    }

    public static void createDBStructure(Statement stmt) throws SQLException {
        String sql = null;

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
                "    name_id       INTEGER REFERENCES Name (name_id) \n" +
                "                          NOT NULL,\n" +
                "    media_type_id INTEGER REFERENCES Media_Type (media_type_id) \n" +
                "                          NOT NULL,\n" +
                "    count_frames  INTEGER\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Media_Photo (\n" +
                "    id        INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                      UNIQUE\n" +
                "                      NOT NULL,\n" +
                "    media_id  INTEGER REFERENCES Media (media_id) \n" +
                "                      NOT NULL,\n" +
                "    photo_id  INTEGER NOT NULL,\n" +
                "    hasPixels BOOLEAN DEFAULT (0),\n" +
                "    hasPhoto  BOOLEAN DEFAULT (0),\n" +
                "    UNIQUE (\n" +
                "        media_id ASC,\n" +
                "        photo_id ASC\n" +
                "    )\n" +
                ");";
        stmt.execute(sql);


        sql = "CREATE TABLE IF NOT EXISTS Photo (\n" +
                "    media_photo_id INTEGER REFERENCES Media_Photo (id) \n" +
                "                           NOT NULL\n" +
                "                           UNIQUE,\n" +
                "    image          BLOB    NOT NULL\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TRIGGER IF NOT EXISTS set_in_Media_Photo_hasPhoto_true\n" +
                "         AFTER INSERT\n" +
                "            ON Photo\n" +
                "BEGIN\n" +
                "    UPDATE Media_Photo\n" +
                "       SET hasPhoto = 1\n" +
                "     WHERE Media_Photo.id = new.media_photo_id;\n" +
                "END;";
        stmt.execute(sql);

        sql = "CREATE TRIGGER IF NOT EXISTS set_in_Media_Photo_hasPhoto_false\n" +
                "         AFTER DELETE\n" +
                "            ON Photo\n" +
                "BEGIN\n" +
                "    UPDATE Media_Photo\n" +
                "       SET hasPhoto = 0\n" +
                "     WHERE Media_Photo.id = old.media_photo_id;\n" +
                "END;";
        stmt.execute(sql);


        sql = "CREATE TABLE IF NOT EXISTS Pixels (\n" +
                "    media_photo_id INTEGER REFERENCES Media_Photo (id) \n" +
                "                           UNIQUE\n" +
                "                           NOT NULL,\n" +
                "    pixel_number   INTEGER NOT NULL,\n" +
                "    color          INTEGER NOT NULL\n" +
                ");";
        stmt.execute(sql);

        sql = "CREATE TRIGGER IF NOT EXISTS set_in_Media_Photo_hasPixels_true\n" +
                "         AFTER INSERT\n" +
                "            ON Pixels\n" +
                "BEGIN\n" +
                "    UPDATE Media_Photo\n" +
                "       SET hasPixels = true\n" +
                "     WHERE Media_Photo.id = new.media_photo_id;\n" +
                "END;";
        stmt.execute(sql);

        sql = "CREATE TRIGGER IF NOT EXISTS set_in_Media_Photo_hasPixels_false\n" +
                "         AFTER DELETE\n" +
                "            ON Pixels\n" +
                "BEGIN\n" +
                "    UPDATE Media_Photo\n" +
                "       SET hasPixels = false\n" +
                "     WHERE Media_Photo.id = old.media_photo_id;\n" +
                "END;";
        stmt.execute(sql);

    }

}
