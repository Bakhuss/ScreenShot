package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {

    public Button Screen;
    public TextField tfTimeScreen;
    public Label lbTimeErr;
    public BorderPane mainFrame;
    public Label lbTimes;
    public Pane paneSetSize;
    public ProgressIndicator progIndicScreen;
    public Label lbFrames;
    public Label lbWidth;
    public Label lbHeight;
    public RadioButton rbFullSize;
    public RadioButton rbCustomSize;
    public TextField tfXSize;
    public TextField tfYSize;
    public TextField tfWidthSize;
    public TextField tfHeightSize;
    public Button btViewList;
    public Pane paneAboutProgram;
    public Label lbAboutProgram;

    static ObservableList<ViewFromDB> ScreenShots = FXCollections.observableArrayList();
    public TableView<ViewFromDB> tableViewFromDB;
    public TableColumn<ViewFromDB, String> nameColumn;
    public TableColumn<ViewFromDB, Integer> framesColumn;
    public SQLHandler getAllTables;

    static ArrayList<String> tablesName = null;


    Image image = null;
    InputStream is = null;
    BufferedOutputStream bos = null;
    Thread[] threads = new Thread[1];
    static double timeProgress = 0;

    int name_id = 0;
    int info_id = 0;
    int media_id = 0;


    public void initialize() {
        tablesName = new ArrayList<>();
        String aboutProgram = "Скриншотер для съёмки серии снимков в течение выбранного времени (от 1 секунды).\n" +
                              "Выбор размера снимков (full или custom).\n" +
                              "Автоматическое сохранение серии фотографий в БД SQLite (одна серия - одна таблица в БД).\n" +
                              "Запрос серий из БД по кнопке View list.\n" +
                              "Просмотр снимков по двойному клику по названию.\n" +
                              "Пролистывание снимков колёсиком мыши либо стрелками влево-вправо на клавиатуре.\n" +
                              "Удаление по клику правой кнопкой в открытом отдельном окне серии.";
        lbAboutProgram.setText(aboutProgram);
        if (!Main.getPrimaryStage().isIconified()) {
            getLbWidth().setText("width:  " + String.valueOf(ScreenCapture.getMaxWidth()));
            getLbHeight().setText("height: " + String.valueOf(ScreenCapture.getMaxHeight()));
        }
    }


    public void screening(ActionEvent actionEvent) throws Exception {
        if (ScreenCapture.isScreening()) return;

        System.out.println("\nScreen");
        System.out.println(ScreenCapture.getXSize() + " " + ScreenCapture.getYSize());
        System.out.println(ScreenCapture.getWidth() + " " + ScreenCapture.getHeight());

        getScreenController();
    }

    public void getScreenController() {

        getMemoryInfo();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    ScreenCapture.setTimeScreen(Integer.valueOf(tfTimeScreen.getText()));
                    ScreenCapture.setTimeProg(0);

                    setLbFramesText("");

                    threads[0] = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                progIndicScreen.setProgress(0);
                                setLbTimeErr(" ");
                                ScreenCapture.getScreen();
                                setLbFramesText(String.valueOf(ScreenCapture.getCountFrames()));
                                while (ScreenCapture.isSavingToSql()) {
                                    progIndicScreen.setProgress(0);
                                }
                                progIndicScreen.setProgress(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    for (Thread o : threads) {
                        o.setDaemon(true);
                        o.start();
                    }

//                    SwingUtilities.invokeLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            progIndicScreen.setProgress(0);
//                            long t = System.currentTimeMillis();
//                            do {
//
//                                System.out.println((double) (System.currentTimeMillis() - t) / (ScreenCapture.getTimeScreen() * 1000));
//                                progIndicScreen.setProgress((double) (System.currentTimeMillis() - t) / (ScreenCapture.getTimeScreen() * 1000));
//
//                            } while (System.currentTimeMillis() - t < ScreenCapture.getTimeScreen() * 1000);
//                            progIndicScreen.setProgress(1);
//                        }
//                    });

                } catch (NumberFormatException e) {
                    setLbTimeErr("?");
                    e.printStackTrace();
                }

                System.out.println(threads[0].getState());
            }

        }).start();
    }

