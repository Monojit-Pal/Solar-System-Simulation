import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * SolarSystem simulates a simple solar system with planets orbiting around the sun.
 * It uses Java Swing for drawing the simulation.
 *
 * @author Monojit-Pal
 */
public class SolarSystem extends JPanel {

    /**
     * Width and height of the window for the simulation.
     */
    static final int W = 950, H = 950;

    /**
     * Represents a planet orbiting the sun.
     */
    static class Planet {

        /** Name of the planet (e.g., Mercury, Venus). */
        String name;
        /** Distance from the sun (radius of orbit, in pixels). */
        int r;
        /** Size of the planet (diameter, in pixels). */
        int size;
        /** How fast the planet moves around the sun (angle increment per tick). */
        double speed;
        /** Current angle of the planet in its orbit. */
        double angle;
        /** Color used to draw the planet. */
        Color color;

        /**
         * Creates a new planet with the given properties.
         *
         * @param name  Name of the planet
         * @param r     Distance from the sun
         * @param size  Size of the planet
         * @param speed Speed of orbit
         * @param color Color of the planet
         */
        public Planet(String name, int r, int size, double speed, Color color) {
            this.name = name;
            this.r = r;
            this.size = size;
            this.speed = speed;
            this.color = color;
        }

        /**
         * Moves the planet forward in its orbit by increasing its angle.
         * This simulates the planet moving around the sun.
         */
        void tick() {
            angle += speed;
        }
    }

    /**
     * Array of planets in the solar system. Each planet orbits the sun.
     * The arguments are: name, orbit radius, size, speed, color.
     */
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

    /**
     * The center point of the solar system (where the sun is drawn).
     */
    private final Point center = new Point (W / 2, H / 2 - 60);

    /**
     * Array of stars, each with x, y position and size.
     */
    private final int[][] stars;

    /**
     * Generates random stars for the background.
     *
     * @param count Number of stars
     * @param w     Width of the area
     * @param h     Height of the area
     * @return      2D array with star positions and sizes
     */
    private int[][] genStars (int count, int w, int h) {
        Random rng = new Random(42);
        int[][] s = new int[count][3];
        for (int i = 0; i < count; i++) {
            s[i][0] = rng.nextInt(w); // x position
            s[i][1] = rng.nextInt(h); // y position
            s[i][2] = 1 + rng.nextInt(2); // star size (1 or 2)
        }
        return s;
    }

    /**
     * Constructor sets up the solar system simulation, background, stars, and animation timer.
     */
    public SolarSystem() {
        setBackground(new Color(10, 12, 20)); // Set background color (dark blue/black)
        stars = genStars(220, W, H); // Generate stars for the background
        Timer timer = new Timer(16, e -> {
            for (Planet p : planets) {
                p.tick(); // Move each planet forward in its orbit
            }
            repaint(); // Redraw the panel
        });
        timer.start(); // Start the animation
    }

    /**
     * Draws the solar system, including stars, orbits, sun, and planets.
     * This method is called automatically by Swing when the panel needs to be redrawn.
     *
     * @param g The graphics context to use for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw stars in the background
        g2.setColor(new Color(230, 230, 255));
        for (int[] s : stars) {
            g2.fillOval(s[0], s[1], s[2], s[2]);
        }

        // Draw orbits for each planet
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(250, 255, 255, 40));
        for (Planet p : planets) {
            int d = p.r * 2;
            g.drawOval(center.x - p.r, center.y - p.r, d, d);
        }

        // Draw the sun with a gradient
        int sunSize = 36;
        GradientPaint sun = new GradientPaint(center.x - sunSize, center.y - sunSize, 
            new Color(255, 220, 120), center.x + sunSize, center.y + sunSize, 
            new Color(255, 140, 50));
        g2.setPaint(sun);
        g2.fillOval(center.x - sunSize, center.y - sunSize, sunSize * 2, sunSize * 2);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Draw each planet and its name
        for (Planet p : planets) {
            // Calculate planet's position based on its orbit radius and angle
            int px = center.x + (int) (p.r * Math.cos(p.angle));
            int py = center.y + (int) (p.r * Math.sin(p.angle));
            g2.setColor(p.color);
            g2.fillOval(px - p.size, py - p.size, p.size * 2, p.size * 2);

            // Draw Saturn's ring
            if (p.name.equals("Saturn")) {
                g2.setColor(new Color(230, 220, 180));
                AffineTransform old = g2.getTransform();
                g2.translate(px, py);
                g2.rotate(-0.6);
                g2.drawOval(-p.size - 6, -p.size / 2, (p.size + 6) * 2, p.size);
                g2.setTransform(old);
            }

            // Draw planet name near the planet
            g2.setColor(new Color(240, 240, 240));
            int lx = px + (px - center.x) / Math.max(1, (int) (p.r / 10.0)) + 8;
            int ly = py + (py - center.y) / Math.max(1, (int) (p.r / 10.0));
            g2.drawString(p.name, lx, ly);
        }
    }

    /**
     * Main method to start the simulation. Creates the window and shows the solar system.
     *
     * @param args Command-line arguments (not used)
     */
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

