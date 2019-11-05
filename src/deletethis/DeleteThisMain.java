package deletethis;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DeleteThisMain implements Runnable {
    private static final Image HOLE;
    static {
        BufferedImage temp = null;
        try {
            temp = ImageIO.read(new File("hole.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HOLE = temp.getScaledInstance(60, 120, Image.SCALE_SMOOTH);
        }
    }
    
    public static void main(String[] args) {
        new Thread(new DeleteThisMain()).start();
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Delete this");
        frame.add(new FakeBackground());
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }
    
    final class FakeBackground extends JPanel {
        private BufferedImage screenCapture;
        private ArrayList<Point> points;
        public FakeBackground() {
            try {
                Robot r = new Robot();
                screenCapture = r.createScreenCapture(new Rectangle(new Point(0, 0),
                        Toolkit.getDefaultToolkit().getScreenSize()));
            } catch (AWTException ex) {
                ex.printStackTrace(System.err);
            }
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    points.add(e.getPoint());
                    repaint();
                }
            });
            
            points = new ArrayList<>();
        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(screenCapture, 0, 0, null);
            for (Point p : points) {
                g.drawImage(HOLE, p.x - HOLE.getWidth(null) / 2, 
                        p.y - HOLE.getHeight(null) / 2, null);
            }
        }
        
    }
}