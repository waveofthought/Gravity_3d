/**
 * Created by Alex on 11/21/2014.
 */
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends Canvas {
    ArrayList<Particle> list;
    QuadTree tree;
    Path path;
    boolean[] keys;
    double scale, x, y, intX, intY, currX, currY;
    boolean mouseDown;
    final static int WIDTH = 1920;
    final static int HEIGHT = 1080;
    int brushSize = 0;
    public Game() {
        keys = new boolean[65536];
        scale = 1;
        x = 0;
        y = 0;
        list = new ArrayList<Particle>(0);
//        for(int i = 0; i < 10; i++) {
////            double x = Math.random() * 800 - 400;
////            double y = Math.random() * 800 - 400;
//            double angle = Math.random() * Math.PI * 2;
//            double rad = Math.sqrt(Math.random()) * 50;
//            double  weight = Math.random();
//            list.add(new Particle(weight * weight + 1, Math.cos(angle) * rad + 4000, Math.sin(angle) * rad ,-2,0,2,Color.DARK_GRAY, 0));
//        }
//        for(int i = 0; i < 2; i++) {
////            double x = Math.random() * 800 - 400;
////            double y = Math.random() * 800 - 400;
//            double angle = Math.random() * Math.PI * 2;
//            double rad = Math.sqrt(Math.random()) * 1000;
//            double  weight = Math.random();
//            list.add(new Particle(weight * weight * weight + 1, Math.cos(angle) * rad + 10000 , Math.sin(angle) * rad, Math.sin(angle) * rad / 20000 + (Math.random() - 0.5)/ 2, - Math.cos(angle) * rad / 20000 + (Math.random() - 0.5) / 2 + 0.05, 2, Color.DARK_GRAY, 0));
//        }
        placeParticles(40, 2, 1, 500, 300, 0, 0);
//        placeParticles(30,2,1,700,1000,0,-1);

//        placeParticles(1,5,10000,0,0,0,0);
//        for(int i = 0; i < 1000; i++) {
////            double x = Math.random() * 800 - 400;
////            double y = Math.random() * 800 - 400;
//            double angle = Math.random() * Math.PI * 2;
//            double rad = Math.sqrt(Math.random()) * 20;
//            double  weight = Math.random();
//            list.add(new Particle(weight * weight * weight + 1, Math.cos(angle) * rad + 100, Math.sin(angle) * rad, Math.sin(angle) * rad / 700 + (Math.random() - 0.5)/ 10, - Math.cos(angle) * rad / 700 + (Math.random() - 0.5) / 10 - 0.05, 1, Color.DARK_GRAY, 0));
//        }
        tree = new QuadTree(list);
        path = new Path();
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        };
        addKeyListener(keyListener);
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDown = true;
                intX = e.getX();
                intY = e.getY();
                currX = e.getX();
                currY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(mouseDown) {
                    placeParticles(brushSize, 2, 1, intX, intY, (intX - e.getX()) / scale / 500, (intY - e.getY()) / scale / 500);
                    System.out.println(tree.root.list.size());
                    mouseDown = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        MouseMotionListener mouseMove = new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                currX = e.getX();
                currY = e.getY();

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseMove);
    }
    public void placeParticles(double size, double rad, double mass, double x, double y, double vx, double vy) {
        for(int i = 0; i < size * size * 4; i++) {
            double fx = i % ((int) size * 2) * 0.8660254 - (double) size / 2;
            double fy = i / ((int)size * 2) + 0.25 - (double)size / 2;
            if(i % 2 == 0) {
                fy = i / ((int)size * 2) - 0.25 - (double)size / 2;
            }
            if(fx * fx + fy * fy < size * size / 4)
            list.add(new Particle(mass, rad * fx * 2 + (x  - WIDTH / 2) / scale - this.x, rad * fy * 2 + (y - HEIGHT / 2) / scale - this.y, vx, vy, rad, Color.darkGray, 0));
        }
    }
    public void render(Graphics2D g2d) {
        tree.renderParticles(g2d, x, y, scale);
        if(mouseDown)
        path.render(g2d, x, y, scale);
        if(keys[KeyEvent.VK_T]) {
            tree.renderNodes(g2d, x, y, scale);
        }
    }
    public void update() {
        if(keys[KeyEvent.VK_S]) {
            y -= 5 / scale;
        }
        if(keys[KeyEvent.VK_W]) {
            y += 5 / scale;
        }
        if(keys[KeyEvent.VK_D]) {
            x -= 5 / scale;
        }
        if(keys[KeyEvent.VK_A]) {
            x += 5 / scale;
        }
        if(keys[KeyEvent.VK_EQUALS]) {
           scale /= 0.99;
        }
        if(keys[KeyEvent.VK_MINUS]) {
            scale *= 0.99;
        }
        if(keys[KeyEvent.VK_Z]) {
            brushSize++;
        }
        if(keys[KeyEvent.VK_X]) {
            brushSize--;
        }
        for(int i = 0; i < list.size(); i++) {
            tree.root.calcGravity(list.get(i));
            tree.root.calcCollision(list.get(i));
        }
        for(int i = 0; i < list.size(); i++) {
            list.get(i).updatePos();
            list.get(i).tempToColor();
        }
        if(mouseDown)
        path.simulate(tree.root, (intX - WIDTH / 2) / scale - x, (intY - HEIGHT / 2) / scale - y, (intX - currX) / scale / 500, (intY - currY) / scale / 500);
        tree.recalcDim();
        tree.root.sort();
    }
}


