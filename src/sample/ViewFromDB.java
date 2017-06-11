package sample;


import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewFromDB {

    private String name;
    private Integer frames;

    public ViewFromDB(){};
    public ViewFromDB(String name, Integer frames) {
        this.name = name;
        this.frames = frames;
    }
    public ViewFromDB(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFrames() {
        return frames;
    }

    public void setFrames(Integer frames) {
        this.frames = frames;
    }
}
