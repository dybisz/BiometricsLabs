package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import utils.Pixel;

/**
 * Class provides {@link ConvolutionFilter} with appropriate values to
 * perform Gaussian filter calculations.
 * <p></p>
 * Created by dybisz on 30/10/2015.
 */
public class GaussianFilter extends ConvolutionFilter {
    private double[][] convolutionMat = {
            {1, 4, 1},
            {4, 16, 4},
            {1, 4, 1}
    };
    private int kernelSize = 1;
    private double divisor = 36.0;
    private int omitFrame = 1;

    public GaussianFilter(Image inputImage) {
        super(inputImage);
        setConvolutionMatrix(convolutionMat);
        setKernelSize(kernelSize);
        setDivisor(divisor);
        setOmitFrame(omitFrame);
    }
}