//    public void getScreenKey(KeyEvent keyEvent) {
//        if (ScreenCapture.isScreening()) return;
//
//        System.out.println("\nScreen");
//        System.out.println(keyEvent.getCode());
//        if (keyEvent.getCode() == KeyCode.PRINTSCREEN) {
//            getScreenController();
//        }
//
//    }

    public void getScreenKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.PRINTSCREEN) {
            System.out.println("\nScreen");
            System.out.println(keyEvent.getCode());
            if (ScreenCapture.isScreening()) return;
            getScreenController();
        }

        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.V) {
            System.out.println("Ctrl + " + keyEvent.getCode());
            System.out.println("Vacuum start");
            SQLHandler sendVacuum = new SQLHandler();
            long time = System.currentTimeMillis();
            try {
                sendVacuum.connect();
                String sqlVac = "vacuum;";
                sendVacuum.getStmt().execute(sqlVac);
                System.out.println("Vacuum complete");

            } catch (SQLException e) {
                System.out.println("Vacuum abort");
                e.printStackTrace();
            }finally {
                sendVacuum.disconnect();
            }
            System.out.println("Затраченное время: " + (System.currentTimeMillis() - time));
        }

    }




    public void getView(ActionEvent actionEvent) {
        if (ScreenCapture.isScreening()) return;
        getMemoryInfo();
        System.out.println("Количество ядер: " + Runtime.getRuntime().availableProcessors());

//                   //Vacuum
//
//        SQLHandler sendVacuum = new SQLHandler();
//        try {
//            sendVacuum.connect();
//            String sqlVac = "vacuum;";
//            sendVacuum.getStmt().execute(sqlVac);
//            System.out.println("Vacuum complete");
//
//        } catch (SQLException e) {
//            System.out.println("Vacuum abort");
//            e.printStackTrace();
//        }finally {
//            sendVacuum.disconnect();
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getTables();
                nameColumn.setCellValueFactory(new PropertyValueFactory<ViewFromDB, String>("name"));
                framesColumn.setCellValueFactory(new PropertyValueFactory<ViewFromDB, Integer>("frames"));
                System.out.println(ScreenShots.size());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        btViewList.setText("View list (" + ScreenShots.size() + ")");
                        tableViewFromDB.setItems(ScreenShots);
                    }
                });
            }
        }).start();

        ScreenShots.clear();
    }

    public void getTables() {
        getAllTables = new SQLHandler();
        try {
            getAllTables.connect();
            getAllTables.getAllTables();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось запросить список таблиц из БД.");
        } finally {
            getAllTables.disconnect();
        }
    }


