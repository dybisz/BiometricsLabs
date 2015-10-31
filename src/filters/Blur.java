package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by dybisz on 16/10/2015.
 */
public class Blur {
    /**
     * Image to convert to grayscale;
     */
    Image image = null;

    /**
     * Size of the (square) convolution matrix. By default it is 3.
     */
    int kernelSize = 3;

    /**
     * Width of loaded inputImage.
     */
    double width;

    /**
     * Height of loaded inputImage
     */
    double height;

    /**
     * Output inputImage.
     */
    WritableImage destination;

    public Blur(Image image, int kernelSize) {
        this.image = image;
        this.kernelSize = kernelSize;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.destination = new WritableImage((int)this.width, (int)this.height);
    }

    public Blur(Image image) {

    }

    /**
     * Blurs {@link #image} and saves results to {@link #destination}.
     * @return Blurred inputImage.
     */
    public WritableImage apply() {
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = destination.getPixelWriter();

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double red = 0;
                double green = 0;
                double blue = 0;
                double alpha = 0;
                int count = 0;

                for (int i = -kernelSize; i <= kernelSize; i++) {
                    for (int j = -kernelSize; j <= kernelSize; j++) {
                        if (x + i < 0 || x + i >= width
                                || y + j < 0 || y + j >= height) {
                            continue;
                        }
                        Color color = pixelReader.getColor(x + i, y + j);
                        red += color.getRed();
                        green += color.getGreen();
                        blue += color.getBlue();
                        alpha += color.getOpacity();
                        count++;
                    }
                }
                Color blurColor = Color.color(red / count,
                        green / count,
                        blue / count,
                        alpha / count);
                pixelWriter.setColor(x, y, blurColor);
            }
        }
        return destination;
    }
}
