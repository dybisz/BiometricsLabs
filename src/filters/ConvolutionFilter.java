package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import utils.Pixel;

/**
 * Class assumes that kernel matrix will be a squared matrix.
 * <p>
 * Created by dybisz on 31/10/2015.
 */
public abstract class ConvolutionFilter extends Filter {
    private double[][] convolutionMatrix;
    private int kernelSize;
    // Size of frame consisting of pixels to omit during calculations
    // Important in considering 'border cases'
    private int omitFrame;
    private double divisor;

    ConvolutionFilter(Image inputImage) {
        super(inputImage);
    }

    @Override
    public WritableImage apply() {
        PixelReader pixelReader = getInputImage().getPixelReader();
        PixelWriter pixelWriter = getOutputImage().getPixelWriter();

        for (int x = omitFrame; x < getImageWidth() - omitFrame; x++) {
            for (int y = omitFrame; y < getImageHeight() - omitFrame; y++) {
                Color outputColor = calculateKernel(pixelReader, x, y);
                pixelWriter.setColor(x, y, outputColor);
            }
        }

        return getOutputImage();
    }

    protected Color calculateKernel(PixelReader pixelReader, int x, int y) {
        // Collect sum from channels in one data structure
        Pixel sumOnChannels = new Pixel(0, 0, 0);

        for (int i = -kernelSize; i <= kernelSize; i++) {
            for (int j = -kernelSize; j <= kernelSize; j++) {
                Color inputColor = pixelReader.getColor(x + i, y + j);
                double weight = getWeightFromConvolutionMat(i, j);

                sumOnChannels.addValuesToChannels(
                        weight * inputColor.getRed(),
                        weight * inputColor.getGreen(),
                        weight * inputColor.getBlue());
            }
        }
        sumOnChannels.divideAllChannelsBy(divisor);
        sumOnChannels.clampAllChannels();

        Color outputColor = sumOnChannels.toColor();
        return outputColor;
    }

    private double getWeightFromConvolutionMat(int i, int j) {
        int translateI = i + kernelSize;
        int translateJ = j + kernelSize;
        return convolutionMatrix[translateI][translateJ];
    }

    public void setConvolutionMatrix(double[][] convolutionMatrix) {
        this.convolutionMatrix = convolutionMatrix;
    }

    public void setKernelSize(int kernelSize) {
        this.kernelSize = kernelSize;
    }

    public void setDivisor(double divisor) {
        this.divisor = divisor;
    }

    public int getKernelSize() {
        return kernelSize;
    }

    public void setOmitFrame(int omitFrame) {
        this.omitFrame = omitFrame;
    }
}
