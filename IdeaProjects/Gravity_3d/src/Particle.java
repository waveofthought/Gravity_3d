import java.awt.*;

/**
 * Created by Alex on 11/21/2014.
 */
public class Particle {
    double mass, x, y, vx, vy, fx, fy, rad, temp;
    int r, b, g, a;
    static final double k = 0.3;
    static final double damp = 0.1;
    static final double G = 0.02;
    Color color, tempColor;
    public Particle(double mass, double x, double y, double vx, double vy, double rad, Color color, double temp) {
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.rad = rad;
        this.color = color;
        this.tempColor = color;
        this.r = color.getRed();
        this.b = color.getBlue();
        this.g = color.getGreen();
        this.a = color.getAlpha();
        this.temp = temp;
        this.fx = 0;
        this.fy = 0;
    }
    public void applySpringForce(Particle p) {
        double dx = (p.x - x);
        double dy = (p.y - y);
        double r = Math.sqrt(dx * dx + dy * dy);
        double dist = rad + p.rad;
//        if(r < dist * (1 + 0.2 / (1 + p.temp + temp))) {
        if(r < dist) {
            double nx = dx / r;
            double ny = dy / r;
            double d = ((p.vx - vx) * nx + (p.vy - vy) * ny) * damp;
            double fMag = (dist - r) * k - d;
            fx -= fMag * nx;
            fy -= fMag * ny;
            temp += Math.abs(d) *2  + (p.temp - this.temp)  / 100  ;
        }
    }
    public void applyGravity(double mass, double x, double y) {
        double dx = (x - this.x);
        double dy = (y - this.y);
        double r2 = dx * dx + dy * dy;
        double r = Math.sqrt(r2);
        if(r > rad / 2) {
            double grav = G * mass * this.mass / r2;
            this.fx += dx / r * grav;
            this.fy += dy / r * grav;
        }
    }
    public void updatePos() {
        vx += fx / mass;
        vy += fy / mass;
        x += vx;
        y += vy;
        fx = 0;
        fy = 0;
        temp *= 0.993;
    }
    public void tempToColor(){
        if(temp<1){
            tempColor = new Color((int)lerp(0,1,temp,r,255),(int)lerp(0,1,temp,g,0),(int)lerp(0,1,temp,b,0),a);
        }
        if(temp<3 && temp>=1){
            tempColor = new Color(255,(int)lerp(1,3,temp,0,200),0,(int)lerp(1,5,temp,a,255));
        }
        if(temp<6 && temp>=3){
            tempColor = new Color(255,(int)lerp(3,6,temp,200,255),0,(int)lerp(0,6,temp,a,255));
        }
        if(temp<15 && temp>=6){
            tempColor = new Color(255,255,(int)lerp(6,15,temp,0,255),255);
        }
        if(temp>=15){
            tempColor = new Color(255,255,255,255);
        }
    }
    public double lerp(double iMin, double iMax, double i, double oMin, double oMax){
        return((i-iMin)/(iMax-iMin)*(oMax-oMin)+oMin);
    }
    public void render(Graphics2D g2d, double x, double y, double scale) {
        g2d.setColor(tempColor);
        g2d.fillOval((int)((this.x + x) * scale + Game.WIDTH / 2  - rad * scale),(int)((this.y + y) * scale + Game.HEIGHT / 2 - rad * scale),Math.max((int)(Math.ceil(rad * 2 * scale * 1.3 )),2), Math.max((int)Math.ceil((rad * 2 * scale * 1.3)),2));
    }
}
