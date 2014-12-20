import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

/**
 * Created by Alex on 11/21/2014.
 */


public class Main {
    JFrame frame;
    Game game;
    BufferStrategy strategy;
    long last, now, prev;
    int fps, oldFps;
    boolean running;
    public Main() {
        frame = new JFrame("Game");
        game = new Game();
        game.setBackground(Color.BLACK);
        frame.add(game);
        frame.setSize(game.WIDTH, game.HEIGHT);
        frame.setResizable(false);
        frame.setIgnoreRepaint(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game.createBufferStrategy(2);
        strategy = game.getBufferStrategy();
        last = System.nanoTime();
        now = 0;
        prev = last;
        fps = 0;
        running = true;
    }
    public void gameLoop() {
        while(running) {
            Graphics g = strategy.getDrawGraphics();
            Graphics2D g2d = (Graphics2D) g;
            if(!game.keys[KeyEvent.VK_U])
            g2d.clearRect(0, 0, game.WIDTH, game.HEIGHT);
            game.render(g2d);
            g2d.setColor(Color.WHITE);
            if(game.mouseDown) {
                g2d.drawLine((int) game.intX, (int) game.intY, (int) game.currX, (int) game.currY);
                g2d.drawOval((int) (game.intX - game.brushSize * game.scale * 2), (int) (game.intY - game.brushSize * game.scale * 2), (int) (game.brushSize * game.scale * 4), (int) (game.brushSize * game.scale * 4));
            }
            g2d.drawString(String.valueOf(oldFps), 50, 50);
            g2d.drawString(String.valueOf(game.list.size()), 50, 70);
            g2d.drawString(String.valueOf(game.brushSize), 50, 90);
            g.dispose();
            strategy.show();
            game.update();
            now = System.nanoTime();
            if(now - last >= 1000000000) {
                System.out.println(fps);
                oldFps = fps;
                fps = 0;
                last = now;
            }
            if(now - prev <= 16666666) {
                try {
                    Thread.sleep((16666666 - (now - prev)) / 500000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            prev = now;
            fps++;
        }
    }

    public static void main(String [] args) throws InterruptedException {
        int i = 3;
        double d = 2.5;
        Main m = new Main();
        m.gameLoop();
    }
}
