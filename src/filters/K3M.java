package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by dybisz on 14.12.15.
 */
public class K3M extends Filter {
    private static final int OBJECT = 1;
    private static final int BORDER = 2;
    private static final int BACKGROUND = 0;
    static int[][] weights = new int[][]{
            {128, 1, 2},
            {64, 0, 4},
            {32, 16, 8}
    };
    private static final int A_0[] = {3, 6, 7, 12, 14, 15, 24, 28, 30, 31, 48, 56, 60,
            62, 63, 96, 112, 120, 124, 126, 127, 129, 131, 135, 143, 159, 191, 192,
            193, 195, 199, 207, 223, 224,
            225, 227, 231, 239, 240, 241, 243, 247, 248, 249,
            251, 252, 253, 254};
    private static final int A_1[] = {7, 14, 28, 56, 112, 131, 193, 224};
    private static final int A_2[] = {7, 14, 15, 28, 30, 56, 60, 112, 120, 131, 135,
            193, 195, 224, 225, 240};
    private static final int A_3[] = {7, 14, 15, 28, 30, 31, 56, 60, 62, 112, 120,
            124, 131, 135, 143, 193, 195, 199, 224, 225, 227,
            240, 241, 248};
    private static final int A_4[] = {7, 14, 15, 28, 30, 31, 56, 60, 62, 63, 112, 120,
            124, 126, 131, 135, 143, 159, 193, 195, 199, 207,
            224, 225, 227, 231, 240, 241, 243, 248, 249, 252};

    private static final int A_5[] = {7, 14, 15, 28, 30, 31, 56, 60, 62, 63, 112, 120,
            124, 126, 131, 135, 143, 159, 191, 193, 195, 199,
            207, 224, 225, 227, 231, 239, 240, 241, 243, 248,
            249, 251, 252, 254};
    private static final int A_1pix[] = {3, 6, 7, 12, 14, 15, 24, 28, 30, 31, 48, 56,
            60, 62, 63, 96, 112, 120, 124, 126, 127, 129, 131,
            135, 143, 159, 191, 192, 193, 195, 199, 207, 223,
            224, 225, 227, 231, 239, 240, 241, 243, 247, 248,
            249, 251, 252, 253, 254};

    public K3M(Image image) {
        super(image);
    }


    @Override
    public WritableImage apply() throws Exception {
        int mask[][] = loadMask(getInputImage());
        int maskPrev[][] = new int[imgHeight()][imgWidth()];

        do {
            // SAVE PREVIOUS MASK
            copy(mask, maskPrev);
            // PHASE 0 - MARK BORDERS
            int inter[][] = markBorders(mask);
            // PHASE 1
            applyPhase(A_1, inter, mask);
            // PHASE 2
            applyPhase(A_2, inter, mask);
            // PHASE 3
            applyPhase(A_3, inter, mask);
            // PHASE 4
            applyPhase(A_4, inter, mask);
            // PHASE 5
            applyPhase(A_5, inter, mask);
            // PHASE 6 - UNMARK BORDERS
            mask = unmarkBorders(inter);
        } while (different(mask, maskPrev));

        // SKELETON
        applyPhase(A_1pix, mask, mask);

        return convertMaskToImg(mask);
    }

    private int[][] unmarkBorders(int[][] input) {
        int output[][] = new int[imgHeight()][imgWidth()];

        for (int h = 1; h < imgHeight() - 1; h++) {
            for (int w = 1; w < imgWidth() - 1; w++) {
                int inputVal = input[h][w];

                if (inputVal == BORDER) {
                    output[h][w] = OBJECT;
                } else output[h][w] = inputVal;

            }
        }
        return output;
    }

    private void applyPhase(int[] list, int[][] source, int[][] mask) {
        for (int h = 1; h < imgHeight() - 1; h++) {
            for (int w = 1; w < imgWidth() - 1; w++) {
                int inputVal = source[h][w];

                if (inputVal == BORDER) {
                    int weight = getWeight(h, w, mask);

                    if (weightOnList(weight, list)) {
                        source[h][w] = BACKGROUND;
                    }

                }

            }
        }
    }

    private int[][] markBorders(int[][] mask) {
        int output[][] = new int[imgHeight()][imgWidth()];

        for (int h = 1; h < imgHeight() - 1; h++) {
            for (int w = 1; w < imgWidth() - 1; w++) {
                int inputVal = mask[h][w];
                if (inputVal == OBJECT && isBorder(h, w, mask)) {
                    output[h][w] = BORDER;
                } else output[h][w] = inputVal;
            }
        }
        return output;
    }

    private boolean isBorder(int h, int w, int[][] mask) {
        int sum = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                sum += mask[h + i][w + j];
            }
        }
        return ((sum - mask[h][w]) <= 7);
    }

    private boolean different(int[][] mask, int[][] maskPrev) {

        for (int h = 0; h < imgHeight(); h++) {
            for (int w = 0; w < imgWidth(); w++) {
                if (mask[h][w] != maskPrev[h][w]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean weightOnList(int weight, int list[]) {
        for (int i = 0; i < list.length; i++) {
            if (weight == list[i]) return true;
        }
        return false;
    }

    /**
     * Based on masked image, it calculates weight of a given pixel.
     *
     * @param h
     * @param w
     * @param mask
     * @return
     */
    private int getWeight(int h, int w, int[][] mask) {
        int sum = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int mask_val = mask[h + i][w + j];
                int ngbh_val = weights[i + 1][j + 1];
                sum += mask_val * ngbh_val;
            }
        }
        return sum;
    }

    private int[][] loadMask(Image inputImage) {
        PixelReader inputReader = inputImage.getPixelReader();
        int output[][] = new int[imgHeight()][imgWidth()];

        for (int x = 0; x < imgWidth(); x++) {
            for (int y = 0; y < imgHeight(); y++) {
                Color inputColor = inputReader.getColor(x, y);
                output[y][x] = (inputColor.equals(Color.WHITE)) ? BACKGROUND : OBJECT;
            }
        }

        return output;
    }

    private int imgWidth() {
        return (int) getImageWidth();
    }

    private int imgHeight() {
        return (int) getImageHeight();
    }

    private void copy(int[][] source, int[][] destination) {
        for (int h = 0; h < imgHeight(); h++) {
            for (int w = 0; w < imgWidth(); w++) {
                int sourceVal = source[h][w];
                destination[h][w] = sourceVal;
            }
        }
    }

    private void printArray(int[][] mask) {
        for (int x = 0; x < imgHeight(); x++) {
            for (int y = 0; y < imgWidth(); y++) {
                System.out.print(mask[x][y] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private WritableImage convertMaskToImg(int[][] mask) {
        WritableImage outputImage = new WritableImage(imgWidth(), imgHeight());
        PixelWriter pixelWriter = outputImage.getPixelWriter();

        for (int x = 0; x < imgWidth(); x++) {
            for (int y = 0; y < imgHeight(); y++) {
                int inputVal = mask[y][x];
                Color outputColor = (inputVal == 1) ? Color.WHITE : Color.BLACK;
                pixelWriter.setColor(x, y, outputColor);
            }
        }

        return outputImage;
    }

}
