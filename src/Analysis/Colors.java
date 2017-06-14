package Analysis;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Colors {

//                    int red = (rgb >> 16) & 0xff;
//                    int green = (rgb >> 8) & 0xff;
//                    int blue = (rgb) & 0xff;
//                    System.out.println(red + " " + green + " " + blue);

    private HashMap<String, Integer> countColors;
    private ArrayList<Integer> arrInt = null;
    private ArrayList<BufferedImage> bi = null;


    public Colors(){}

    public Colors(ArrayList<BufferedImage> bi) {
        this.bi = bi;
    }

    public void getCountColors() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Colors");
                countColors = new HashMap<>();
                long time2 = System.currentTimeMillis();

//                for (BufferedImage o : bi) {


                BufferedImage o = bi.get(0);
                    for (int i = 0; i < o.getHeight(); i++) {
                        for (int j = 0; j < o.getWidth(); j++) {
                            int rgb = o.getRGB(j, i);
                            String str = "rgb: " + rgb;
                            if (countColors.containsKey(str)) {
                                int actualCount = countColors.get(str);
                                actualCount = actualCount + 1;
                                countColors.put(str, actualCount);
                            } else {
                                countColors.put(str, 1);
                            }
                        }
                    }
//                }

                System.out.println("Время анализа количества цветов: " + (System.currentTimeMillis() - time2));
                getMaxCountColor();

            }
        }).start();

    }

    public void getMaxCountColor() {
        long t3 = System.currentTimeMillis();
        int max = 0;
        int r = 0;
        int r1 = 0;
        arrInt = new ArrayList<>(countColors.values());
        for (int o : arrInt) {
            if (max < o) {
                max = o;
                r1 = r;
//                System.out.println("r1: " + r1);
            }
//            if ( (r != 0) && (r % 30 == 0) ) System.out.println();
//            System.out.print(max + " | " + o + " ");
            r++;
        }
        System.out.println("Время поиска макс. цвета: " + (System.currentTimeMillis() - t3));

        String st = (String) countColors.keySet().toArray()[r1];
        System.out.println("Цвет: " + st);
        System.out.println("Количество: " + max);
    }

}
