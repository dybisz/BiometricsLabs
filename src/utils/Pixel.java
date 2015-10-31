package utils;

import javafx.scene.paint.Color;

/**
 *
 */
public class Pixel {
    private static final double MINIMUM_CHANNEL_SATURATION = 0.0;
    private static final double MAXIMUM_CHANNEL_SATURATION = 1.0;
    private double r;
    private double g;
    private double b;
    private double a;

    public Pixel(double red, double green, double blue) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.a = 1.0;
    }

    public Pixel(Color color) {
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();
        a = color.getOpacity();
    }

    public Color toColor() {
        return new Color(getR(), getG(), getB(), getA());
    }

    public void addValueToAllChannelsAndClamp(double value) {
        double modifiedRed = clampChannelSaturation(getR() + value);
        double modifiedGreen = clampChannelSaturation(getG() + value);
        double modifiedBlue = clampChannelSaturation(getB() + value);

        setR(modifiedRed);
        setG(modifiedGreen);
        setB(modifiedBlue);
    }

    public static double clampChannelSaturation(double value) {
        return Math.min(MAXIMUM_CHANNEL_SATURATION, Math.max(value, MINIMUM_CHANNEL_SATURATION));
    }

    public void clampAllChannels() {
        setR(clampChannelSaturation(getR()));
        setG(clampChannelSaturation(getG()));
        setB(clampChannelSaturation(getB()));
    }

    public void compareAndSetMinimumR(double r) {
        setR((getR() < r) ? getR() : r);
    }

    public void compareAndSetMinimumG(double g) {
        setG((getG() < g) ? getG() : g);
    }

    public void compareAndSetMinimumB(double b) {
        setB((getB() < b) ? getB() : b);
    }

    public void compareAndSetMaximumR(double r) {
        setR((getR() > r) ? getR() : r);
    }

    public void compareAndSetMaximumG(double g) {
        setG((getG() > g) ? getG() : g);
    }

    public void compareAndSetMaximumB(double b) {
        setB((getB() > b) ? getB() : b);
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setRGB(double red, double green, double blue) {
        setR(red);
        setG(green);
        setB(blue);
    }
}
