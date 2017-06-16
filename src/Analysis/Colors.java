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

    private BufferedImage bi;


    public Colors() {
    }

    public Colors(BufferedImage bi) {
        this.bi = bi;
    }


    public void getCountColors() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

        System.out.println("Colors");
        setCountColorsMap(new HashMap<>());

        long time2 = System.currentTimeMillis();

//                for (BufferedImage o : bi) {
        int pixelNumber = 1;
//                BufferedImage o = bi;
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

        new Color(255,255,225);

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


    public void getMinCountColor() {
        System.out.println("getMinCountColor");
        long t3 = System.currentTimeMillis();
        int min = 0;
        int r = 0;
        int r1 = 0;

        Object[] set = getCountColorsMap().keySet().toArray();
        min = getCountColorsMap().get((Color) set[0]).size();
        Color clr = (Color) set[0];
        for (int i = 0; i < getCountColorsMap().size(); i++) {
            if (min > getCountColorsMap().get((Color) set[i]).size()) {
                min = getCountColorsMap().get((Color) set[i]).size();
                r1 = r;
                clr = (Color) set[i];
            }
            r++;
        }

        System.out.println("Время поиска макс. цвета: " + (System.currentTimeMillis() - t3));

        System.out.println("Цвет: " + clr.getRGB());
        System.out.println("Количество: " + min);
//        System.out.println(countColors.get(clr));
//        for ( Object o : countColors.get(clr).toArray() ) {
//            System.out.print(" " + getPoint( (int) o, bi.get(0)) );
//        }


    }


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
            if ( sizes.get(i).compareTo(pair.getValue().size()) == 0) {
                colors.add(pair.getKey());
//                System.out.println("ColorSize: " + pair.getKey().getRGB() + " " + pair.getValue().size());
            }
        }
//        System.out.println("colorsSizeFromKeyByValue: " + colors.size());
        return colors;
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
}
