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
public class HistogramNormalization extends Filter {
    public enum NormalizationType {AVERAGE, PER_CHANNEL}

    private NormalizationType type;
    private Pixel minSaturation;
    private Pixel maxSaturation;

    public HistogramNormalization(Image inputImage, NormalizationType type) {
        super(inputImage);
        this.type = type;
        this.minSaturation = new Pixel(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        this.maxSaturation = new Pixel(-1.0, -1.0, -1.0);
    }

    /**
     * Normalizes histogram of input image ({@link #inputImage}) according to preferred
     * type ({@link #type}.
     *
     * @return Processed image, with normalized histogram; {@link #outputImage}.
     */
    @Override
    public WritableImage apply() {
        findMinMaxPixelsPerChannel();
        decideAboutNormalizationType();
        return normalizeHistogram();
    }

    private void findMinMaxPixelsPerChannel() {
        PixelReader pixelReader = getInputImage().getPixelReader();

        for (int x = 0; x < getImageWidth(); x++) {
            for (int y = 0; y < getImageHeight(); y++) {
                Color inputColor = pixelReader.getColor(x, y);
                Pixel inputPixel = new Pixel(inputColor);

                minSaturation.compareAndSetMinimumR(inputPixel.getR());
                minSaturation.compareAndSetMinimumG(inputPixel.getG());
                minSaturation.compareAndSetMinimumB(inputPixel.getB());

                maxSaturation.compareAndSetMaximumR(inputPixel.getR());
                maxSaturation.compareAndSetMaximumG(inputPixel.getG());
                maxSaturation.compareAndSetMaximumB(inputPixel.getB());
            }
        }
    }

    private void decideAboutNormalizationType() {
        switch (type) {
            /* To preserve generality of performPerChannelHistogramNormalization method,
               we set minimum and maximum for all "per channel operations" at the same value,
               which is an average of minimal and maximal saturation of channels, respectively */
            case AVERAGE:
                double min = getAverageFromChannels(minSaturation);
                double max = getAverageFromChannels(maxSaturation);
                minSaturation.setRGB(min, min, min);
                maxSaturation.setRGB(max, max, max);
                break;
        }
    }

    private double getAverageFromChannels(Pixel pixel) {
        return (pixel.getR() + pixel.getG() + pixel.getB()) / 3.0;
    }

    private WritableImage normalizeHistogram() {
        PixelReader inputReader = getInputImage().getPixelReader();
        PixelWriter outputWriter = getOutputImage().getPixelWriter();

        for (int x = 0; x < getImageWidth(); x++) {
            for (int y = 0; y < getImageHeight(); y++) {
                Color inputColor = inputReader.getColor(x, y);
                Pixel inputPixel = new Pixel(inputColor);
                Pixel outputPixel = performPerChannelHistogramNormalization(inputPixel);
                outputPixel.clampAllChannels();
                outputWriter.setColor(x, y, outputPixel.toColor());
            }
        }
        return getOutputImage();
    }

    private Pixel performPerChannelHistogramNormalization(Pixel pixel) {
        double redSaturation = (pixel.getR() - minSaturation.getR()) *
                (1.0 / maxSaturation.getR() - minSaturation.getR());
        double greenSaturation = (pixel.getG() - minSaturation.getG()) *
                (1.0 / maxSaturation.getG() - minSaturation.getG());
        double blueSaturation = (pixel.getB() - minSaturation.getB()) *
                (1.0 / maxSaturation.getB() - minSaturation.getB());
        return new Pixel(redSaturation, greenSaturation, blueSaturation);
    }


}
