package app.bvk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class ZoomableImageView extends ImageView
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ZoomableImageView.class);
    private static final int MIN_PIXELS = 10;

    public ZoomableImageView(final Pane container, final Image image)
    {
        super(image);
        final double width = image.getWidth();
        final double height = image.getHeight();

        this.setPreserveRatio(true);
        this.reset(width / 2, height / 2);

        final ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        this.setOnMousePressed(e ->
        {
            final Point2D mousePress = this.imageViewToImage(new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        this.setOnMouseDragged(e ->
        {
            final Point2D dragPoint = this.imageViewToImage(new Point2D(e.getX(), e.getY()));
            this.shift(dragPoint.subtract(mouseDown.get()));
            mouseDown.set(this.imageViewToImage(new Point2D(e.getX(), e.getY())));
        });

        this.setOnScroll(e ->
        {
            final double delta = e.getDeltaY();
            final Rectangle2D viewport = this.getViewport();

            // don't scale so we're zoomed in to fewer than MIN_PIXELS
            // in any direction:
            final double minScale = Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight());
            // don't scale so that we're bigger than image dimensions:
            final double maxScale = Math.max(width / viewport.getWidth(), height / viewport.getHeight());
            final double scale = this.clamp(Math.pow(1.01, delta), minScale, maxScale);

            final Point2D mouse = this.imageViewToImage(new Point2D(e.getX(), e.getY()));

            final double newWidth = viewport.getWidth() * scale;
            final double newHeight = viewport.getHeight() * scale;

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image
            // solving this for newViewportMinX gives
            // newViewportMinX = x - (x - currentViewportMinX) * scale
            // we then clamp this value so the image never scrolls out
            // of the imageview:

            final double newMinX = this.clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0, width - newWidth);
            final double newMinY = this.clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 0, height - newHeight);
            this.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
            LOGGER.debug("minScale: {}, maxScale: {}, scale: {}", minScale, maxScale, scale);
            LOGGER.debug("mouse: {}", mouse);
            LOGGER.debug("newWidth: {}, newHeight: {}, newMinX: {}, newMinY: {}", newWidth, newHeight, newMinX, newMinY);
            LOGGER.debug("img width: {}", image.getWidth());
            LOGGER.debug("img height: {}", image.getHeight());
        });

        this.setOnMouseClicked(e ->
        {
            if (e.getClickCount() == 2)
            {
                this.reset(width, height);
            }
        });

        this.fitWidthProperty().bind(container.widthProperty());
        this.fitHeightProperty().bind(container.heightProperty());
    }

    // reset to the top left:
    private void reset(final double width, final double height)
    {
        this.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private void shift(final Point2D delta)
    {
        final Rectangle2D viewport = this.getViewport();

        final double width = this.getImage().getWidth();
        final double height = this.getImage().getHeight();

        final double maxX = width - viewport.getWidth();
        final double maxY = height - viewport.getHeight();

        final double minX = this.clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        final double minY = this.clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        this.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(final double value, final double min, final double max)
    {

        if (value < min)
        {
            return min;
        }
        if (value > max)
        {
            return max;
        }
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual
    // image:
    private Point2D imageViewToImage(final Point2D imageViewCoordinates)
    {
        final double xProportion = imageViewCoordinates.getX() / this.getBoundsInLocal().getWidth();
        final double yProportion = imageViewCoordinates.getY() / this.getBoundsInLocal().getHeight();

        final Rectangle2D viewport = this.getViewport();
        return new Point2D(viewport.getMinX() + xProportion * viewport.getWidth(), viewport.getMinY() + yProportion * viewport.getHeight());
    }
}
