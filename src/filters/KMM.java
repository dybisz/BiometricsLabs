package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by root on 20/11/15.
 */
public class KMM extends Filter {

    private int[] deletionArray = {3, 5, 7, 12, 13, 14, 15, 20,
            21, 22, 23, 28, 29, 30, 31, 48,
            52, 53, 54, 55, 56, 60, 61, 62,
            63, 65, 67, 69, 71, 77, 79, 80,
            81, 83, 84, 85, 86, 87, 88, 89,
            91, 92, 93, 94, 95, 97, 99, 101,
            103, 109, 111, 112, 113, 115, 116, 117,
            118, 119, 120, 121, 123, 124, 125, 126,
            127, 131, 133, 135, 141, 143, 149, 151,
            157, 159, 181, 183, 189, 191, 192, 193,
            195, 197, 199, 205, 207, 208, 209, 211,
            212, 213, 214, 215, 216, 217, 219, 220,
            221, 222, 223, 224, 225, 227, 229, 231,
            237, 239, 240, 241, 243, 244, 245, 246,
            247, 248, 249, 251, 252, 253, 254, 255};

    public KMM(Image image) {
        super(image);
    }

    // red == 2
    // green = 3
    // blue == 4
    @Override
    public WritableImage apply() throws Exception {
        WritableImage inbetweenImage = findBorderPixels();
        inbetweenImage = findElbowPixels(inbetweenImage);
        inbetweenImage = findPixelsToDelete(inbetweenImage);

        return inbetweenImage;
    }

    private WritableImage findPixelsToDelete(WritableImage inbetweenImage) {
        WritableImage outputImage = new WritableImage((int) getInputImage().getWidth(), (int) getInputImage().getHeight());
        PixelReader pixelReader = getInputImage().getPixelReader();
        PixelWriter pixelWriter = outputImage.getPixelWriter();


        for (int x = 1; x < getImageWidth() - 1; x++) {
            for (int y = 1; y < getImageHeight() - 1; y++) {
                Color inputColor = pixelReader.getColor(x,y);

                if(isWhite(inputColor)) {
                    int markValue = calculateMarkValue(pixelReader, x, y,);
                }


            }
        }


    }


    private WritableImage findBorderPixels() {
        WritableImage outputImage = new WritableImage((int) getInputImage().getWidth(), (int) getInputImage().getHeight());
        PixelReader pixelReader = getInputImage().getPixelReader();
        PixelWriter pixelWriter = outputImage.getPixelWriter();

        for (int x = 1; x < getImageWidth() - 1; x++) {
            for (int y = 1; y < getImageHeight() - 1; y++) {
                Color inputColor = pixelReader.getColor(x, y);

                /* Set 2's */
                if (isWhite(inputColor)) {
                    // change to 2
                    if (touchingViaEdgeBackground(pixelReader, x, y)) {
                        pixelWriter.setColor(x, y, new Color(1.0, 0.0, 0.0, 1.0));
                    }
                    // leave 1
                    else {
                        pixelWriter.setColor(x, y, new Color(1.0, 1.0, 1.0, 1.0));
                    }
                } else {
                    pixelWriter.setColor(x, y, new Color(0.0, 0.0, 0.0, 1.0));
                }


            }

        }
        return outputImage;
    }


    private WritableImage findElbowPixels(WritableImage inbetweenImage) {
        WritableImage outputImage = new WritableImage((int) getInputImage().getWidth(), (int) getInputImage().getHeight());
        PixelReader pixelReader = inbetweenImage.getPixelReader();
        PixelWriter pixelWriter = outputImage.getPixelWriter();

        for (int x = 1; x < getImageWidth() - 1; x++) {
            for (int y = 1; y < getImageHeight() - 1; y++) {
                Color inputColor = pixelReader.getColor(x, y);

                /* Set 3's */
                if (isWhite(inputColor)) {
                    // change to 3
                    if (touchingViaCornersBackground(pixelReader, x, y)) {
                        pixelWriter.setColor(x, y, Color.BLUE);
                    }
                    // leave 1
                    else {
                        pixelWriter.setColor(x, y, new Color(1.0, 1.0, 1.0, 1.0));
                    }
                } else {
                    pixelWriter.setColor(x, y, inputColor);
                }


            }

        }
        return outputImage;
    }

    private boolean touchingViaCornersBackground(PixelReader pixelReader, int x, int y) {
        boolean up = pixelReader.getColor(x - 1, y - 1).getRed() == 0.0;
        boolean right = pixelReader.getColor(x + 1, y - 1).getRed() == 0.0;
        boolean left = pixelReader.getColor(x - 1, y + 1).getRed() == 0.0;
        boolean down = pixelReader.getColor(x + 1, y + 1).getRed() == 0.0;
        return up || right || left || down;
    }

    private boolean touchingViaEdgeBackground(PixelReader pixelReader, int x, int y) {
        boolean up = pixelReader.getColor(x, y - 1).getRed() == 0.0;
        boolean right = pixelReader.getColor(x + 1, y).getRed() == 0.0;
        boolean left = pixelReader.getColor(x - 1, y).getRed() == 0.0;
        boolean down = pixelReader.getColor(x, y + 1).getRed() == 0.0;
        return up || right || left || down;
    }

    public boolean isWhite(Color inputColor) {
        return (inputColor.getRed() == 1) && (inputColor.getGreen() == 1) && (inputColor.getBlue() == 1);
    }

    public boolean isBlack(Color inputColor) {
        return (inputColor.getRed() == 0) && (inputColor.getGreen() == 0) && (inputColor.getBlue() == 0);
    }

    public boolean isRed(Color inputColor) {
        return (inputColor.getRed() == 1) && (inputColor.getGreen() == 0) && (inputColor.getBlue() == 0);
    }

    public boolean isBlue(Color inputColor) {
        return (inputColor.getRed() == 0) && (inputColor.getGreen() == 0) && (inputColor.getBlue() == 1);
    }

    public boolean isGreen(Color inputColor) {
        return (inputColor.getRed() == 0) && (inputColor.getGreen() == 1) && (inputColor.getBlue() == 0);
    }
}
