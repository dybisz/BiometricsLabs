package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Based on  "A novel method for detecting lips, eyes and faces in real time"
 * paper by Cheng-Chin Chiang, Wen-Kai Tai, Mau-Tsuen Tang, Yi-Ting Huang and
 * Chi-Jaung Huang.
 *
 * Created by dybisz on 30/10/2015.
 */
public class FaceDetection extends Filter {
    public FaceDetection(Image inputImage) {
        super(inputImage);
    }

    @Override
    public WritableImage apply() {
        PixelReader pixelReader = getInputImage().getPixelReader();
        PixelWriter pixelWriter = getOutputImage().getPixelWriter();

        for (int x = 1; x < getImageWidth() - 1; x++) {
            for (int y = 1; y < getImageHeight() - 1; y++) {
                Color inputColor = pixelReader.getColor(x, y);
                Color outputColor;

                double[] chromaticValues = convertToChromaticSpace(inputColor.getRed(),
                        inputColor.getGreen(), inputColor.getBlue());

                /* R1 */
                boolean betweenPolynomials = chromaticValues[1] > fLower(chromaticValues[0]) &&
                        chromaticValues[1] < fUpper(chromaticValues[0]);

                /* R2 */
                boolean inBrightCircle = ( (chromaticValues[0] - 0.33)*(chromaticValues[0] - 0.33)
                        + (chromaticValues[1] - 0.33)*(chromaticValues[1] - 0.33) <= 0.0004);

                /* R3 */
                boolean correctInequality = inputColor.getRed() > inputColor.getGreen() &&
                        inputColor.getGreen() > inputColor.getBlue();

                /* R4 */
                boolean differenceRG = inputColor.getRed() - inputColor.getGreen() >= 45.0 / 255.0;

                /* LIPS */
                boolean lips = (
                        (fLower(chromaticValues[0]) <= chromaticValues[1])
                        && (chromaticValues[1] <= l(chromaticValues[0]))
                        && ((inputColor.getRed() * 255.0) >= 20)
                        && ((inputColor.getGreen() * 255.0) >= 20)
                        && ((inputColor.getBlue() * 255.0) >= 20)
                );

                if(betweenPolynomials && !inBrightCircle && correctInequality && differenceRG) {

                    if(lips) {
                        outputColor = new Color(1.0, 0.0, 0.0, 1.0);
                    } else {
                        outputColor = new Color(1.0, 1.0, 1.0, 1.0);
                    }
                }else {
                    outputColor = new Color(0, 0, 0, 1.0);
                }
                pixelWriter.setColor(x, y, outputColor);
            }
        }
        return getOutputImage();
    }

    private double[] convertToChromaticSpace(double r, double g, double b) {
        double[] chromaticValues = new double[2];
        chromaticValues[0] = r / (r + g + b); // r
        chromaticValues[1] = g / (r + g + b); // g
        return chromaticValues;
    }

    private double fUpper(double rChromatic) {
        return -1.3767 * rChromatic * rChromatic + 1.0743 * rChromatic + 0.1452;
    }

    private double fLower(double rChromatic) {
        return -0.776 * rChromatic * rChromatic + 0.5601 * rChromatic + 0.1766;
    }

    private double l(double rChromatic) {
        return -0.776 * rChromatic * rChromatic + 0.5601 * rChromatic + 0.2123;
    }
}
