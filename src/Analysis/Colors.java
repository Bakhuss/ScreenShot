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

    //    private HashMap<Color, ArrayList<Integer>> countColors = null;
    private ArrayList<Integer> sizes = null;

    private HashMap<ArrayList<Integer>, Color> countColors = null;
    TreeMap<ArrayList<Integer>, Color> tempp = null;


    HashMap<Color, ArrayList<Integer>> tempArr = null;


    private ArrayList<Color> pixelColors = null;
    private ArrayList<ArrayList<Integer>> pixelsPoint = null;

    private BufferedImage bi;


    public Colors() {
    }

    public Colors(BufferedImage bi) {
        this.bi = bi;
    }

/*
    public void getCountColor() {
        System.out.println("Colors");
        setPixelColors(new ArrayList<>());
        setPixelsPoint(new ArrayList<>());


        int pixelNumber = 1;
        long t = System.currentTimeMillis();
        for (int i = 0; i < bi.getHeight(); i++) {
            for (int j = 0; j < bi.getWidth(); j++) {
                int index = 0;
                Color rgbC = new Color(bi.getRGB(j, i));

                index = getPixelColors().indexOf(rgbC);
                if (index == -1) {
                    getPixelColors().add(rgbC);
                    getPixelsPoint().add(new ArrayList<>());
                    index = getPixelColors().size() - 1;
                }

//                for (int l = 0; l < getPixelColors().size(); l++) {
//                    if (getPixelColors().get(l).equals(rgbC)) {
//                        break;
//                    }
//                    index++;
//                }
//
//                if (index == getPixelColors().size()) {
//                    getPixelColors().add(rgbC);
//                    getPixelsPoint().add(new ArrayList<>());
//                    index = getPixelColors().size() - 1;
//                }


//                System.out.println("Index: " + index);
//                if (!getPixelColors().contains(rgbC)) {
//                    getPixelColors().add(rgbC);
//                    getPixelsPoint().add(new ArrayList<>());
//                    index = getPixelColors().size() - 1;
//                }


                getPixelsPoint().get(index).add(pixelNumber);
                pixelNumber++;

                if (getPixelsPoint().size() == 1) continue;
                sorts(index);
            }
        }

//        countColorss = new TreeMap<>(new Comparator<ArrayList<Integer>>() {
//            @Override
//            public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
//                return ( o2.size() - o1.size() );
//            }
//        });

        System.out.println("TimeArrayLists: " + (System.currentTimeMillis() - t));
        System.out.println("Итог Array");
        System.out.println("ArrayColors: " + getPixelColors().size());
        System.out.println("ArrayPixels: " + getPixelsPoint().size());
        System.out.println(getPixelColors().get(2).getRGB() + "\n" + getPixelsPoint().get(2));
//        System.out.println(getCountColorsMap().get(getPixelColors().get(2)));
    }

    public void sorts(int index) {
        if (index == 0) return;
        long t = System.currentTimeMillis();

        if (getPixelsPoint().get(index).size() > getPixelsPoint().get(index-1).size()) {
            ArrayList<Integer> tempArray = getPixelsPoint().get(index);
            Color tepmColor = getPixelColors().get(index);

            getPixelsPoint().remove(index);
            getPixelColors().remove(index);

            getPixelsPoint().add(index-1, tempArray);
            getPixelColors().add(index-1, tepmColor);
        }

//        System.out.print(" Время сортировки: " + (System.currentTimeMillis()-t) );
    }
*/

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

        int pixelNumber = 1;
        System.out.println("bi: " + bi.getWidth() + " " + bi.getHeight());
        for (int i = 0; i < bi.getHeight(); i++) {
            for (int j = 0; j < bi.getWidth(); j++) {
                Color rgbC = new Color(bi.getRGB(j, i));

                if (!getCountColorsMap().containsValue(rgbC)) {
                    getCountColorsMap().put(new ArrayList<>(), rgbC);
                }
                getKeyByValue(rgbC).add(pixelNumber);
//                System.out.println("SizeKeyByValue: " + getKeyByValue(rgbC).size());
                pixelNumber++;

                if (getCountColorsMap().size() % 50000 == 0) {
                    System.out.println("Size: " + getCountColorsMap().size());
                }
//                System.out.println("Size: " + getCountColorsMap().size() );

            }
        }

        tempp.putAll(getCountColorsMap());


        System.out.println("Время анализа количества цветов: " + (System.currentTimeMillis() - time2));
        System.out.println(pixelNumber - 1);

        System.out.println(getCountColorsMap().size());

        new Color(-14872569);

    }




/*
    public void getCountColors() {

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

        System.out.println("Время анализа количества цветов: " + (System.currentTimeMillis() - time2));
        System.out.println(pixelNumber - 1);

        System.out.println(countColors.size());

        sort();


        new Color(-69120);

    }
*/


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


    public ArrayList<Integer> getKeyByValue(Color color) {

        Set<Map.Entry<ArrayList<Integer>, Color>> entrySet = getCountColorsMap().entrySet();
//        Set<Map.Entry<ArrayList<Integer>, Color>> entrySet = getCountColorsMap().entrySet();

//        Set<Map.Entry<Color, ArrayList<Integer>>> entrySet = getCountColorsMap().entrySet();

        for (Map.Entry<ArrayList<Integer>, Color> pair : entrySet) {
            if (pair.getValue().equals(color)) return pair.getKey();
        }

//        System.out.println("colorsSizeFromKeyByValue: " + colors.size());
        return new ArrayList<>();
    }


    public ArrayList<Integer> getNKeyByValue(Color color) {

        Set<Map.Entry<ArrayList<Integer>, Color>> entrySet = getTempp().entrySet();
        for (Map.Entry<ArrayList<Integer>, Color> pair : entrySet) {
            if (pair.getValue().equals(color)) return pair.getKey();
        }
//        System.out.println("colorsSizeFromKeyByValue: " + colors.size());
        return new ArrayList<>();
    }

    public HashMap<Color, ArrayList<Integer>> getTempArr() {
        return tempArr;
    }



/*
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

                for (int i = 0; i < getSizes().size(); i++) {
                    if (i % 10000 == 0) System.out.println(tempArr.size() + " ");
                    ArrayList<Color> tempC = getNKeyByValue(i);
                    for (Color p : tempC) {
                        getPixelColors().add(p);
                        getPixelsPoint().add(tempArr.get(p));
                        tempArr.remove(p);
//                System.out.print( tempArr.size() + " " );
                    }
                }

                System.out.println("\nВремя переноса: " + (System.currentTimeMillis() - t));
                System.out.println("tempArrSizeResume: " + tempArr.size());
                System.out.println(getPixelColors().size() + " " + getPixelsPoint().size());
                System.out.println(getPixelColors().get(0).getRGB() + " " + getPixelsPoint().get(0).size());
                System.out.println(getPixelColors().get(getPixelColors().size() - 1).getRGB() + " " + getPixelsPoint().get(getPixelsPoint().size() - 1).size());


            }
        });
        thread.setDaemon(true);
        thread.start();


    }
*/

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

    public ArrayList<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Integer> sizes) {
        this.sizes = sizes;
    }


    public ArrayList<Color> getPixelColors() {
        return pixelColors;
    }

    private void setPixelColors(ArrayList<Color> pixelColors) {
        this.pixelColors = pixelColors;
    }

    public synchronized ArrayList<ArrayList<Integer>> getPixelsPoint() {
        return pixelsPoint;
    }

    private void setPixelsPoint(ArrayList<ArrayList<Integer>> pixelsPoint) {
        this.pixelsPoint = pixelsPoint;
    }

}
