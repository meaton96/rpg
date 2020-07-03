package animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lombok.Getter;

import java.io.IOException;

@Getter
public class SpriteAnimation extends Transition {           //implement worker?
    
    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;
    private final Duration duration;
    private int lastIndex;
    
    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count,   int columns,
            int offsetX, int offsetY,
            int width,   int height) {
        this.duration = duration;
        this.imageView = imageView;
        this.count     = count;
        this.columns   = columns;
        this.offsetX   = offsetX;
        this.offsetY   = offsetY;
        this.width     = width;
        this.height    = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }
    
    public void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width  + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }


    public void play(Group group, double x, double y) {
        for (Object o : group.getChildren()) {
            if (o instanceof ImageView) {
                if (!o.equals(imageView)) {
                    ((ImageView) o).setOpacity(0);
                }
                else
                    ((ImageView) o).setOpacity(1);
            }

        }
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        if (!group.getChildren().contains(imageView))
            group.getChildren().add(imageView);
        else
            imageView.setOpacity(1);

        super.play();

    }
}
