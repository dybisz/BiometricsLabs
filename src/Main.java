import controllers.ImagePreview;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.SaveImageToFile;

// tinyurl.com/ns4ca9j
public class Main extends Application {
    ImagePreview imagePreview;
    Slider slider;
    Scene scene;
    private ImagePreview root;
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Biomteric Labs");
        Group root = new Group();
        imagePreview = new ImagePreview();
        setupSlider();
        Button button = new Button("save_pic");
        Button button2 = new Button("hist_norm");
        button2.setOnAction((event) -> {
            imagePreview.applyHistogramNormalization();
        });

        button.setOnAction((event) -> {
            SaveImageToFile.saveCurrentPictureToFile(imagePreview.getImage(), "");
        });
        VBox vbox = new VBox( button /*button2*//*, slider*/);

        root.getChildren().addAll(imagePreview, vbox);
        Scene scene = new Scene(root, 800, 600);
        imagePreview.fitWidthProperty().bind(scene.widthProperty());
        imagePreview.fitHeightProperty().bind(scene.heightProperty());
        imagePreview.setPreserveRatio(true);
//        startAnimation();


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void setupSlider() {
        slider = new Slider();
        slider.setMinorTickCount(100);
        slider.setMin(0.0);
        slider.setMax(1.0);

        slider.valueProperty().addListener((a,b,c) -> {
            imagePreview.applyTreshold(255.0 * slider.getValue());
            System.out.println("slider val: " + slider.getValue());
        });
    }

    void setupImagePreview() {

    }

    void startAnimation() {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE );
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(3000),
                new KeyValue (slider.valueProperty(), 1)));
        timeline.play();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
