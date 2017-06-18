package Analysis;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Colors {

//                    int red = (rgb >> 16) & 0xff;
//                    int green = (rgb >> 8) & 0xff;
//                    int blue = (rgb) & 0xff;
//                    System.out.println(red + " " + green + " " + blue);

    private HashMap<Color, ArrayList<Integer>> countColors = null;
    private ArrayList<Integer> sizes = null;



    HashMap<Color, ArrayList<Integer>> tempArr = null;



    private ArrayList<Color> pixelColors = null;
    private ArrayList<ArrayList<Integer>> pixelsPoint = null;

    private BufferedImage bi;


    public Colors() {
    }

    public Colors(BufferedImage bi) {
        this.bi = bi;
    }


    public void getCountColor() {
        System.out.println("Colors");
        setPixelColors(new ArrayList<>());
        setPixelsPoint(new ArrayList<>());


        int pixelNumber = 1;
        long t = System.currentTimeMillis();
        for (int i = 0; i < bi.getHeight(); i++) {
            for (int j = 0; j < bi.getWidth(); j++) {
                int index = 0;
                Color rgbC = new Color(bi.getRGB(j,i));
                int a = 0;
                for ( int l = 0; l < getPixelColors().size(); l++ ) {
                    if (getPixelColors().get(l).equals(rgbC)) {
                        break;
                    }
                    index++;
                }
//                System.out.println("Index: " + index);
                if (!getPixelColors().contains(rgbC)) {
                    getPixelColors().add(rgbC);
                    getPixelsPoint().add(new ArrayList<>());
                    index = getPixelColors().size() - 1;
                }
                    getPixelsPoint().get(index).add(pixelNumber);
                pixelNumber++;
            }
        }

        System.out.println("TimeArrayLists: " + (System.currentTimeMillis() - t));
        System.out.println("Итог Array");
        System.out.println("ArrayColors: " + getPixelColors().size());
        System.out.println("ArrayPixels: " + getPixelsPoint().size());
        System.out.println(getPixelColors().get(2).getRGB() + "\n" + getPixelsPoint().get(2));
        System.out.println( getCountColorsMap().get(getPixelColors().get(2)) );
    }

    public void getCountColors() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

        System.out.println("Colors");
        setCountColorsMap(new HashMap<>());

        long time2 = System.currentTimeMillis();

        int pixelNumber = 1;
        System.out.println("bi: " + bi.getWidth() + " " + bi.getHeight());
        for (int i = 0; i < bi.getHeight(); i++) {
            for (int j = 0; j < bi.getWidth(); j++) {
                Color rgbC = new Color(bi.getRGB(j, i));

                if (!getCountColorsMap().containsKey(rgbC)) getCountColorsMap().put(rgbC, new ArrayList<>());
                getCountColorsMap().get(rgbC).add(pixelNumber);

                pixelNumber++;
            }
        }
//                }

        System.out.println("Время анализа количества цветов: " + (System.currentTimeMillis() - time2));
        System.out.println(pixelNumber - 1);

        System.out.println(countColors.size());

        sort();

//        fillArraysColorsAndPoints();

        new Color(-69120);

//                getMinCountColor();

//            }
//        }).start();

    }



