package mathathon2017.util;

import java.util.ArrayList;
import java.util.List;

public class ImageBase {

    private List<Triangle> triangles = new ArrayList<>();

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
    }
    
    public String toString() {
        return triangles.toString();
    }
}
