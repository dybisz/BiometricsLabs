package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by dybisz on 16/10/2015.
 */
public class Grayscale {
    /**
     * Image to convert to grayscale;
     */
    Image image = null;

    public Grayscale(Image image) {
        this.image = image;
    }

    public WritableImage apply() {
            double width = image.getWidth();
            double height = image.getHeight();
            WritableImage destination = new WritableImage((int)width, (int)height);

            PixelReader pixelReader = image.getPixelReader();
            PixelWriter pixelWriter = destination.getPixelWriter();

            for (int readY = 0; readY < height; readY++) {
                for (int readX = 0; readX < width; readX++) {
                    Color color = pixelReader.getColor(readX, readY);
                    double red =  (color.getRed());
                    double green =  (color.getGreen());
                    double blue =  (color.getBlue());
                    double newSaturation = (red + green + blue) / 3.0;
                    Color grayscaleColor = new Color(newSaturation, newSaturation ,newSaturation, color.getOpacity());
                    pixelWriter.setColor(readX, readY, grayscaleColor);
                }
            }
            return destination;
    }
}
