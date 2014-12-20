import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Alex on 11/21/2014.
 */
public class QuadTree {
    public ArrayList<Particle> list;
    public double x, y;
    public double dim;
    public Node root;
    public QuadTree(ArrayList<Particle> T) {
        list = T;
        dim = 0;
        recalcDim();
        root = new Node(list, x, y, dim);
    }
    public void recalcDim() {
        x = 1e99;
        y = 1e99;
        dim = 0;
        int i = 0;
        for(i = 0; i < list.size(); i++) {
            Particle particle = list.get(i);
            if(particle.x < x) {
                x = particle.x;
            }
            if(particle.y < y) {
                y = particle.y;
            }
        }
        for(i = 0; i < list.size(); i++) {
            Particle particle = list.get(i);
            if(particle.x > dim + x) {
                dim = particle.x - x;
            }
            if(particle.y > dim + y) {
                dim = particle.y - y;
            }
        }
        x -= dim / 100;
        y -= dim / 100;
        dim += dim / 50;
        if(this.root != null) {
            root.x = this.x;
            root.y = this.y;
            root.dim = this.dim;
        }
    }
    public void renderNodes(Graphics2D g2d, double x, double y, double scale) {
        root.render(g2d, x, y, scale);
    }
    public void renderParticles(Graphics2D g2d, double x, double y, double scale) {
        for(int i = 0; i < list.size(); i++) {
            list.get(i).render(g2d, x, y, scale);
        }
    }

}

