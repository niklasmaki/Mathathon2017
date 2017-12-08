package mathathon2017;

import java.awt.image.BufferedImage;

import mathathon2017.util.Coordinate;
import mathathon2017.util.ImageBase;
import mathathon2017.util.Triangle;

public class Triangler {
    public static void main(String[] args) {
        String logo = "solinor_avatar.png";
        BufferedImage image = ImageUtils.getImage(logo);
        ImageBase base = new ImageBase();
        for(int i = 0; i<30; i++) {
            base.getTriangles().add(createRandomTriangle(image.getWidth(), image.getHeight()));
        }
        
        long distance = ImageUtils.compare(base, image);
        System.out.println("0: distance: " + distance);
        System.out.println(base);
        int index = 0;
        
        while(true) {
            Triangle original = base.getTriangles().remove(0);
            Triangle mutated = mutate(original, image);
            base.getTriangles().add(mutated);
            long newDistance = ImageUtils.compare(base, image);
            if(newDistance < distance) {
                distance = newDistance;
                System.out.println(index + ": distance: " + distance);
                System.out.println(base);
                index++;
                if(index%10==0) {
                    ImageUtils.saveImage(base, "pics/best" + index, image.getWidth(), image.getHeight());
//                    ImageUtils.submitPicture(base); 
                }
            }
            else {
                base.getTriangles().remove(base.getTriangles().size()-1);
                base.getTriangles().add(original);
            }
        }
    }
    
    private static Triangle createRandomTriangle(int width, int height) {
        Triangle t = new Triangle();
        t.a = new Coordinate(rnd(height), rnd(width));
        t.b = new Coordinate(rnd(height), rnd(width));
        t.c = new Coordinate(rnd(height), rnd(width));
        t.setColor(rnd(255), rnd(255), rnd(255));
        t.opacity = 10 + rnd(245);
        return t;
    }
    private static Triangle mutate(Triangle triangle, BufferedImage image) {
        Triangle t = triangle.copy();
        if(flipCoin()) {
            switch(rnd(6)){
            case 0:
                t.a.x = rnd(image.getHeight());
                break;
            case 1:
                t.a.y = rnd(image.getWidth());
                break;
            case 2:
                t.b.x = rnd(image.getHeight());
                break;
            case 3:
                t.b.y = rnd(image.getWidth());
                break;
            case 4:
                t.c.x = rnd(image.getHeight());
                break;
            case 5:
                t.c.y = rnd(image.getWidth());
            default:
                break;
            }
        }
        else {
            switch(rnd(4)){
            case 0:
                t.red = rnd(255);
                break;
            case 1:
                t.green = rnd(255);
                break;
            case 2:
                t.blue = rnd(255);
                break;
            case 3:
                t.opacity = 10+rnd(245);
                break;
            }
        }
        return t;
    }
    
    private static boolean flipCoin() {
        return Math.random() > 0.5;
    }
    
    private static int rnd(int max) {
        return (int)(Math.random()*max);
    }

}
