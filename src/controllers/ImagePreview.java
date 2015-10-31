package controllers;

import filters.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//     >> BIOMETRIC TASK SOLUTION:
//        applyGrayScale();
//        applyBlur();
//        applyTreshold(8);
//        applySobelFilter();

public class ImagePreview extends ImageView {
    private final static String DEFAULT_IMAGE_URL = "face_2.jpg";
    Image image = null;

    public ImagePreview() {
        loadDefaultImage();
        applyFaceDetection();
    }

    private void applyFaceDetection() {
        if(image != null) {
            FaceDetection faceDetection = new FaceDetection(image);
            image = faceDetection.apply();
            setImage(image);
        }
    }

    private void applyBlur() {
        if(image != null) {
            Blur blur = new Blur(image, 5);
            image = blur.apply();
            setImage(image);
        }
    }

    private void applySobelFilter() {
        if(image != null) {
            SobelFilter sobelFilter = new SobelFilter(image);
            image = sobelFilter.apply();
            setImage(image);
        }
    }

    private void applyGaussianFilter() {
        if(image != null) {
            GaussianFilter gaussianFilter = new GaussianFilter(image);
            image = gaussianFilter.apply();
            setImage(image);
        }
    }

    private void applyLowPassFilter() {
        if(image != null) {
            LowPassFilter lowPassFilter = new LowPassFilter(image);
            image = lowPassFilter.apply();
            setImage(image);
        }
    }

    private void applyHighPassFilter() {
        if (image != null) {
            HighPassFilter highPassFilter = new HighPassFilter(image);
            image = highPassFilter.apply();
            setImage(image);
        }
    }

    public void saveCurrentPictureToFile() {
        String out = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss''").format(new Date());
        File file = new File(out + ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(getImage(), null), "png", file);
        } catch (Exception s) {
        }
    }

    public void applyBrightness(double brightnessLevel) {
        if (image != null) {
            Brightness brightness = new Brightness(image);
            brightness.setBrightness(brightnessLevel);
            setImage(brightness.apply());
        }
    }

    public void applyHistogramNormalization() {
        if (image != null) {
            HistogramNormalization histogramNormalization =
                    new HistogramNormalization(image, HistogramNormalization.NormalizationType.PER_CHANNEL);
            WritableImage outputImage = histogramNormalization.apply();
            image = outputImage;
            setImage(outputImage);
        }
    }

    /**
     * Apply treshold to given image.
     *
     * @param treshoold Value of treshold.
     */
    public void applyTreshold(double treshoold) {
        if (image != null) {
            Tresholding tresholding = new Tresholding(image, treshoold);
            WritableImage outputImage = tresholding.apply();
            image = outputImage;
            setImage(outputImage);
        }
    }

    /**
     * Changes current image to grayscale.
     */
    private void applyGrayScale() {
        if (image != null) {
            Grayscale grayscale = new Grayscale(image);
            WritableImage outputImage = grayscale.apply();
            image = outputImage;
            setImage(outputImage);
        }
    }

    /**
     * Blurs current image.
     *
     * @param kernelSize Size of (square) convolution matrix.
     */
    private void blur(int kernelSize) {
        if (image != null) {
            Blur blur = new Blur(image, kernelSize);
            WritableImage outputImage = blur.apply();
            image = outputImage;
            setImage(outputImage);
        }
    }

    /**
     * Loads image from {@link #DEFAULT_IMAGE_URL} url.
     */
    private void loadDefaultImage() {
        image = new Image(DEFAULT_IMAGE_URL, false);
        setImage(image);
    }

}
