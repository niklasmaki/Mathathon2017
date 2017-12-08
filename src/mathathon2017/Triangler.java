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
        t.setColor(5, 172, 240);
        t.opacity = 255;
        return t;
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
        position = position + rnd(70) - 35;
        if (position > maxSize) {
            return maxSize - 1;
        } else if(position < 0) {
            return 0;
        }
        return position;
    }
    
    private static int changeColor(int color) {
        color = color + rnd(20) - 10;
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