/*
    public HashMap<Color, ArrayList<Integer>> getCountColors() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

                System.out.println("Colors");
                countColors = new HashMap<>();

                long time2 = System.currentTimeMillis();

//                for (BufferedImage o : bi) {
                int pixelNumber = 1;
//                BufferedImage o = bi;
                System.out.println("bi: " + bi.getWidth() + " " + bi.getHeight());
                for (int i = 0; i < bi.getHeight(); i++) {
                    for (int j = 0; j < bi.getWidth(); j++) {
                        Color rgbC = new Color(bi.getRGB(j, i));

                        if (!countColors.containsKey(rgbC)) countColors.put(rgbC, new ArrayList<>());
                        countColors.get(rgbC).add(pixelNumber);

                        pixelNumber++;
                    }
                }
//                }

                System.out.println("Время анализа количества цветов: " + (System.currentTimeMillis() - time2));
                System.out.println(pixelNumber - 1);

                System.out.println(countColors.size());

                new Color(-1245204);

//                getMinCountColor();
                return countColors;
//            }
//        }).start();

    }
*/


    public static Point getPoint(int pixelNumber, int imageWidth) {
        int x, y;
        y = Math.abs((pixelNumber - 1) / imageWidth);
        x = (pixelNumber - 1) - y * imageWidth;

        return new Point(x, y);
    }

    public void sort() {
        sizes = new ArrayList<>(getCountColorsMap().size());
        Object[] objects = getCountColorsMap().values().toArray();

        for (Object o : objects) {
            sizes.add(((ArrayList<Integer>) o).size());
        }
        objects = null;
        System.out.println("sizesSize: " + sizes.size());

        sizes.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

        System.out.println("Size: " + sizes.get(0) + " " + sizes.get(sizes.size() - 1));
    }

    public ArrayList<Color> getKeyByValue(int i) {
        ArrayList<Color> colors = new ArrayList<>(1);
        Set<Map.Entry<Color, ArrayList<Integer>>> entrySet = getCountColorsMap().entrySet();
        for (Map.Entry<Color, ArrayList<Integer>> pair : entrySet) {
            if (sizes.get(i).compareTo(pair.getValue().size()) == 0) {
                colors.add(pair.getKey());
//                System.out.println("ColorSize: " + pair.getKey().getRGB() + " " + pair.getValue().size());
            }
        }
//        System.out.println("colorsSizeFromKeyByValue: " + colors.size());
        return colors;
    }


    public ArrayList<Color> getNKeyByValue(int i) {
        ArrayList<Color> colors = new ArrayList<>(1);
        Set<Map.Entry<Color, ArrayList<Integer>>> entrySet = getTempArr().entrySet();
        for (Map.Entry<Color, ArrayList<Integer>> pair : entrySet) {
            if (sizes.get(i).compareTo(pair.getValue().size()) == 0) {
                colors.add(pair.getKey());
//                System.out.println("ColorSize: " + pair.getKey().getRGB() + " " + pair.getValue().size());
            }
        }
//        System.out.println("colorsSizeFromKeyByValue: " + colors.size());
        return colors;
    }

    public HashMap<Color, ArrayList<Integer>> getTempArr() {
        return tempArr;
    }

    public void fillArraysColorsAndPoints() {
        System.out.println("fillArraysColorsAndPoints");
        setPixelColors(new ArrayList<>());
        setPixelsPoint(new ArrayList<>());
        tempArr = new HashMap<>();
        tempArr.putAll(getCountColorsMap());
        System.out.println("tempArrSize: " + tempArr.size());

        long t = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for ( int i = 0; i < getSizes().size(); i++ ) {
                    if (i % 10000 == 0) System.out.println( tempArr.size() + " " );
                    ArrayList<Color> tempC =  getNKeyByValue(i);
                    for ( Color p : tempC ) {
                        getPixelColors().add(p);
                        getPixelsPoint().add(tempArr.get(p));
                        tempArr.remove(p);
//                System.out.print( tempArr.size() + " " );
                    }
                }

                System.out.println("\nВремя переноса: " + (System.currentTimeMillis() - t) );
                System.out.println("tempArrSizeResume: " + tempArr.size());
                System.out.println( getPixelColors().size() + " " + getPixelsPoint().size() );
                System.out.println(getPixelColors().get(0).getRGB() + " " + getPixelsPoint().get(0).size());
                System.out.println(getPixelColors().get(getPixelColors().size() - 1).getRGB() + " " + getPixelsPoint().get(getPixelsPoint().size() - 1).size());


            }
        });
        thread.setDaemon(true);
        thread.start();


    }


    public void setCountColorsMap(HashMap<Color, ArrayList<Integer>> countColors) {
        this.countColors = countColors;
    }

    public HashMap<Color, ArrayList<Integer>> getCountColorsMap() {
        return countColors;
    }

    public ArrayList<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Integer> sizes) {
        this.sizes = sizes;
    }


    public ArrayList<Color> getPixelColors() {
        return pixelColors;
    }

    public void setPixelColors(ArrayList<Color> pixelColors) {
        this.pixelColors = pixelColors;
    }

    public synchronized ArrayList<ArrayList<Integer>> getPixelsPoint() {
        return pixelsPoint;
    }

    public void setPixelsPoint(ArrayList<ArrayList<Integer>> pixelsPoint) {
        this.pixelsPoint = pixelsPoint;
    }
}
