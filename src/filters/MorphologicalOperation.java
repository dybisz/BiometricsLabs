package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by dybisz on 06/11/2015.
 */
public abstract class MorphologicalOperation extends Filter {

    // Default one is most common one
    private double[][] structuringElement = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };

    MorphologicalOperation(Image inputImage) {
        super(inputImage);
    }

    MorphologicalOperation() {
    }

    ;

    @Override
    public WritableImage apply() throws Exception {
        checkStructuringElementAssumptions();
        PixelReader pixelReader = getInputImage().getPixelReader();
        PixelWriter pixelWriter = getOutputImage().getPixelWriter();

        for (int x = 0; x < getImageWidth(); x++) {
            for (int y = 0; y < getImageHeight(); y++) {
                Color outputColor = calculateMorphological(pixelReader, x, y);
                pixelWriter.setColor(x, y, outputColor);
            }
        }

        return getOutputImage();
    }

    protected abstract Color calculateMorphological(PixelReader pixelReader, int x, int y) throws Exception;

    protected boolean fit(PixelReader pixelReader, int x, int y) {
        int halfStructElementSize = (structuringElement.length / 2);

        for (int i = -halfStructElementSize; i <= halfStructElementSize; i++) {
            for (int j = -halfStructElementSize; j <= halfStructElementSize; j++) {
                Color inputColor = checkInputColor(pixelReader, x + i, y + i);

                double structuringElementValue =
                        structuringElement[i + halfStructElementSize][j + halfStructElementSize];

                if (inputColor.getRed() != structuringElementValue && structuringElementValue == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean hit(PixelReader pixelReader, int x, int y) {
        int halfStructElementSize = (structuringElement.length / 2);

        for (int i = -halfStructElementSize; i <= halfStructElementSize; i++) {
            for (int j = -halfStructElementSize; j <= halfStructElementSize; j++) {
                Color inputColor = checkInputColor(pixelReader, (x + i), (y + i));

                double structuringElementValue =
                        structuringElement[i + halfStructElementSize][j + halfStructElementSize];
                if (inputColor.getRed() == structuringElementValue && inputColor.getRed() == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Color checkInputColor(PixelReader pixelReader, int x, int y) {
        if (outOfBorder(x, y)) {
            return outOfBorderColor();
        } else {
            return pixelReader.getColor(x, y);
        }
    }

    protected boolean outOfBorder(int x, int y) {
        return x < 0 || x >= getImageWidth()
                || y < 0 || y >= getImageHeight();
    }

    // Each morphological operation has its own policy regarding 'out of border cases'.
    protected abstract Color outOfBorderColor();

    protected void checkStructuringElementAssumptions() throws Exception {
        checkForNull(structuringElement);
        checkForSizes(structuringElement);
    }

    protected void checkForSizes(double[][] structuringElement) throws Exception {
        if (structuringElement.length % 2 != 1) {
            throw new WrongStructuringElementSize();
        }
        if (structuringElement[0].length % 2 != 1) {
            throw new WrongStructuringElementSize();
        }
        if (structuringElement.length != structuringElement[0].length) {
            throw new WrongStructuringElementSize();
        }
    }

    public void setStructuringElement(double[][] structuringElement) {
        this.structuringElement = structuringElement;
    }
}
