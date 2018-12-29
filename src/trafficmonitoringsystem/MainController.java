package trafficmonitoringsystem;


import Utilities.GlobalObjects;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledExecutorService;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


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
        if(cameraActive == false)
        {
            if(!textfield_path.getText().isEmpty())
            {
                cameraActive = true;
                GlobalObjects.getInstance().videoCapture = new VideoCapture(textfield_path.getText().trim());
                if(GlobalObjects.getInstance().videoCapture.isOpened())
                {
                    Runnable frameGrabber = new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            Mat frame = new Mat();
                            if(GlobalObjects.getInstance().videoCapture.read(frame))
                                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                            else
                            {
                                GlobalObjects.getInstance().shutdownScheduledExecutor(GlobalObjects.getInstance().timer);
                                GlobalObjects.getInstance().stopCamera(GlobalObjects.getInstance().videoCapture);
                            }
                                
                            Image imageToShow = GlobalObjects.getInstance().mat2Image(frame);
                            imageview_video.setImage(imageToShow);
                        }
                    };
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
    void jfxbutton_playOnClick(ActionEvent event) 
    {
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }    
    
}
