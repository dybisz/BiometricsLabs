package filters;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Created by dybisz on 16/10/2015.
 */
public abstract class Filter {
    private Image inputImage;
    private WritableImage outputImage;
    private double imageWidth;
    private double imageHeight;

    Filter(Image inputImage) {
        setInputImage(inputImage);
    }

    protected void setInputImage(Image inputImage) {
        this.inputImage = inputImage;
        this.imageWidth = inputImage.getWidth();
        this.imageHeight = inputImage.getHeight();
        this.outputImage = new WritableImage((int) this.imageWidth, (int) this.imageHeight);
    }

    public abstract WritableImage apply();

    protected Image getInputImage() {
        return inputImage;
    }

    protected WritableImage getOutputImage() {
        return outputImage;
    }

    protected double getImageWidth() {
        return imageWidth;
    }

    protected double getImageHeight() {
        return imageHeight;
    }
}
