package app.bvk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WrappedImageView extends ImageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(WrappedImageView.class);
    private static final int MIN_PIXELS = 10;

    public WrappedImageView(final VBox container, final Image img) {
        super(img);
        setPreserveRatio(true);
        Image image = img;
        double width = image.getWidth();
        double height = image.getHeight();

        setPreserveRatio(true);
        reset(this, width / 2, height / 2);

        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        setOnMousePressed(e -> {
            Point2D mousePress = imageViewToImage(this, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(this, new Point2D(e.getX(), e.getY()));
            shift(this, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(this, new Point2D(e.getX(), e.getY())));
        });

        setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = this.getViewport();
            double scale = clamp(Math.pow(1.01, delta),
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight()));
            Point2D mouse = imageViewToImage(this, new Point2D(e.getX(), e.getY()));
            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;
            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image
            // solving this for newViewportMinX gives
            // newViewportMinX = x - (x - currentViewportMinX) * scale
            // we then clamp this value so the image never scrolls out
            // of the imageview:
            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 0, height - newHeight);
            setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(this, width, height);
            }
        });

        // HBox buttons = createButtons(width, height, imageView);
        // Tooltip tooltip = new Tooltip("Scroll to zoom, drag to pan");
        // Tooltip.install(buttons, tooltip);

        fitWidthProperty().bind(container.widthProperty());
        fitHeightProperty().bind(container.heightProperty());

    }

    private HBox createButtons(double width, double height, ImageView imageView) {
        Button reset = new Button("Reset");
        reset.setOnAction(e -> reset(imageView, width / 2, height / 2));
        Button full = new Button("Full view");
        full.setOnAction(e -> reset(imageView, width, height));
        HBox buttons = new HBox(10, reset, full);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10));
        return buttons;
    }

    // reset to the top left:
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(double value, double min, double max) {

        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual
    // image:
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

}
