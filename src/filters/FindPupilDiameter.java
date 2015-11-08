package filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by dybisz on 07/11/2015.
 */
public class FindPupilDiameter extends Filter {
    private final Image originalImage;

    public FindPupilDiameter(Image inputImage, Image originalImage) {
        super(inputImage);
        this.originalImage = originalImage;
    }

    @Override
    public WritableImage apply() throws Exception {
        
        // FOR EACH ROW SHOOT RAY
            // CALCULATE RAY's LENGTH
            // IF LONGER THAN ACTUAL LONGEST - SAVE IT

        int width = (int) getImageWidth();
        int height = (int) getImageHeight();
        WritableImage outputImage = new WritableImage(width, height);

        PixelReader pixelReader = getInputImage().getPixelReader();
        PixelWriter pixelWriter = outputImage.getPixelWriter();
        PixelReader originPixelReader = originalImage.getPixelReader();

        int diameterY = 0;
        int diameterStartX = 0;
        int diameterEndX = 0;
        int diameterLength = 0;

        for(int y = 0; y < height; y++) {
            int lengthCounter = 0;
            boolean counting = false;
            int leftX = 0;
            int rightX = 0;

            for(int x = 0; x < width; x++) {
                Color inputColor = pixelReader.getColor(x,y);

                // IF == 1 && NOT COUNTING => COUNTING == TRUE && ACTUAL_MEASURE++

                if(inputColor.getRed() == 0 && counting) {
                    lengthCounter++;
                }

                if(inputColor.getRed() == 0 && !counting) {
                    counting = true;
                    leftX = x;
                    lengthCounter++;
                }
                // IF == 0 AND COUNTING ==> STOP COUNTING
                if(inputColor.getRed() == 1 && counting) {
                    rightX = x;
                    if(lengthCounter > diameterLength) {

                        diameterLength = lengthCounter;
                        diameterStartX = leftX;
                        diameterEndX = rightX;
                        diameterY = y;

                        System.out.println("diameterLength: " + diameterLength);
                    }
                    lengthCounter = 0;
                    counting = false;
                }

            }
        }

        System.out.println("x:[" + diameterStartX + "][" + diameterEndX +  "] y = " + diameterY);
        // COPY IMAGE
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Color inputColor = originPixelReader.getColor(x,y);
                pixelWriter.setColor(x,y,inputColor);
            }
        }

        // MARK DIAMETER
        for(int x = diameterStartX; x <= diameterEndX; x++) {
            if(x == diameterStartX + diameterLength/2) {
                pixelWriter.setColor(x,diameterY,new Color(1,1,1,1));
            }else {
                pixelWriter.setColor(x,diameterY,new Color(1,0,0,1));
            }

        }




        // DRAW LONGEST ONE ON ORIGINAL IMAGE
        // DRAW BOUNDARIES FOR LONGEST ROW ON ORIGINAL IMAGE

        // FOR EACH COLUMN SHOOT RAY
            // CALCULATE RAY's LENGTH
            // IF LONGER THAN ACTUAL LONGEST - SAVE IT

        // DRAW LONGEST ONE ON ORIGINAL IMAGE
        // DRAW BOUNDARIES FOR LONGEST ROW ON ORIGINAL IMAGE


        return outputImage;
    }
}
