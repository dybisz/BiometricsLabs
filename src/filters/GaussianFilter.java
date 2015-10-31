package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import utils.Pixel;

/**
 * Created by dybisz on 30/10/2015.
 */
public class GaussianFilter extends Filter {
    private double[][] convolutionMat = {
            {1, 4, 1},
            {4, 16, 4},
            {1, 4, 1}
    };
    private int kernelSize = 1;

    public GaussianFilter(Image inputImage) {
        super(inputImage);
    }

    @Override
    public WritableImage apply() {
        PixelReader pixelReader = getInputImage().getPixelReader();
        PixelWriter pixelWriter = getOutputImage().getPixelWriter();

        for (int x = 1; x < getImageWidth() - 1; x++) {
            for (int y = 1; y < getImageHeight() - 1; y++) {
                Color outputColor = calculateKernel(pixelReader, x, y);
                pixelWriter.setColor(x, y, outputColor);
            }
        }


        return getOutputImage();
    }

    private Color calculateKernel(PixelReader pixelReader, int x, int y) {
        double sumChannelRed = 0.0;
        double sumChannelGreen = 0.0;
        double sumChannelBlue= 0.0;

        for (int i = -kernelSize; i <= kernelSize; i++) {
            for (int j = -kernelSize; j <= kernelSize; j++) {
                Color inputColor = pixelReader.getColor(x + i, y + j);
                int moveIndexI = i + 1;
                int moveIndexJ = j + 1;
                double weight = convolutionMat[moveIndexI][moveIndexJ];
                sumChannelRed += weight * inputColor.getRed();
                sumChannelGreen += weight * inputColor.getGreen();
                sumChannelBlue += weight * inputColor.getBlue();
            }
        }

        sumChannelRed /= 36.0;
        sumChannelGreen /= 36.0;
        sumChannelBlue /= 36.0;

        sumChannelRed = Pixel.clampChannelSaturation(sumChannelRed);
        sumChannelGreen = Pixel.clampChannelSaturation(sumChannelGreen);
        sumChannelBlue = Pixel.clampChannelSaturation(sumChannelBlue);

        Color outputColor = new Color(sumChannelRed, sumChannelGreen, sumChannelBlue, 1.0);
        return outputColor;
    }
}
