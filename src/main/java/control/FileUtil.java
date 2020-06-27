package control;

import javafx.scene.image.Image;

public class FileUtil {
    
    public static Image getResourceStreamFromClass(final Class<?> c, final String path) {
        try {
            System.out.println(path);
            return new Image(c.getResourceAsStream(path));
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(path, e);
        }
    }

}
