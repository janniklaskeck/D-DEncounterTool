package app.bvk.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WrappedImageView extends ImageView {

    WrappedImageView() {
        setPreserveRatio(false);
    }

    public WrappedImageView(final Image img) {
        super(img);
        setPreserveRatio(true);
    }

    @Override
    public double maxWidth(final double height) {
        return 16384;
    }

    @Override
    public double minWidth(final double height) {
        return 40;
    }

    @Override
    public double prefWidth(final double height) {
        Image img = getImage();
        if (img == null) {
            return minWidth(height);
        }
        return img.getWidth();
    }

    @Override
    public double maxHeight(final double width) {
        return 16384;
    }

    @Override
    public double minHeight(final double width) {
        return 40;
    }

    @Override
    public double prefHeight(final double width) {
        Image img = getImage();
        if (img == null) {
            return minHeight(width);
        }
        return img.getHeight();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(final double width, final double height) {
        setFitWidth(width);
        setFitHeight(height);
    }
}
