package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import utils.SaveImageToFile;

/**
 * Created by dybisz on 06/11/2015.
 */
public class ConditionalDilation extends MorphologicalOperation {
    public ConditionalDilation(Image inputImage) {
        super(inputImage);
    }

    @Override
    public WritableImage apply() throws Exception {
        checkStructuringElementAssumptions();
        WritableImage negativeImage = negateImage(getInputImage());
        WritableImage filledRegion = createSeededImage(2, 2);

        for (int i = 0; ; i++) {
            WritableImage freshFilledRegion = applyDilation(filledRegion);
            freshFilledRegion = intersection(freshFilledRegion, negativeImage);

            if (areEqual(freshFilledRegion, filledRegion)) {
                break;
            } else {
                filledRegion = freshFilledRegion;
            }
        }
        return filledRegion;
    }

    private boolean areEqual(WritableImage img1, WritableImage img2) {
        int width = (int) img1.getWidth();
        int height = (int) img2.getHeight();
        PixelReader pixelReader1 = img1.getPixelReader();
        PixelReader pixelReader2 = img2.getPixelReader();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color1 = pixelReader1.getColor(x, y);
                Color color2 = pixelReader2.getColor(x, y);

                if(color1.getRed() != color2.getRed()) {
                    return false;
                }
            }
        }

        return true;
    }

    private WritableImage intersection(WritableImage img1, WritableImage img2) {
        int width = (int) img1.getWidth();
        int height = (int) img2.getHeight();
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader pixelReader1 = img1.getPixelReader();
        PixelReader pixelReader2 = img2.getPixelReader();
        PixelWriter pixelWriter = outputImage.getPixelWriter();


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color1 = pixelReader1.getColor(x, y);
                Color color2 = pixelReader2.getColor(x, y);

                if(color1.getRed() == color2.getRed() && color1.getRed() == 1) {
                    pixelWriter.setColor(x,y, new Color(1,1,1,1));
                }
                else {
                    pixelWriter.setColor(x,y, new Color(0,0,0,1));
                }

            }
        }

        return outputImage;
    }

    private WritableImage applyDilation(WritableImage inputImage) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader inputReader = inputImage.getPixelReader();
        PixelWriter outputWriter = outputImage.getPixelWriter();

        double[][] structElement = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 1, 0}
        };

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // FOR EACH PIXEL
                Color color = inputReader.getColor(x, y);
                Color outputColor = new Color(0, 0, 0, 1);
                // CHECK NEIGHBOURS
                for (int n_x = -1; n_x <= 1; n_x++) {
                    for (int n_y = -1; n_y <= 1; n_y++) {
                        Color neighbourColorOnImg;
                        // IF BORDER PIXEL => CONSIDER BLACK
                        int n_x_on_img = x + n_x;
                        int n_y_on_img = y + n_y;
                        if (n_x_on_img < 0 || n_x_on_img >= width || n_y_on_img < 0 || n_y_on_img >= height) {
//                            System.out.println("out of border condition met");
                            neighbourColorOnImg = new Color(0, 0, 0, 1);
                        } else {
                            neighbourColorOnImg = inputReader.getColor(n_x_on_img, n_y_on_img);
                        }

                        if (neighbourColorOnImg.getRed() == structElement[n_x + 1][n_y + 1] && neighbourColorOnImg.getRed() == 1) {
                            outputColor = new Color(1, 1, 1, 1);
                        }

                    }
                }

                outputWriter.setColor(x, y, outputColor);


            }
        }
        return outputImage;
    }

    private WritableImage createSeededImage(int seedX, int seedY) {
        int width = (int) getImageWidth();
        int height = (int) getImageHeight();
        WritableImage outputImage = new WritableImage(width, height);
        PixelWriter outputWriter = outputImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color outputColor;
                if (x == seedX && y == seedY) {
                    outputColor = new Color(1, 1, 1, 1);

                } else {
                    outputColor = new Color(0, 0, 0, 1);
                }
                outputWriter.setColor(x, y, outputColor);
            }
        }

        return outputImage;
    }

    @Override
    protected Color calculateMorphological(PixelReader pixelReader, int x, int y) throws Exception {
        return null;
    }

    private WritableImage negateImage(Image inputImage) throws Exception {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(width, height);

        PixelReader inputReader = inputImage.getPixelReader();
        PixelWriter outputWriter = outputImage.getPixelWriter();


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color inputColor = inputReader.getColor(x, y);

                if (notBinaryColor(inputColor)) {
                    throw new NotBinaryImage();
                }

                double outputValue = (inputColor.getRed() == 1) ? 0 : 1;
                Color outputColor = new Color(outputValue, outputValue, outputValue, 1.0);
                outputWriter.setColor(x, y, outputColor);
            }
        }


        return outputImage;
    }

    private boolean notBinaryColor(Color color) {
        return !(color.getRed() == 1 || color.getRed() == 0)
                && (color.getRed() == color.getBlue()
                && color.getBlue() == color.getGreen()
                && color.getRed() == color.getGreen());
    }

    @Override
    protected Color outOfBorderColor() {
        return new Color(0, 0, 0, 1);
    }
}
