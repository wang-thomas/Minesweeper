import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FlagObj extends GameObj{
	public static final String IMG_FILE = "files/flag.png";
    public static final int SIZE = 20;

    private static BufferedImage img;

    public FlagObj(int xPos, int yPos, int courtWidth, int courtHeight) {
        super(0, 0, xPos, yPos, SIZE, SIZE, courtWidth, courtHeight);

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}
