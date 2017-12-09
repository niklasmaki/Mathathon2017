package mathathon2017;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import mathathon2017.util.Coordinate;
import mathathon2017.util.ImageBase;
import mathathon2017.util.Triangle;

public class Triangler {

    private static int lkm = 30;
    private static int k = 10;
    private static int[] maxarvot;
    private static BufferedImage image;
    private static boolean opacity = false;
    private static int opacitytime = 2400;
    private static double c = 0.05;
    private static double b = 4;

    private static ImageBase luvutToKuva(int[] luvut) {
        ArrayList<Triangle> kolmiot = new ArrayList<>();
        for (int kolmio = 0; kolmio < luvut.length / k; kolmio++) {
            Triangle t = new Triangle();
            t.a = new Coordinate(luvut[kolmio * k + 0], luvut[kolmio * k + 1]);
            t.b = new Coordinate(luvut[kolmio * k + 2], luvut[kolmio * k + 3]);
            t.c = new Coordinate(luvut[kolmio * k + 4], luvut[kolmio * k + 5]);
            t.red = luvut[kolmio * k + 6];
            t.green = luvut[kolmio * k + 7];
            t.blue = luvut[kolmio * k + 8];
            t.opacity = luvut[kolmio * k + 9];
            kolmiot.add(t);
        }
        ImageBase ib = new ImageBase();
        ib.setTriangles(kolmiot);
        return ib;
    }

    private static void muuta(int[] luvut, int ind, int index) {
        if (!opacity && (ind % k == 9)) {
            return;
        }
        int max = maxarvot[ind % k];

        int d = (int) (1 + c * max * Math.exp(-Math.log(2) / b * (index / luvut.length)));

        int luku = luvut[ind];
        long etaisyys = ImageUtils.compare(luvutToKuva(luvut), image);

        long etP = etaisyys;
        int aP = luku + d;
        for (; aP < maxarvot[ind % k]; aP += d) {
            luvut[ind] = aP;
            long uusiet = ImageUtils.compare(luvutToKuva(luvut), image);
            if (uusiet > etP) {
                break;
            }
            etP = uusiet;
        }
        aP -= d;

        long etN = etaisyys;
        int aN = luku - d;
        for (; aN >= 0; aN -= d) {
            luvut[ind] = aN;
            long uusiet = ImageUtils.compare(luvutToKuva(luvut), image);
            if (uusiet > etN) {
                break;
            }
            etN = uusiet;
        }
        aN += d;

        if (etP < etN) {
            luvut[ind] = aP;
        } else {
            luvut[ind] = aN;
        }
    }

    public static void main(String[] args) {
        String logo = "talvi.png";
        image = ImageUtils.getImage(logo);
        int width = image.getWidth();
        int height = image.getHeight();
        maxarvot = new int[]{width, height, width, height, width, height, 255, 255, 255, 255};
        int[] luvut = new int[lkm * k];
        for (int i = 0; i < lkm; i++) {
            int x = rnd(maxarvot[0]);
            int y = rnd(maxarvot[1]);
            Color color = new Color(image.getRGB(x, y));
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
//            int r = rnd(maxarvot[6]);
//            int g = rnd(maxarvot[7]);
//            int b = rnd(maxarvot[8]);
            luvut[i * k + 0] = x;
            luvut[i * k + 1] = y;
            luvut[i * k + 2] = x;
            luvut[i * k + 3] = y;
            luvut[i * k + 4] = x;
            luvut[i * k + 5] = y;
            luvut[i * k + 6] = r;
            luvut[i * k + 7] = g;
            luvut[i * k + 8] = b;
            luvut[i * k + 9] = 255;
        }

        ImageBase base = luvutToKuva(luvut);
        long distance = ImageUtils.compare(base, image);
        System.out.println("0: distance: " + distance);
        System.out.println(base);
        int index = 0;
//        double vanha = 0;

        while (true) {
            int lukuind = index++ % luvut.length;

            if (index == opacitytime) {
                opacity = true;
            }
            muuta(luvut, lukuind, index);

            base = luvutToKuva(luvut);
            long newDistance = ImageUtils.compare(base, image);
            distance = newDistance;

//            if (!opacity && (index % 300 == 0)) {
//                if (vanha==distance) {
//                    opacity = true;
//                }
//                vanha = distance;
//            }
            if (index % 100 == 0) {
                System.out.println(index + ": distance: " + distance);
                System.out.println(base);
            }
            if (index % 1000 == 0) {
                ImageUtils.saveImage(base, "pics/best" + index, image.getWidth(), image.getHeight());
                if (index % 10000 == 0) {
                    ImageUtils.submitPicture(base);
                }
            }
        }
    }

    private static int rnd(int max) {
        return (int) (Math.random() * max);
    }

}
