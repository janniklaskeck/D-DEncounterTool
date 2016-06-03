package app.bvk.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WrappedImageView extends ImageView {

    private static final int MIN_PIXELS = 200;

    public WrappedImageView(final Scene container, final Image img) {
        super(img);
        setPreserveRatio(true);
        final double width = img.getWidth();
        final double height = img.getHeight();

        setPreserveRatio(true);
        reset(width, height);

        final ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        setOnMousePressed(e -> {
            final Point2D mousePress = imageViewToImage(new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        setOnMouseDragged(e -> {
            final Point2D dragPoint = imageViewToImage(new Point2D(e.getX(), e.getY()));
            shift(dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(new Point2D(e.getX(), e.getY())));
        });

        setOnScroll(e -> {
            final double delta = e.getDeltaY() * -1;
            final Rectangle2D viewport = this.getViewport();
            final double scale = clamp(Math.pow(1.01, delta),
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight()));
            final Point2D mouse = imageViewToImage(new Point2D(e.getX(), e.getY()));
            final double newWidth = viewport.getWidth() * scale;
            final double newHeight = viewport.getHeight() * scale;
            final double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0,
                    width - newWidth);
            final double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 0,
                    height - newHeight);
            setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(width, height);
            }
        });
        fitWidthProperty().bind(container.widthProperty());
        fitHeightProperty().bind(container.heightProperty());
    }

    private void reset(final double width, final double height) {
        this.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private void shift(final Point2D delta) {
        final Rectangle2D viewport = this.getViewport();
        final double width = this.getImage().getWidth();
        final double height = this.getImage().getHeight();
        final double maxX = width - viewport.getWidth();
        final double maxY = height - viewport.getHeight();
        final double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        final double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);
        this.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(final double value, final double min, final double max) {
        return Math.max(min, Math.min(max, value));
    }

    private Point2D imageViewToImage(final Point2D imageViewCoordinates) {
        final double xProportion = imageViewCoordinates.getX() / this.getBoundsInLocal().getWidth();
        final double yProportion = imageViewCoordinates.getY() / this.getBoundsInLocal().getHeight();

        final Rectangle2D viewport = this.getViewport();
        return new Point2D(viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

}
