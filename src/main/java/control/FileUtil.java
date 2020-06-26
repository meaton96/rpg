package control;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.IOException;

public class FileUtil {
    
    public static Image getResourceStreamFromClass(final Class c, final String path) {
        try {
            return new Image(c.getResourceAsStream(path));
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(path, e);
        }
    }

}
