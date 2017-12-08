package mathathon2017;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import mathathon2017.util.ImageBase;

public class ImageUtils {
    
    private static String teamName = "test";
    private static String resultServiceURL = "http://solinor.herokuapp.com/submit/" +teamName;
    public static BufferedImage getImage(String path) {
        File imgFile = new File(path);
        BufferedImage image = null;
        try {
            image = ImageIO.read(imgFile);

        } catch (Exception e) {
            System.out.println("Error with reading file " + imgFile.getAbsolutePath());
        }
        return image;
    }
    
    public static long compare(ImageBase base, BufferedImage original) {
        long distance = 0;
        int height = original.getHeight();
        int width = original.getWidth();
        BufferedImage image = drawImage(base, width, height);
        
        final byte[] originalBytes = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
        final byte[] newBytes = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        for(int i = 0; i < originalBytes.length; i+=4) {
        	//first pixel is opacity
        	for(int x=1; x<4; x++) distance += Math.abs(originalBytes[i+x]-newBytes[i+x]);
        }
            
        return distance;
    }

    public static BufferedImage drawImage(ImageBase base, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        base.getTriangles().forEach(t -> {
            Polygon p = new Polygon();
            p.addPoint(t.a.x, t.a.y);
            p.addPoint(t.b.x, t.b.y);
            p.addPoint(t.c.x, t.c.y);
            graphics.setColor(new Color(t.red, t.green, t.blue, t.opacity));
            graphics.drawPolygon(p);
            graphics.fillPolygon(p);
        });
        graphics.drawImage(image, null, 0, 0);
        graphics.dispose();
        return image;
    }
    
    public static void saveImage(ImageBase base, String fileName, int width, int height) {
        File output = new File(fileName + ".png");
        try {
            ImageIO.write(drawImage(base, width, height), "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public static boolean submitPicture(ImageBase image) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
        restTemplate.put(resultServiceURL, image);
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
        
    }
}
