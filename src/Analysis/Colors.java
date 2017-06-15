package Analysis;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.*;
import java.util.List;

public class Colors {

//                    int red = (rgb >> 16) & 0xff;
//                    int green = (rgb >> 8) & 0xff;
//                    int blue = (rgb) & 0xff;
//                    System.out.println(red + " " + green + " " + blue);

    private HashMap<Color, ArrayList<Integer>> countColors = new HashMap<>();

    private BufferedImage bi;


    public Colors() {
    }

    public Colors(BufferedImage bi) {
        this.bi = bi;
    }

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

//                System.out.println("WhiteSize: " + countColors.get(Color.WHITE).size() );
                System.out.println(countColors.size());

                new Color(-1245204);

//                getMinCountColor();
                return countColors;
//            }
//        }).start();

    }

    public void getMinCountColor(HashMap<Color, ArrayList<Integer>> countColors) {
        System.out.println("getMinCountColor");
        long t3 = System.currentTimeMillis();
        int min = 0;
        int r = 0;
        int r1 = 0;

        Object[] set = countColors.keySet().toArray();
        min = countColors.get((Color) set[0]).size();
        Color clr = (Color) set[0];
        for (int i = 0; i < countColors.size(); i++) {
            if (min > countColors.get((Color) set[i]).size()) {
                min = countColors.get((Color) set[i]).size();
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


    public Point getPoint(int pixelNumber, BufferedImage image) {
        int x, y;
        y = Math.abs((pixelNumber - 1) / image.getWidth());
        x = (pixelNumber - 1) - y * image.getWidth();

        return new Point(x, y);
    }


}
