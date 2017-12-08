package mathathon2017;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import static mathathon2017.ImageUtils.drawImage;

import mathathon2017.util.Coordinate;
import mathathon2017.util.ImageBase;
import mathathon2017.util.Triangle;

public class Triangler {
    public static void main(String[] args) {
        String logo = "talvi.png";
        BufferedImage image = ImageUtils.getImage(logo);
        EdgeDetector edgeDetector = new EdgeDetector();
        List<Coordinate> edgeCoordinates = edgeDetector.getEdgeCoordinates(image);
        
        ImageBase base = new ImageBase();
        for(int i = 0; i<30; i++) {
            base.getTriangles().add(createRandomEdgePointTriangle(edgeCoordinates,image));
        }
        
        long distance = ImageUtils.compare(base, image);
        System.out.println("0: distance: " + distance);
        System.out.println(base);
        int index = 0;
        while(true) {
            Triangle original = base.getTriangles().remove(0);
            Triangle mutated = mutate(original, image,distance);
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
    
    private static Triangle createRandomEdgePointTriangle(List<Coordinate> edgeCoords,BufferedImage image) {
        Triangle t = new Triangle();
        t.a = edgeCoords.get(rnd(edgeCoords.size()));
        t.b = edgeCoords.get(rnd(edgeCoords.size()));
        t.c = edgeCoords.get(rnd(edgeCoords.size()));
        setColorOfTriangle(image,t);
        t.opacity = 255;
        return t;
    }
    
    private static Triangle createRandomTriangle(int width, int height) {
        Triangle t = new Triangle();
        t.a = new Coordinate(rnd(height), rnd(width));
        t.b = new Coordinate(rnd(height), rnd(width));
        t.c = new Coordinate(rnd(height), rnd(width));
        t.setColor(5, 172, 240);
        t.opacity = 100;
        return t;
    }
    
    private static void setColorOfTriangle(BufferedImage image, Triangle t) {
        int x = (t.a.x + t.b.x + t.c.x)/3;
        int y = (t.a.y + t.b.y + t.c.y)/3;
        int numberColor = image.getRGB(x, y);
        Color color = new Color(numberColor);
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        t.setColor(red, green, blue);
    }
    
    private static Triangle mutate(Triangle triangle, BufferedImage image,long distance) {
        Triangle t = triangle.copy();
        if(flipCoin()) {
            switch(rnd(6)){
            case 0:
                t.a.x = changeCoordinates(t.a.x,image.getHeight(),distance);
                break;
            case 1:
                t.a.y = changeCoordinates(t.a.y,image.getWidth(),distance);
                break;
            case 2:
                t.b.x = changeCoordinates(t.b.x,image.getHeight(),distance);
                break;
            case 3:
                t.b.y = changeCoordinates(t.b.y,image.getWidth(),distance);
                break;
            case 4:
                t.c.x = changeCoordinates(t.c.x,image.getHeight(),distance);
                break;
            case 5:
                t.c.y = changeCoordinates(t.c.y,image.getWidth(),distance);
            default:
                break;
            }
        }
        else {
            switch(rnd(4)){
            case 0:
                t.red = changeColor(t.red,distance);
                break;
            case 1:
                t.green = changeColor(t.green,distance);
                break;
            case 2:
                t.blue = changeColor(t.blue,distance);
                break;
            case 3:
                t.opacity = changeColor(t.opacity,distance);
                break;
            }
        }
        return t;
    }
    
    private static int changeCoordinates(int position,int maxSize, long distance) {
        int luku = (int) (7*distance/1000000 + 40);
        position = position + rnd(luku) - luku/2;
        if (position > maxSize) {
            return maxSize - 1;
        } else if(position < 0) {
            return 0;
        }
        return position;
    }
    
    private static int changeColor(int color,long distance) {
        int variluku = (int) (10 + 2*distance/1000000) ;
        color = color + rnd(variluku) - variluku/2;
        if(color > 255) {
            return 255;
        } else if(color < 0) {
            return 0;
        }
        return color;
    }
    
    private static boolean flipCoin() {
        return Math.random() > 0.3;
    }
    
    private static int rnd(int max) {
        return (int)(Math.random()*max);
    }

}
