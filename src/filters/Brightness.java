package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import utils.Pixel;

/**
 * Created by dybisz on 16/10/2015.
 */
public class Brightness extends Filter {

    private double brightness;

    public Brightness(Image inputImage) {
        super(inputImage);
    }

    /**
     * Modifies saturation of each pixel's channels by {@link #brightness} from
     * the input image i.e. {@link #inputImage}.
     *
     * @return Image with modified brightness along all channels; {@link #outputImage}.
     */
    @Override
    public WritableImage apply() {
        PixelReader pixelReader = getInputImage().getPixelReader();
        PixelWriter pixelWriter = getOutputImage().getPixelWriter();

        for (int x = 0; x < getImageWidth(); x++) {
            for (int y = 0; y < getImageHeight(); y++) {
                Color inputColor = pixelReader.getColor(x, y);
                Pixel pixel = new Pixel(inputColor);
                pixel.addValueToAllChannelsAndClamp(brightness);
                Color outputColor = pixel.toColor();
                pixelWriter.setColor(x, y, outputColor);
            }
        }

        return getOutputImage();
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

}
