package mathathon2017;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import mathathon2017.util.Coordinate;
import mathathon2017.util.ImageBase;
import mathathon2017.util.Triangle;

public class Triangler {
    public static void main(String[] args) {
        String logo = "solinor_avatar.png";
        BufferedImage image = ImageUtils.getImage(logo);
        EdgeDetector edgeDetector = new EdgeDetector();
        List<Coordinate> edgeCoordinates = edgeDetector.getEdgeCoordinates(image);
        
        ImageBase base = new ImageBase();
        for(int i = 0; i<30; i++) {
            base.getTriangles().add(createRandomEdgePointTriangle(edgeCoordinates));
        }
        
        long distance = ImageUtils.compare(base, image);
        System.out.println("0: distance: " + distance);
        System.out.println(base);
        int index = 0;
        //Rajoitetaan 100 iteraatioon ja tutkitaan suppenemista eri arvoilla
        int iteraatioita = 0;
        long uusDistance = distance;
        long alkuDistance = distance;
        while(iteraatioita < 101) {
            Triangle original = base.getTriangles().remove(0);
            Triangle mutated = mutate(original, image);
            base.getTriangles().add(mutated);
            long newDistance = ImageUtils.compare(base, image);
            if(newDistance < distance) {
                distance = newDistance;
                uusDistance = newDistance;
                System.out.println(index + ": distance: " + distance);
                System.out.println(base);
                index++;
                if(index%10==0) {
                    ImageUtils.saveImage(base, "pics/best" + index, image.getWidth(), image.getHeight());
//                    ImageUtils.submitPicture(base); 
                }
                iteraatioita++;
            }
            else {
                base.getTriangles().remove(base.getTriangles().size()-1);
                base.getTriangles().add(original);
                iteraatioita++;
            }
            
        }
        //Tänne tulostus suppenemisesta
        System.out.println("Distance parani: " + (alkuDistance - uusDistance));
    }
    
    private static Triangle createRandomEdgePointTriangle(List<Coordinate> edgeCoords) {
        Triangle t = new Triangle();
        t.a = edgeCoords.get(rnd(edgeCoords.size()));
        t.b = edgeCoords.get(rnd(edgeCoords.size()));
        t.c = edgeCoords.get(rnd(edgeCoords.size()));
        t.setColor(5, 172, 240);
        t.opacity = 255;
        return t;
    }
    
    private static Triangle createRandomTriangle(int width, int height) {
        Triangle t = new Triangle();
        t.a = new Coordinate(rnd(height), rnd(width));
        t.b = new Coordinate(rnd(height), rnd(width));
        t.c = new Coordinate(rnd(height), rnd(width));
        t.setColor(5, 172, 240);
        t.opacity = 255;
        return t;
    }
    
    private static void setColorOfTriangle(int ax, int ay, int bx, int by, int cx, int cy, BufferedImage image, Triangle t) {
        double lambda = 1/10;
        int kax = (ax + bx + cx)/3;
        int kay = (ay + by + cy)/3;
        int aax = (int) Math.round(lambda*(bx-ax) + lambda*(cx-ax) + ax);
        int aay = (int) Math.round(lambda*(by-ay) + lambda*(cy-ay) + ay);
        int bax = (int) Math.round(lambda*(ax-bx) + lambda*(cx-bx) + bx);
        int bay = (int) Math.round(lambda*(ay-by) + lambda*(cy-by) + by);
        int cax = (int) Math.round(lambda*(ax-cx) + lambda*(ax-cx) + cx);
        int cay = (int) Math.round(lambda*(ay-cy) + lambda*(ay-cy) + cy);
        Color yksi = new Color(image.getRGB(kax, kay));
        Color kaksi = new Color(image.getRGB(aax, aay));
        Color kolme = new Color(image.getRGB(bax, bay));
        Color nelja = new Color(image.getRGB(cax, cay));
        int red = (yksi.getRed() + kaksi.getRed() + kolme.getRed() + nelja.getRed())/4;
        int green = (yksi.getGreen() + kaksi.getGreen() + kolme.getGreen() + nelja.getGreen())/4;
        int blue = (yksi.getBlue() + kaksi.getBlue() + kolme.getBlue() + nelja.getBlue())/4;
        t.setColor(red, green, blue);
    }
    
    private static Triangle mutate(Triangle triangle, BufferedImage image) {
        Triangle t = triangle.copy();
        if(flipCoin()) {
            switch(rnd(6)){
            case 0:
                t.a.x = changeCoordinates(t.a.x,image.getHeight());
                break;
            case 1:
                t.a.y = changeCoordinates(t.a.y,image.getWidth());
                break;
            case 2:
                t.b.x = changeCoordinates(t.b.x,image.getHeight());
                break;
            case 3:
                t.b.y = changeCoordinates(t.b.y,image.getWidth());
                break;
            case 4:
                t.c.x = changeCoordinates(t.c.x,image.getHeight());
                break;
            case 5:
                t.c.y = changeCoordinates(t.c.y,image.getWidth());
            default:
                break;
            }
        }
        else {
            switch(rnd(4)){
            case 0:
                t.red = changeColor(t.red);
                break;
            case 1:
                t.green = changeColor(t.green);
                break;
            case 2:
                t.blue = changeColor(t.blue);
                break;
            case 3:
                t.opacity = 10+rnd(245);
                break;
            }
        }
        return t;
    }
    
    private static int changeCoordinates(int position,int maxSize) {
        position = position + rnd(40) - 20;
        if (position > maxSize) {
            return maxSize - 1;
        } else if(position < 0) {
            return 0;
        }
        return position;
    }
    
    private static int changeColor(int color) {
        color = color + rnd(10) - 5;
        if(color > 255) {
            return 255;
        } else if(color < 0) {
            return 0;
        }
        return color;
    }
    
    private static boolean flipCoin() {
        return Math.random() > 0.5;
    }
    
    private static int rnd(int max) {
        return (int)(Math.random()*max);
    }

}
