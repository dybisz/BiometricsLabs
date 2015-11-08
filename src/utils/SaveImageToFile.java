package utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dybisz on 07/11/2015.
 */
public class SaveImageToFile {
    public static void saveCurrentPictureToFile(Image image, String name) {
        String out = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss''").format(new Date());
        File file = new File("outputdata/" + name + out + ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (Exception s) {
            System.out.println("image saving error");
        }
    }
}
