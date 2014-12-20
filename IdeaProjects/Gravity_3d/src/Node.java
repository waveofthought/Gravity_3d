import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Alex on 11/22/2014.
 */
public class Node {
    ArrayList<Particle> list;
    ArrayList<Node> children;
    double x, y, dim, cogx, cogy, mass;
    int size;
    public Node(ArrayList<Particle> list, double x, double y, double dim) {
        this.list = list;
        this.x = x;
        this.y = y;
        this.dim = dim;
        cogx = 0;
        cogy = 0;
        mass = 0;
        for(int i = 0; i < list.size(); i++) {
            Particle currElem = list.get(i);
            cogx += currElem.x / list.size();
            cogy += currElem.y / list.size();
            mass += currElem.mass;
        }
        children = new ArrayList<Node>(0);
        sort();
    }
    public void sort() {
        children.clear();
        if(list.size() > 1) {
            ArrayList<Particle> l1 = new ArrayList<Particle>(0);
            ArrayList<Particle> l2 = new ArrayList<Particle>(0);
            ArrayList<Particle> l3 = new ArrayList<Particle>(0);
            ArrayList<Particle> l4 = new ArrayList<Particle>(0);
            double halfDim = dim / 2;
            for(int i = 0; i < list.size(); i++) {
                Particle currElem = list.get(i);
                if (currElem.x > x && currElem.x <= x + halfDim && currElem.y > y && currElem.y <= y + halfDim) {
                    l1.add(currElem);
                }
                if (currElem.x > x + halfDim && currElem.x <= x + dim && currElem.y > y && currElem.y <= y + halfDim) {
                    l2.add(currElem);
                }
                if (currElem.x > x && currElem.x <= x + halfDim && currElem.y > y + halfDim && currElem.y <= y + dim) {
                    l3.add(currElem);
                }
                if (currElem.x > x + halfDim && currElem.x <= x + dim && currElem.y > y + halfDim && currElem.y <= y + dim) {
                    l4.add(currElem);
                }
            }
            if(l1.size() > 0) children.add(new Node(l1, x, y, halfDim));
            if(l2.size() > 0) children.add(new Node(l2, x + halfDim, y, halfDim));
            if(l3.size() > 0) children.add(new Node(l3, x, y + halfDim, halfDim));
            if(l4.size() > 0) children.add(new Node(l4, x + halfDim, y + halfDim, halfDim));
        }

    }
    public void render(Graphics2D g2d, double x, double y, double scale) {
        g2d.setColor(Color.WHITE);
        g2d.drawRect((int)((this.x + x) * scale + Game.WIDTH / 2), (int)((this.y + y) * scale + Game.HEIGHT / 2), (int) (dim * scale), (int) (dim * scale));
        g2d.fillRect((int)((this.cogx + x) * scale + Game.WIDTH / 2), (int)((this.cogy + y) * scale + Game.HEIGHT / 2), 2, 2);
        for(int i = 0; i < children.size(); i++) {
            children.get(i).render(g2d, x, y, scale);
        }
    }
    public void calcGravity(Particle particle) {
        if(list.size() > 1) {
            double ratio = dim * dim / ((particle.x - cogx) * (particle.x - cogx) + (particle.y - cogy) * (particle.y - cogy));
            if(ratio <= 0.25) {
                particle.applyGravity(mass, cogx, cogy);
            } else {
                for(int i = 0; i < children.size(); i++) {
                    children.get(i).calcGravity(particle);
                }
            }
        }
        if(list.size() == 1 && list.get(0) != particle) {
            particle.applyGravity(mass, cogx, cogy);
        }
    }
    public void calcCollision(Particle particle) {
        if(x - particle.x < particle.rad * 2) {
            if (particle.x - x - dim < particle.rad * 2) {
                if (particle.y - y - dim < particle.rad * 2) {
                    if (y - particle.y < particle.rad * 2) {
                        if (list.size() > 1) {
                            for (int i = 0; i < children.size(); i++) {
                                children.get(i).calcCollision(particle);
                            }
                        } else {
                            if (list.get(0) != particle) {
                                particle.applySpringForce(list.get(0));
                            }
                        }
                    }
                }
            }
        }
    }
}
