package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by dybisz on 16/10/2015.
 */
public class Tresholding extends Filter {

    private double treshold;

    public Tresholding(Image inputImage, double treshold) {
        super(inputImage);
        this.treshold = treshold;
    }

    /**
     * Method applies thresholding (with treshold determined by{@link #treshold})
     * to the input inputImage (i.e. {@link #inputImage}. Treshold is assumed to
     * be from interval <0;255> for user convenience. Image must be grayscale.
     *
     * @return Image after tresholding ({@link #outputImage} field) or null in case
     * when error occurred.
     */
    @Override
    public WritableImage apply() {
        try {
            return applyTresholdingToImage();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return null;
    }

    private WritableImage applyTresholdingToImage() throws Exception {
        PixelReader inputReader = getInputImage().getPixelReader();
        PixelWriter outputWriter = getOutputImage().getPixelWriter();

        for (int x = 0; x < getImageWidth(); x++) {
            for (int y = 0; y < getImageHeight(); y++) {
                Color inputColor = inputReader.getColor(x, y);
                checkIfGrayscale(inputColor);
                Color outputColor = checkTreshold(inputColor);
                outputWriter.setColor(x, y, outputColor);
            }
        }
        return getOutputImage();
    }

    private void checkIfGrayscale(Color color) throws Exception {
        double red = color.getRed();
        double green = color.getGreen();
        double blue = color.getBlue();

        if (atLeastOneDiffers(red, green, blue)) {
            throw new NotGrayscaleException();
        }
    }

    private boolean atLeastOneDiffers(double val1, double val2, double val3) {
        return notEqual(val1, val2) || notEqual(val1, val3) || notEqual(val2, val3);
    }

    private boolean notEqual(double val1, double val2) {
        return (val1 != val2);
    }

    /**
     * @param inputColor Grayscale image.
     * @return Black or white color.
     */
    private Color checkTreshold(Color inputColor) {
        Color outputColor;
        double inputOpacity = inputColor.getOpacity();
        double inputSaturation = inputColor.getRed();

        if (saturationAboveTreshold(inputSaturation)) {
            outputColor = new Color(1, 1, 1, inputOpacity);
        } else {
            outputColor = new Color(0, 0, 0, inputOpacity);
        }
        return outputColor;
    }

    private boolean saturationAboveTreshold(double saturation) {
        if (255.0 * saturation > treshold) {
            return true;
        } else {
            return false;
        }
    }
}
