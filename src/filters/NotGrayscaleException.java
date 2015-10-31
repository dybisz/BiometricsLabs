package filters;

/**
 * Created by dybisz on 18/10/2015.
 */
public class NotGrayscaleException extends Exception {
    private static final String exceptionMessage = "Provided image is not in grayscale.";

    NotGrayscaleException() {
        super(exceptionMessage);
    }
}
