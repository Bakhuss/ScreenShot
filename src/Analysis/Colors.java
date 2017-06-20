package Analysis;


import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class Colors {

//                    int red = (rgb >> 16) & 0xff;
//                    int green = (rgb >> 8) & 0xff;
//                    int blue = (rgb) & 0xff;
//                    System.out.println(red + " " + green + " " + blue);

    //    private HashMap<Color, ArrayList<Integer>> countColors = null;

    private HashMap<ArrayList<Integer>, Color> countColors = null;
    TreeMap<ArrayList<Integer>, Color> tempp = null;

    private ArrayList<Color> pixelColors = null;
    private ArrayList<ArrayList<Integer>> pixelsPoint = null;

    private BufferedImage bi;


    public Colors() {
    }

    public Colors(BufferedImage bi) {
        this.bi = bi;
    }


    public void getCountColors() {

        System.out.println("Colors");

        setCountColorsMap(new HashMap<>());

        tempp = new TreeMap<>(new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                if (o1.size() == o2.size()) return -1;
                return (o2.size() - o1.size());
            }
        });

        long time2 = System.currentTimeMillis();

//        int Tread_Count = 2;
//        Thread[] trd = new Thread[Tread_Count];
//        HashMap<ArrayList<Integer>, Color>[] tempCountColorsMap = new HashMap[Tread_Count];
//        for (int i = 0; i < trd.length; i++) {
//            final int w = i;
//            trd[i] = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    int height = bi.getHeight()/Tread_Count;
//                    tempCountColorsMap[w] = new HashMap<>();
//                    int pixelNumber = w * height*bi.getWidth() + 1;
//                    for (int i = w * height + 1; i < (w + 1) * height; i++) {
//                        for (int j = 0; j < bi.getWidth(); j++) {
//                            Color rgbC = new Color(bi.getRGB(j, i));
//
//                            if (!tempCountColorsMap[w].containsValue(rgbC)) {
//                                tempCountColorsMap[w].put(new ArrayList<>(), rgbC);
//                            }
//                            getKeyByValue(rgbC).add(pixelNumber);
//                            pixelNumber++;
//
//                            if (getCountColorsMap().size() % 30000 == 0) {
//                                System.out.println("Size: " + getCountColorsMap().size());
//                            }
//
//                        }
//                    }
//                }
//            });
//            trd[i].start();
//        }
//




        int pixelNumber = 1;
        System.out.println("bi: " + bi.getWidth() + " " + bi.getHeight());
        for (int i = 0; i < bi.getHeight(); i++) {
            for (int j = 0; j < bi.getWidth(); j++) {
                Color rgbC = new Color(bi.getRGB(j, i));

                if (!getCountColorsMap().containsValue(rgbC)) {
                    getCountColorsMap().put(new ArrayList<>(), rgbC);
                }
                getKeyByValue(rgbC).add(pixelNumber);
                pixelNumber++;

                if (getCountColorsMap().size() % 30000 == 0) {
                    System.out.println("Size: " + getCountColorsMap().size());
                }

            }
        }

        tempp.putAll(getCountColorsMap());

        File file = new File("tempp.txt");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("tempp.txt"));
            oos.writeObject(getCountColorsMap());
        } catch (IOException e) {
            e.printStackTrace();
        }




        System.out.println("Время анализа количества цветов: " + (System.currentTimeMillis() - time2));
        System.out.println(pixelNumber - 1);

        System.out.println(getCountColorsMap().size());
        setCountColorsMap(null);


        new Color(-1315600);

    }


    public static Point getPoint(int pixelNumber, int imageWidth) {
        int x, y;
        y = Math.abs((pixelNumber - 1) / imageWidth);
        x = (pixelNumber - 1) - y * imageWidth;

        return new Point(x, y);
    }


    public ArrayList<Integer> getKeyByValue(Color color) {

        Set<Map.Entry<ArrayList<Integer>, Color>> entrySet = getCountColorsMap().entrySet();
        for (Map.Entry<ArrayList<Integer>, Color> pair : entrySet) {
            if (pair.getValue().equals(color)) return pair.getKey();
        }
        return new ArrayList<>();
    }


    public ArrayList<Integer> getNKeyByValue(Color color) {

        Set<Map.Entry<ArrayList<Integer>, Color>> entrySet = getTempp().entrySet();
        for (Map.Entry<ArrayList<Integer>, Color> pair : entrySet) {
            if (pair.getValue().equals(color)) return pair.getKey();
        }
        return new ArrayList<>();
    }


    public void setCountColorsMap(HashMap<ArrayList<Integer>, Color> countColors) {
        this.countColors = countColors;
    }

    public HashMap<ArrayList<Integer>, Color> getCountColorsMap() {
        return countColors;
    }

    public TreeMap<ArrayList<Integer>, Color> getTempp() {
        return tempp;
    }

    public void setTempp(TreeMap<ArrayList<Integer>, Color> tempp) {
        this.tempp = tempp;
    }

}
