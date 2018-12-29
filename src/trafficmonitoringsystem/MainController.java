package trafficmonitoringsystem;
import Utilities.GlobalObjects;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import java.util.concurrent.TimeUnit;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

public class MainController implements Initializable
{
    
    boolean cameraActive = false;
    @FXML private AnchorPane anchorpane_center_main;
    @FXML private ImageView imageview_video;
    @FXML private TextField textfield_carcount,textfield_path;
    @FXML private Button button_choosefile,button_playpause;

    @FXML void button_choosefileOnClick(ActionEvent event) 
    {
        textfield_path.setText(GlobalObjects.getInstance().showChooserDialog("mp4","avi","wmv"));
    }

    @FXML void button_playpauseOnClick(ActionEvent event) 
    {
        CascadeClassifier cascadeClassifier = new CascadeClassifier("C:\\Users\\Windows\\Documents\\NetBeansProjects\\TrafficMonitoringSystem\\src\\trafficmonitoringsystem\\cars.xml");
        if(cameraActive == false)
        {
            if(!textfield_path.getText().isEmpty())
            {
                cameraActive = true;
                GlobalObjects.getInstance().videoCapture = new VideoCapture(textfield_path.getText().trim());
                if(GlobalObjects.getInstance().videoCapture.isOpened())
                {
                    Mat frame = new Mat();
                    Mat frameCopy = new Mat();
                    MatOfRect rect = new MatOfRect();
                    
                    Runnable frameGrabber = new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            
                            if(GlobalObjects.getInstance().videoCapture.read(frame))
                            {
                                cascadeClassifier.detectMultiScale(frame, rect);
                                for(Rect objects : rect.toArray())
                                {
                                    Imgproc.rectangle(frame, new Point(objects.x, objects.y), new Point(objects.x + objects.width, objects.y + objects.height),
                                                new Scalar(0, 255, 0));
                                }
                                Image imageToShow = GlobalObjects.getInstance().mat2Image(frame);
                                imageview_video.setImage(imageToShow);
                            }
                            else
                            {
                                GlobalObjects.getInstance().shutdownScheduledExecutor(GlobalObjects.getInstance().timer);
                                GlobalObjects.getInstance().stopCamera(GlobalObjects.getInstance().videoCapture);
                            }
                                
                            
                        }
                    };
                    GlobalObjects.getInstance().timer = Executors.newSingleThreadScheduledExecutor();
                    GlobalObjects.getInstance().timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
                }
                else
                    GlobalObjects.getInstance().showMessage("Cannot be opened", anchorpane_center_main);
            }
            else
                GlobalObjects.getInstance().showMessage("Choose a video file", anchorpane_center_main);
        }
        else
        {
            cameraActive = false;
            GlobalObjects.getInstance().shutdownScheduledExecutor(GlobalObjects.getInstance().timer);
            GlobalObjects.getInstance().stopCamera(GlobalObjects.getInstance().videoCapture);
        }
    }
    @FXML
    void jfxbutton_playOnClick(ActionEvent event){}
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    
}
