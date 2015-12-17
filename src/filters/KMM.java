package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by dybisz on 14.12.15.
 */
public class KMM extends Filter {
    private static final int OBJECT = 1;
    private static final int BORDER = 2;
    private static final int ELBOW = 3;
    private static final int DELETE = 4;
    private static final int BACKGROUND = 0;

    private static final int[][] weights = new int[][]{
            {128, 1, 2},
            {64, 0, 4},
            {32, 16, 8}
    };

    private static final int[] deletionArray = {3, 5, 7, 12, 13, 14, 15, 20,
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

    @Override
    public WritableImage apply() throws Exception {
        int n = 25;

        // Step 1
        int mask[][] = loadMask(getInputImage());
        while (n-- != 0) {
            // Step 2
            int twos[][] = mark2s(mask);
            // Step 3
            int threes[][] = mark3s(twos);
            // Step 4
            int fours[][] = mark4s(threes, mask);
            // Step 5
            int deleted4s[][] = delete4s(fours, mask);
            // Step 6
            int deleted2s[][] = delete2s(deleted4s, mask);
            // Step 7
            int deleted3s[][] = delete3s(deleted2s, mask);
        }

        return convertMaskToImg(mask);
    }

    private void copy(int[][] source, int[][] destination) {
        for (int h = 0; h < imgHeight() ; h++) {
            for (int w = 0; w < imgWidth(); w++) {
                int sourceVal = source[h][w];
                destination[h][w] = sourceVal;
            }
        }
    }

    private int[][] delete3s(int[][] input, int[][] mask) {
        int output[][] = new int[imgHeight()][imgWidth()];

        for (int h = 1; h < imgHeight() - 1; h++) {
            for (int w = 1; w < imgWidth() - 1; w++) {
                int inputVal = input[h][w];
                if(inputVal == ELBOW) {
                    int weight = getWeight(h,w,mask);
                    if (weightOnList(weight)) {
                        output[h][w] = BACKGROUND;
                        mask[h][w] = BACKGROUND;
                    } else output[h][w] = OBJECT;
                }else output[h][w] = inputVal;
            }
        }
        return output;
    }

    private int[][] delete2s(int[][] input, int[][] mask) {
        int output[][] = new int[imgHeight()][imgWidth()];

        for (int h = 1; h < imgHeight() - 1; h++) {
            for (int w = 1; w < imgWidth() - 1; w++) {
                int inputVal = input[h][w];
                if(inputVal == BORDER) {
                    int weight = getWeight(h,w,mask);
                    if (weightOnList(weight)) {
                        output[h][w] = BACKGROUND;
                        mask[h][w] = BACKGROUND;
                    } else output[h][w] = OBJECT;

                }else output[h][w] = inputVal;

            }
        }
        return output;
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

    private int[][] delete4s(int[][] input, int[][] mask) {
        int output[][] = new int[imgHeight()][imgWidth()];

        for (int h = 1; h < imgHeight() - 1; h++) {
            for (int w = 1; w < imgWidth() - 1; w++) {
                int inputColor = input[h][w];
                if (inputColor == DELETE) {
                    output[h][w] = BACKGROUND;
                    mask[h][w] = BACKGROUND;
                } else output[h][w] = inputColor;
            }
        }

        return output;
    }

    private int[][] mark4s(int[][] input, int[][] mask) {
        int output[][] = new int[imgHeight()][imgWidth()];

        for (int h = 1; h < imgHeight() - 1; h++) {
            for (int w = 1; w < imgWidth() - 1; w++) {
                int inputColor = input[h][w];
                int weight = getWeight(h, w, mask);

                if (inputColor == BORDER || inputColor == ELBOW) {
                    if (stickyNeighbors234(h, w, mask)) {
                        if (weightOnList(weight)) {
                            output[h][w] = DELETE;
                        } else output[h][w] = inputColor;
                    } else output[h][w] = inputColor;
                } else output[h][w] = inputColor;

            }
        }
        return output;
    }

    private boolean stickyNeighbors234(int h, int w, int[][] mask) {
        int sum = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                sum += mask[h + i][w + j];
            }
        }
        return (sum == 2) || (sum == 3) || (sum == 4);
    }

    private boolean weightOnList(int weight) {
        for (int i = 0; i < deletionArray.length; i++) {
            if (weight == deletionArray[i]) return true;
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

    private int[][] mark3s(int[][] input) {
        int output[][] = new int[imgHeight()][imgWidth()];

        for (int h = 1; h < imgHeight(); h++) {
            for (int w = 1; w < imgWidth(); w++) {
                int inputColor = input[h][w];

                // 3's must touch background only via corners
                // hence we do not consider 2's
                if (inputColor == OBJECT) {
                    if (neighborsByCorner(h, w, input)) {
                        output[h][w] = ELBOW;
                    } else output[h][w] = inputColor;
                } else output[h][w] = inputColor;

            }
        }

        return output;
    }

    /**
     * True if at least one corner neighbor is background pixel.
     *
     * @param h
     * @param w
     * @param input
     * @return
     */
    private boolean neighborsByCorner(int h, int w, int[][] input) {
        boolean upleft = input[h - 1][w - 1] == 0;
        boolean upright = input[h + 1][w - 1] == 0;
        boolean downleft = input[h - 1][w + 1] == 0;
        boolean downright = input[h + 1][w + 1] == 0;
        return upleft || upright || downleft || downright;
    }

    /**
     * Object pixels, which touch background only via edges
     * are marked as 2.
     *
     * @param input
     * @return
     */
    private int[][] mark2s(int[][] input) {
        int output[][] = new int[imgHeight()][imgWidth()];

        for (int h = 1; h < imgHeight()- 1; h++) {
            for (int w = 1; w < imgWidth() -1; w++) {
                int inputColor = input[h][w];
                if (inputColor == OBJECT) {
                    if (neighborsByEdge(h, w, input)) {
                        output[h][w] = BORDER;
                    } else output[h][w] = inputColor;
                } else output[h][w] = inputColor;

            }
        }

        return output;
    }

    /**
     * True if at least one edge neighbor is background.
     *
     * @param h
     * @param w
     * @param input
     * @return
     */
    boolean neighborsByEdge(int h, int w, int input[][]) {
        boolean up = (input[h][w - 1] == 0);
        boolean right = (input[h + 1][w] == 0);
        boolean left = (input[h - 1][w] == 0);
        boolean down = (input[h][w + 1] == 0);
        return up || right || left || down;
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

    /**
     * Code input image as a mask of integers of values 0 or 1.
     * x's and y's are switched, because natively in JavaFX pixels are
     * stored as [width_coord][height_coord], which causes img
     * to be rotated.
     *
     * @param inputImage
     * @return
     */
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

}
