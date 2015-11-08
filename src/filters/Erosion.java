package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * Created by dybisz on 06/11/2015.
 */
public class Erosion extends MorphologicalOperation {
    public Erosion(Image inputImage) {
        super(inputImage);
    }

    @Override
    protected Color calculateMorphological(PixelReader pixelReader, int x, int y) {
        if (fit(pixelReader, x, y)) {
            return new Color(1, 1, 1, 1);
        } else {
            return new Color(0, 0, 0, 1);
        }
    }

    @Override
    protected Color outOfBorderColor() {
        return new Color(1, 1, 1, 1);
    }
}
