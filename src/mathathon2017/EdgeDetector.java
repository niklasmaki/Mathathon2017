/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathathon2017;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mathathon2017.util.Coordinate;

/**
 *
 * @author Niklas
 */
public class EdgeDetector {
    
    private final double EPSILON =  0.5;
    
    public List<Coordinate> getEdgeCoordinates(BufferedImage image) {
        Set<Coordinate> coords = new HashSet<>();
        int previousRgb = image.getRGB(0,0);
        //detect edges horizontally
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x,y);
                if (Math.abs(rgb - previousRgb) > EPSILON) {
                    coords.add(new Coordinate(x, y));
                }
                previousRgb = rgb;
            }
        }
        
        //detect edges vertically
        previousRgb = image.getRGB(0,0);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x,y);
                if (Math.abs(rgb - previousRgb) > EPSILON) {
                    coords.add(new Coordinate(x, y));
                }
                previousRgb = rgb;
            }
        }
        
        return new ArrayList<>(coords);
    }
    
}
