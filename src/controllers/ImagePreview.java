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
//        applyTreshold(10);
//        applySobelFilter();

public class ImagePreview extends ImageView {
    private final static String ORIGINAL_IMAGE_URL = "raw_data_1.jpg";
    private final static String DEFAULT_IMAGE_URL = "r1.bmp";
    Image image = null;
    Image originalImage = null;

    public ImagePreview() {
        loadDefaultImage();
        applyHistogramNormalization();
        applyTreshold(90);
        applyKMM();
    }

    private void applyKMM() {
        try {
            KMM kmm = new KMM(image);
            image = kmm.apply();
            setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFindPupilDiameter() {
        try {
            FindPupilDiameter findPupilDiameter = new FindPupilDiameter(image, new Image(ORIGINAL_IMAGE_URL, false));
            image = findPupilDiameter.apply();
            setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyConditionalDilation() {
        try {
            ConditionalDilation conditionalDilation = new ConditionalDilation(image);
            image = conditionalDilation.apply();
            setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyErosion() {
        try {
            Erosion erosion = new Erosion(image);
            image = erosion.apply();
            setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyDilation() {
        try {
            Dilation dilation = new Dilation(image);
            image = dilation.apply();
            setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFaceDetection() {
        if (image != null) {
            FaceDetection faceDetection = new FaceDetection(image);
            image = faceDetection.apply();
            setImage(image);
        }
    }

    private void applyBlur() {
        if (image != null) {
            Blur blur = new Blur(image, 7);
            image = blur.apply();
            setImage(image);
        }
    }

    private void applySobelFilter() {
        if (image != null) {
            SobelFilter sobelFilter = new SobelFilter(image);
            image = sobelFilter.apply();
            setImage(image);
        }
    }

    private void applyGaussianFilter() {
        if (image != null) {
            GaussianFilter gaussianFilter = new GaussianFilter(image);
            image = gaussianFilter.apply();
            setImage(image);
        }
    }

    private void applyLowPassFilter() {
        if (image != null) {
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
        originalImage = image;
    }

}