//    public void getCountColors(ActionEvent actionEvent) {
//        ScreenCapture.getCountColors();
//    }


    public void mainClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void cancelPaneSize(ActionEvent actionEvent) {
        paneSetSize.setVisible(false);
        mainFrame.setDisable(false);
        mainFrame.setOpacity(1);

        if (ScreenCapture.getWidth() == ScreenCapture.getMaxWidth() && ScreenCapture.getHeight() == ScreenCapture.getMaxHeight()) {
            System.out.println("5");
            rbFullSize.setSelected(true);
            rbCustomSize.setSelected(false);

            ScreenCapture.setXSize(0);
            ScreenCapture.setYSize(0);
        }
    }

    public void savePaneSize(ActionEvent actionEvent) {

        ScreenCapture.setXSize(Integer.parseInt(tfXSize.getText()));
        ScreenCapture.setYSize(Integer.parseInt(tfYSize.getText()));
        ScreenCapture.setWidth(Integer.parseInt(tfWidthSize.getText()));
        ScreenCapture.setHeight(Integer.parseInt(tfHeightSize.getText()));

        System.out.println(ScreenCapture.getWidth());
        System.out.println(ScreenCapture.getHeight());

        setLbWidth(ScreenCapture.getWidth());
        setLbHeight(ScreenCapture.getHeight());

        if (ScreenCapture.getWidth() == ScreenCapture.getMaxWidth() && ScreenCapture.getHeight() == ScreenCapture.getMaxHeight()) {
            System.out.println("5");
            rbFullSize.setSelected(true);
            rbCustomSize.setSelected(false);

            ScreenCapture.setXSize(0);
            ScreenCapture.setYSize(0);
        }


        paneSetSize.setVisible(false);
        mainFrame.setDisable(false);
        mainFrame.setOpacity(1);
    }

    public void setProgIndicScreenValue(double value) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void setLbFramesText(String lbFramesText) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbFrames.setText(lbFramesText);
            }
        });
    }

    public void setLbTimeErr(String lbTimeErrText) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbTimeErr.setText(lbTimeErrText);
            }
        });
    }

    public void setLbWidth(int width) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbWidth.setText("width:  " + width);
            }
        });
    }

    public void setLbHeight(int height) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbHeight.setText("height: " + height);
            }
        });
    }

    public void rbSizeActionFull(ActionEvent actionEvent) {
        if (rbFullSize.isSelected()) System.out.println("Full");
        System.out.println(1);
        rbFullSize.setSelected(true);
        rbCustomSize.setSelected(false);

        setLbWidth(ScreenCapture.getMaxWidth());
        setLbHeight(ScreenCapture.getMaxHeight());
        ScreenCapture.setXSize(0);
        ScreenCapture.setYSize(0);
        ScreenCapture.setWidth(ScreenCapture.getMaxWidth());
        ScreenCapture.setHeight(ScreenCapture.getMaxHeight());

    }

    public void rbSizeActionCustom(ActionEvent actionEvent) {
        if (rbCustomSize.isSelected()) System.out.println("Custom");
        System.out.println(2);
        rbCustomSize.setSelected(true);
        rbFullSize.setSelected(false);

        mainFrame.setDisable(true);
        mainFrame.setOpacity(0.7);
        paneSetSize.setVisible(true);

        tfXSize.setText(String.valueOf(ScreenCapture.getXSize()));
        tfYSize.setText(String.valueOf(ScreenCapture.getYSize()));
        tfWidthSize.setText(String.valueOf(ScreenCapture.getWidth()));
        tfHeightSize.setText(String.valueOf(ScreenCapture.getHeight()));
    }

    public ProgressIndicator getProgIndicScreen() {
        return progIndicScreen;
    }

    public void getScreenParent(ActionEvent actionEvent) {

        ScreenParent.getScreenParent();
        System.out.println(ScreenParent.getStartX() + " " + ScreenParent.getStartY());
        System.out.println(ScreenParent.getEndX() + " " + ScreenParent.getEndY());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Main.primaryStage.isIconified()) {
                }

                System.out.println("Iconfield");
                tfXSize.setText(String.valueOf(ScreenParent.getStartX()));
                tfYSize.setText(String.valueOf(ScreenParent.getStartY()));
                tfWidthSize.setText(String.valueOf(ScreenParent.getEndX() - ScreenParent.getStartX()));
                tfHeightSize.setText(String.valueOf(ScreenParent.getEndY() - ScreenParent.getStartY()));
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void getViewFrames(TableColumn.CellEditEvent<ViewFromDB, String> viewFromDBStringCellEditEvent) {
        System.out.println("getViewFrames");
        String title = viewFromDBStringCellEditEvent.getRowValue().getName();

        if ( getTablesName().contains(title) ) {
//            System.out.println("Contains: " + title);

            for ( Frame o : ViewWindow.getFrames() ) {
//                System.out.println("Good");
                if (o.getTitle().equals("")) continue;
                else {
                    if (o.getTitle().split(" ")[3].equals(title) && ((ViewWindow)o).getBi().size() != 0 ) {
//                        System.out.println("o.getTitle().split()[3]: " + o.getTitle().split(" ")[3]);
                        o.setState(Frame.NORMAL);
                        o.show();
//                        System.out.println(title + " is show");
                    }
                }

            }
        } else {

            final SQLHandler viewFramesFromSQL = new SQLHandler();
            getMemoryInfo();
//            System.out.println("1 " + viewFromDBStringCellEditEvent.getRowValue().getName());

            ArrayList<BufferedImage> bi = new ArrayList<>();
//            System.out.println(bi.size());
            Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Title: " + title);
//                    System.out.println(bi.size());
                    ViewWindow viewWindow = new ViewWindow(bi, title, getName_id(), getInfo_id(), getMedia_id());
                    getTablesName().add(title);

//                    for (int i = 0; i < getTablesName().size(); i++) {
//                        System.out.println(getTablesName().get(i));
//                    }

                }
            });

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ResultSet resultSet;

                        System.out.println("Connect from method getViewFrames");

                        String sql = "select Name.name_id, Info.info_id, Media.media_id from Name\n" +
                                "    inner join Info on Name.name_id = Info.name_id\n" +
                                "    inner join Media on Info.info_id = Media.info_id\n" +
                                "    where name = '" + title + "';";

                        viewFramesFromSQL.connect();

                        resultSet = viewFramesFromSQL.getStmt().executeQuery(sql);
                        resultSet.next();
                        setName_id(resultSet.getInt(1));
                        setInfo_id(resultSet.getInt(2));
                        setMedia_id(resultSet.getInt(3));
                        System.out.println("Name: " + getName_id() + " Info: " + getInfo_id() + " Media: " + getMedia_id());
                        resultSet.close();

                        String sql_photo = "select image from Photo where media_id = (" + getMedia_id() + ") order by photo_id asc;";

                        resultSet = viewFramesFromSQL.getStmt().executeQuery(sql_photo);

                        while (resultSet.next()) {
                            InputStream is = resultSet.getBinaryStream(1);
                            try {
                                bi.add(ImageIO.read(is));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (bi.size() == 1 && tr.getState() != Thread.State.RUNNABLE) {
                                try {
                                    tr.start();
                                } catch (IllegalThreadStateException ie) {
                                    ie.printStackTrace();
                                    break;
                                }
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        viewFramesFromSQL.disconnect();
                        System.out.println("Disconnect from method getViewFrames");
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


//            System.out.println(bi.size());
//            Thread tr = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("Title: " + title);
//                    System.out.println(bi.size());
//                    ViewWindow viewWindow = new ViewWindow(bi, title, getName_id(), getInfo_id(), getMedia_id());
//                    getTablesName().add(title);
//
//                    for (int i = 0; i < getTablesName().size(); i++) {
//                        System.out.println(getTablesName().get(i));
//                    }
//                }
//            });
//            tr.start();
        }
    }

    public void mouseClickedFromTable(MouseEvent mouseEvent) {
//        if (mouseEvent.getClickCount() == 2) System.out.println(mouseEvent.getEventType().getName());
    }

    public static void getMemoryInfo() {
        long total = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long maximum = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long free = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        System.out.println("Memory: total " + total + "Мб | " + "maximum " + maximum + "Мб | " + "free " + free + "Мб");
    }


    public Label getLbWidth() {
        return lbWidth;
    }

    public Label getLbHeight() {
        return lbHeight;
    }


    public static ArrayList<String> getTablesName() {
        return tablesName;
    }

    public static void setTablesName(ArrayList<String> tablesName) {
        Controller.tablesName = tablesName;
    }

    public void showPaneAboutProgram(ActionEvent actionEvent) {

        mainFrame.setDisable(true);
        mainFrame.setOpacity(0.7);
        paneAboutProgram.setVisible(true);



    }

    public void okPaneAboutProgram(ActionEvent actionEvent) {

        paneAboutProgram.setVisible(false);
        mainFrame.setDisable(false);
        mainFrame.setOpacity(1);

    }


    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public int getName_id() {
        return name_id;
    }

    public void setName_id(int name_id) {
        this.name_id = name_id;
    }

    public int getInfo_id() {
        return info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }



    public void getDBFile(ActionEvent actionEvent) {

        FileChooser getDBFile = new FileChooser();
        getDBFile.setTitle("Open DBFile");
        getDBFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite", "*.db"));

        File file = getDBFile.showOpenDialog(Main.primaryStage);
        if (file != null) {
            System.out.println(file.isDirectory());
            System.out.println(file.isFile());
        }

    }

    public void createDBFile(ActionEvent actionEvent) {

        FileChooser createDBFile = new FileChooser();
        createDBFile.setTitle("Create DB file");
        createDBFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite", "*.db"));

        File file = createDBFile.showSaveDialog(Main.primaryStage);

        System.out.println(file.isDirectory());
        System.out.println(file.isFile());

    }


}
