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
public class SobelFilter extends Filter {
    public SobelFilter(Image inputImage) {
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
        Color color;

        /* P0*/
        color = pixelReader.getColor(x - 1, y - 1);
        Pixel p0 = new Pixel(color.getRed(), color.getGreen(), color.getBlue());

        /* P1 */
        color = pixelReader.getColor(x, y - 1);
        Pixel p1 = new Pixel(color.getRed(), color.getGreen(), color.getBlue());

        /* P2 */
        color = pixelReader.getColor(x + 1, y - 1);
        Pixel p2 = new Pixel(color.getRed(), color.getGreen(), color.getBlue());

        /* P3 */
        color = pixelReader.getColor(x + 1, y);
        Pixel p3 = new Pixel(color.getRed(), color.getGreen(), color.getBlue());

        /* P4 */
        color = pixelReader.getColor(x + 1, y + 1);
        Pixel p4 = new Pixel(color.getRed(), color.getGreen(), color.getBlue());

        /* P5 */
        color = pixelReader.getColor(x, y + 1);
        Pixel p5 = new Pixel(color.getRed(), color.getGreen(), color.getBlue());

        /* P6 */
        color = pixelReader.getColor(x - 1, y + 1);
        Pixel p6 = new Pixel(color.getRed(), color.getGreen(), color.getBlue());

        /* P7 */
        color = pixelReader.getColor(x - 1, y);
        Pixel p7 = new Pixel(color.getRed(), color.getGreen(), color.getBlue());


        double x_red = (p2.getR() + 2 * p3.getR() + p4.getR()) - (p0.getR() + 2 * p7.getR() + p6.getR());
        double x_green = (p2.getG() + 2 * p3.getG() + p4.getG()) - (p0.getG() + 2 * p7.getG() + p6.getG());
        double x_blue = (p2.getB() + 2 * p3.getB() + p4.getB()) - (p0.getB() + 2 * p7.getB() + p6.getB());

        double y_red = (p6.getR() + 2 * p5.getR() + p4.getR()) - (p0.getR() + 2 * p1.getR() + p2.getR());
        double y_green= (p6.getG() + 2 * p5.getG() + p4.getG()) - (p0.getG() + 2 * p1.getG() + p2.getG());
        double y_blue = (p6.getB() + 2 * p5.getB() + p4.getB()) - (p0.getB() + 2 * p1.getB() + p2.getB());

        double px_red = Math.sqrt(x_red * x_red + y_red * y_red);
        double px_green= Math.sqrt(x_green * x_green + y_green * y_green);
        double px_blue = Math.sqrt(x_blue * x_blue + y_blue * y_blue);

        px_red = Pixel.clampChannelSaturation(px_red);
        px_green = Pixel.clampChannelSaturation(px_green);
        px_blue = Pixel.clampChannelSaturation(px_blue);

        Color outputColor = new Color(px_red, px_green, px_blue, 1.0);

        return outputColor;
    }
}
