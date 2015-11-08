package filters;

/**
 * Created by dybisz on 18/10/2015.
 */
public class WrongKernelSize extends Exception {
    private static final String exceptionMessage = "Kernel size < 1";

    WrongKernelSize() {
        super(exceptionMessage);
    }
}
