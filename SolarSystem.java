import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class SolarSystem extends JPanel {

    static final int W = 950, H = 950;

    static class Planet {

        String name;
        int r, size;
        double speed, angle;
        Color color;

        public Planet(String name, int r, int size, double speed, Color color) {
            this.name = name;
            this.r = r;
            this.size = size;
            this.speed = speed;
            this.color = color;
        }

        void tick() {
            angle += speed;
        }
    }

    private final Planet[] planets = new Planet[] {
        new Planet("Mercury", 70, 6, 0.055, new Color(180, 170, 160)),
        new Planet("Venus", 110, 10, 0.040, new Color(206, 178, 120)),
        new Planet("Earth", 150, 10, 0.035, new Color(90, 140, 255)),
        new Planet("Mars", 185, 8, 0.030, new Color(220, 110, 90)),
        new Planet("Jupiter", 250, 18, 0.020, new Color(220, 190, 150)),
        new Planet("Saturn", 310, 16, 0.017, new Color(220, 200, 140)),
        new Planet("Uranus", 360, 12, 0.014, new Color(160, 200, 220)),
        new Planet("Neptune", 410, 12, 0.012, new Color(120, 150, 240))
    };

    private final Point center = new Point (W / 2, H / 2 - 60);
    private final int[][] stars;

    private int[][] genStars (int count, int w, int h) {
        Random rng = new Random(42);
        int[][] s = new int[count][3];
        for (int i = 0; i < count; i++) {
            s[i][0] = rng.nextInt(w);
            s[i][1] = rng.nextInt(h);
            s[i][2] = 1 + rng.nextInt(2);
        }
        return s;
    }

    public SolarSystem() {
        setBackground(new Color(10, 12, 20));
        stars = genStars(220, W, H);
        Timer timer = new Timer(16, e -> {
            for (Planet p : planets) {
                p.tick();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(230, 230, 255));
        for (int[] s : stars) {
            g2.fillOval(s[0], s[1], s[2], s[2]);
        }

        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(250, 255, 255, 40));
        for (Planet p : planets) {
            int d = p.r * 2;
            g.drawOval(center.x - p.r, center.y - p.r, d, d);
        }

        int sunSize = 36;

        GradientPaint sun = new GradientPaint(center.x - sunSize, center.y - sunSize, 
            new Color(255, 220, 120), center.x + sunSize, center.y + sunSize, 
            new Color(255, 140, 50));
        g2.setPaint(sun);
        g2.fillOval(center.x - sunSize, center.y - sunSize, sunSize * 2, sunSize * 2);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));

        for (Planet p : planets) {
            int px = center.x + (int) (p.r * Math.cos(p.angle));
            int py = center.y + (int) (p.r * Math.sin(p.angle));
            g2.setColor(p.color);
            g2.fillOval(px - p.size, py - p.size, p.size * 2, p.size * 2);

            if (p.name.equals("Saturn")) {
                g2.setColor(new Color(230, 220, 180));
                AffineTransform old = g2.getTransform();
                g2.translate(px, py);
                g2.rotate(-0.6);
                g2.drawOval(-p.size - 6, -p.size / 2, (p.size + 6) * 2, p.size);
                g2.setTransform(old);
            }

            g2.setColor(new Color(240, 240, 240));
            int lx = px + (px - center.x) / Math.max(1, (int) (p.r / 10.0)) + 8;
            int ly = py + (py - center.y) / Math.max(1, (int) (p.r / 10.0));
            g2.drawString(p.name, lx, ly);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Solar System Simulation");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(W, H);
            f.setLocationRelativeTo(null);
            f.setResizable(false);
            f.add(new SolarSystem());
            f.setVisible(true);
        });
    }
}

