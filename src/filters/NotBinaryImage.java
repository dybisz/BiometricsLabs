package filters;

/**
 * Created by dybisz on 18/10/2015.
 */
public class NotBinaryImage extends Exception {
    private static final String exceptionMessage = "Input image has not been binarized.";

    NotBinaryImage() {
        super(exceptionMessage);
    }
}
