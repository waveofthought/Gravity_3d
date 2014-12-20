import java.awt.*;

/**
 * Created by Alex on 12/9/2014.
 */
public class Path {
    double[] x, y;
    private Particle testP;
    double timeScale;
    int length = 2000;
    public Path() {
        x = new double[length];
        y = new double[length];
        timeScale = 1;
        testP = new Particle(1,0,0,0,0,2, Color.WHITE,0);
    }
    public void simulate(Node node, double x, double y, double vx, double vy) {
        testP.x = x;
        testP.y = y;
        testP.vx = vx;
        testP.vy = vy;
        for(int i = 0; i < length; i++) {
            this.x[i] = testP.x;
            this.y[i] = testP.y;
            node.calcGravity(testP);
//            node.calcCollision(testP);
            testP.updatePos();
        }
    }
    public void render(Graphics2D g2d, double gameX, double gameY, double gameScale) {
        g2d.setColor(Color.RED);
        for(int i = 0; i < length - 1; i++) {
            g2d.drawLine((int)((x[i] + gameX) * gameScale + Game.WIDTH / 2), (int)((y[i] + gameY) * gameScale + Game.HEIGHT / 2),(int)((x[i+1] + gameX) * gameScale + Game.WIDTH / 2), (int)((y[i+1] + gameY) * gameScale + Game.HEIGHT / 2));
        }
    }
}
