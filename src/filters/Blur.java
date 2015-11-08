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
public class Blur extends ConvolutionFilter {

    public Blur(Image image, int kernelSize) {
        super(image);
        setKernelSize(kernelSize);
    }

    @Override
    protected Color calculateKernel(PixelReader pixelReader, int x, int y) {
        Pixel outputColor = new Pixel(0, 0, 0);
        int count = 0;

        for (int i = -getKernelSize(); i <= getKernelSize(); i++) {
            for (int j = -getKernelSize(); j <= getKernelSize(); j++) {
                if (x + i < 0 || x + i >= getImageWidth()
                        || y + j < 0 || y + j >= getImageHeight()) {
                    continue;
                }
                Color color = pixelReader.getColor(x + i, y + j);
                outputColor.addColorChannelsToPixelChannels(color);
                count++;
            }
        }
        outputColor.divideAllChannelsBy(count);

        return outputColor.toColor();
    }
}


