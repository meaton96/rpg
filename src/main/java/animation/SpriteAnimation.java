package animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lombok.Getter;


@Getter
public class SpriteAnimation extends Transition {
    
    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;
    private final Duration duration;
    private int lastIndex;
    
    public SpriteAnimation(ImageView imageView, Duration duration, int count, int columns, int offsetX, int offsetY, int width, int height) {
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
    public SpriteAnimation(SpriteAnimation animation) {
        this.duration = animation.duration;
        this.imageView = animation.imageView;
        this.count = animation.count;
        this.columns = animation.columns;
        this.offsetX = animation.offsetX;
        this.offsetY = animation.offsetY;
        this.width = animation.width;
        this.height = animation.height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
        lastIndex = animation.lastIndex;
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
    public void setLoc(double x, double y) {
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        
    }
    public void setScene(Group group) {
        if (!group.getChildren().contains(imageView))
            group.getChildren().add(imageView);
        else
            imageView.setOpacity(1);
    }
    public void hide() {
        imageView.setOpacity(0);
    }
    public void unHide() {
        imageView.setOpacity(1);
    }
    public void removeFromScene(Group group) {
        group.getChildren().remove(imageView);
    }
    
}
