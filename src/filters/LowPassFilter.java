package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import utils.Pixel;

/**
 * Class provides {@link ConvolutionFilter} with appropriate values to
 * perform Low Pass filter calculations.
 * <p></p>
 * Created by dybisz on 30/10/2015.
 */
public class LowPassFilter extends ConvolutionFilter {
    private double[][] convolutionMat = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };
    private int kernelSize = 1;
    private double divisor = 9;
    private int omitFrame = 1;

    public LowPassFilter(Image inputImage) {
        super(inputImage);
        setConvolutionMatrix(convolutionMat);
        setKernelSize(kernelSize);
        setDivisor(divisor);
        setOmitFrame(omitFrame);
    }
}
