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
public class SobelFilter extends ConvolutionFilter {
    public SobelFilter(Image inputImage) {
        super(inputImage);
        setOmitFrame(1);
    }

    @Override
    protected Color calculateKernel(PixelReader pixelReader, int x, int y) {
        Pixel[] neighbourhood = gatherNeighbourhoodOf(x, y, pixelReader);
        Pixel xGradient = calculateXGradient(neighbourhood);
        Pixel yGradient = calculateYGradient(neighbourhood);
        Color outputColor = calculateOutputColor(xGradient, yGradient);

        return outputColor;
    }

    private Pixel[] gatherNeighbourhoodOf(int x, int y, PixelReader pixelReader) {
        Pixel[] neighbourhood = new Pixel[8];

        /* P0*/
        neighbourhood[0] = extractPixelFromNeighbourhood(pixelReader, x - 1, y - 1);
        /* P1 */
        neighbourhood[1] = extractPixelFromNeighbourhood(pixelReader, x, y - 1);
        /* P2 */
        neighbourhood[2] = extractPixelFromNeighbourhood(pixelReader, x + 1, y - 1);
        /* P3 */
        neighbourhood[3] = extractPixelFromNeighbourhood(pixelReader, x + 1, y);
        /* P4 */
        neighbourhood[4] = extractPixelFromNeighbourhood(pixelReader, x + 1, y + 1);
        /* P5 */
        neighbourhood[5] = extractPixelFromNeighbourhood(pixelReader, x, y + 1);
        /* P6 */
        neighbourhood[6] = extractPixelFromNeighbourhood(pixelReader, x - 1, y + 1);
        /* P7 */
        neighbourhood[7] = extractPixelFromNeighbourhood(pixelReader, x - 1, y);

        return neighbourhood;
    }

    private Pixel extractPixelFromNeighbourhood(PixelReader pixelReader, int x, int y) {
        Color color = pixelReader.getColor(x, y);
        Pixel pixel = new Pixel(color);
        return pixel;
    }

    private Pixel calculateXGradient(Pixel[] neighbourhood) {
        double x_red = (neighbourhood[2].getR() + 2 * neighbourhood[3].getR()
                + neighbourhood[4].getR()) - (neighbourhood[0].getR()
                + 2 * neighbourhood[7].getR() + neighbourhood[6].getR());
        double x_green = (neighbourhood[2].getG() + 2 * neighbourhood[3].getG()
                + neighbourhood[4].getG()) - (neighbourhood[0].getG()
                + 2 * neighbourhood[7].getG() + neighbourhood[6].getG());
        double x_blue = (neighbourhood[2].getB() + 2 * neighbourhood[3].getB()
                + neighbourhood[4].getB()) - (neighbourhood[0].getB()
                + 2 * neighbourhood[7].getB() + neighbourhood[6].getB());

        return new Pixel(x_red, x_green, x_blue);
    }

    private Pixel calculateYGradient(Pixel[] neighbourhood) {
        double y_red = (neighbourhood[6].getR() + 2 * neighbourhood[5].getR()
                + neighbourhood[4].getR()) - (neighbourhood[0].getR()
                + 2 * neighbourhood[1].getR() + neighbourhood[2].getR());
        double y_green = (neighbourhood[6].getG() + 2 * neighbourhood[5].getG()
                + neighbourhood[4].getG()) - (neighbourhood[0].getG()
                + 2 * neighbourhood[1].getG() + neighbourhood[2].getG());
        double y_blue = (neighbourhood[6].getB() + 2 * neighbourhood[5].getB()
                + neighbourhood[4].getB()) - (neighbourhood[0].getB()
                + 2 * neighbourhood[1].getB() + neighbourhood[2].getB());

        return new Pixel(y_red, y_green, y_blue);
    }

    private Color calculateOutputColor(Pixel xGradient, Pixel yGradient) {
        double px_red = euclidianDistanceXY(xGradient.getR(), yGradient.getR());
        double px_green = euclidianDistanceXY(xGradient.getG(), yGradient.getG());
        double px_blue = euclidianDistanceXY(xGradient.getB(), yGradient.getB());

        Pixel outputPixel = new Pixel(px_red, px_green, px_blue);
        outputPixel.clampAllChannels();
        Color outputColor = outputPixel.toColor();

        return outputColor;
    }

    private double euclidianDistanceXY(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
}
