package app.bvk.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WrappedImageView extends ImageView {

    WrappedImageView() {
        setPreserveRatio(false);
    }

    public WrappedImageView(Image img) {
        super(img);
        setPreserveRatio(true);
    }

    @Override
    public double maxWidth(double height) {
        return 16384;
    }

    @Override
    public double minWidth(double height) {
        return 40;
    }

    @Override
    public double prefWidth(double height) {
        Image img = getImage();
        if (img == null) {
            return minWidth(height);
        }
        return img.getWidth();
    }

    @Override
    public double maxHeight(double width) {
        return 16384;
    }

    @Override
    public double minHeight(double width) {
        return 40;
    }

    @Override
    public double prefHeight(double width) {
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
    public void resize(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
    }
}
