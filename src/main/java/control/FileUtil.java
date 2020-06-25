package control;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FileUtil {
    
    public static BufferedImage getResourceStreamFromClass(final Class c, final String path)
    {
        try
        {
            synchronized (ImageIO.class)
            {
                return ImageIO.read(c.getResourceAsStream(path));
            }
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException(path, e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(path, e);
        }
    }

}
