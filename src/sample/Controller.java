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

import javax.imageio.ImageIO;
import javax.swing.*;
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


    public void initialize() {
        tablesName = new ArrayList<>();
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

    public void getScreenKey(KeyEvent keyEvent) {
        if (ScreenCapture.isScreening()) return;

        System.out.println("\nScreen");
        System.out.println(keyEvent.getCode());
        if (keyEvent.getCode() == KeyCode.PRINTSCREEN) {
            getScreenController();
        }

    }


    public void getView(ActionEvent actionEvent) {
        if (ScreenCapture.isScreening()) return;
        getMemoryInfo();

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
            System.out.println("Contains: " + title);
            for ( Frame o : ViewWindow.getFrames() ) {
                if ( o.getTitle().split(" ")[3].equals(title) ) {
                    o.setState(Frame.NORMAL);
                    o.show();
                }
            }
//            return;
        } else {

            final SQLHandler viewFrames = new SQLHandler();
            getMemoryInfo();
            System.out.println("1 " + viewFromDBStringCellEditEvent.getRowValue().getName());

            ArrayList<BufferedImage> bi = new ArrayList<>();
            System.out.println(bi.size());
            Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Title: " + title);
                    System.out.println(bi.size());
                    ViewWindow viewWindow = new ViewWindow(bi, title);
                    getTablesName().add(title);

                    for (int i = 0; i < getTablesName().size(); i++) {
                        System.out.println(getTablesName().get(i));
                    }

                }
            });

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Connect from method getViewFrames");
                        viewFrames.connect();
                        viewFrames.setPstmt(viewFrames.getConnection().prepareStatement("select img from '" + title + "';"));

                        ResultSet resultSet = viewFrames.getPstmt().executeQuery();

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
                        viewFrames.disconnect();
                        System.out.println("Disconnect from method getViewFrames");
                    }
                }
            });
            thread.start();

        }


//        System.out.println("Title: " + title);
//        System.out.println(bi.size());
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ViewWindow viewWindow = new ViewWindow(bi, title);
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();


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
}
