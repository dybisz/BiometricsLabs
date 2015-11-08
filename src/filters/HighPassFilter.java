package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import utils.Pixel;

/**
 * Class provides {@link ConvolutionFilter} with appropriate values to
 * perform High Pass filter calculations.
 * <p></p>
 * Created by dybisz on 30/10/2015.
 */
public class HighPassFilter extends ConvolutionFilter {
    private double[][] convolutionMat = {
            {-1, -1, -1},
            {-1, 9, -1},
            {-1, -1, -1}
    };
    private int kernelSize = 1;
    private double divisor = 9;
    private int omitFrame = 1;

    public HighPassFilter(Image inputImage) {
        super(inputImage);
        setConvolutionMatrix(convolutionMat);
        setKernelSize(kernelSize);
        setDivisor(divisor);
        setOmitFrame(omitFrame);
    }
}