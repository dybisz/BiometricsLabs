package filters;

/**
 * Created by dybisz on 18/10/2015.
 */
public class WrongStructuringElementSize extends Exception {
    private static final String exceptionMessage = "Structuring Element size is either even or is non-symmetrical.";

    WrongStructuringElementSize() {
        super(exceptionMessage);
    }
}
