package trafficmonitoringsystem;
import LocalDatabase.DBOperations;
import LocalDatabase.RetrieveDaysAverage;
import Utilities.GlobalObjects;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import java.net.URL;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

public class MainController implements Initializable
{
    int intervals = 5;  //5seconds
    TimeUnit unit = TimeUnit.SECONDS;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat currentDay = new SimpleDateFormat("EEEE");
    DBOperations db = new RetrieveDaysAverage();
    boolean cameraActive = false;
    int carCount = 0;
    Point linePoint1 = new Point(50, 100);
    Point linePoint2 = new Point(400, 100);
    Scalar greenColor = new Scalar(0, 255, 0);
    @FXML private HBox hbox_bottom;
    @FXML private AnchorPane anchorpane_center_main;
    @FXML private ImageView imageview_video;
    @FXML private TextField textfield_carcount,textfield_path;
    @FXML private Button button_choosefile,button_showreports;
    @FXML private JFXButton jfxbutton_play;

    @FXML void button_choosefileOnClick(ActionEvent event) 
    {
        textfield_path.setText(GlobalObjects.getInstance().showChooserDialog("mp4","avi","wmv"));
    }

    @FXML
    void jfxbutton_playOnClick(ActionEvent event)
    {
        CascadeClassifier cascadeClassifier = new CascadeClassifier("C:\\Users\\joey11\\Documents\\NetBeansProjects\\TrafficMonitoringSystem\\src\\trafficmonitoringsystem\\cars.xml");
        if(cameraActive == false)
        {
            if(!textfield_path.getText().isEmpty())
            {
                cameraActive = true;
                GlobalObjects.getInstance().videoCapture = new VideoCapture(textfield_path.getText().trim());
                if(GlobalObjects.getInstance().videoCapture.isOpened())
                {
                    Mat frame = new Mat();
                    MatOfRect detected = new MatOfRect();
                    MatOfRect rect = new MatOfRect();
                    startBackgroundCounter();
                    Runnable frameGrabber = new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            
                            if(GlobalObjects.getInstance().videoCapture.read(frame))
                            {
                                Imgproc.line(frame, linePoint1, linePoint2, greenColor);
                                cascadeClassifier.detectMultiScale(frame, rect);
                                for(Rect objects : rect.toArray())
                                {
                                    Point center = new Point(objects.x + objects.width / 2, objects.y + objects.height / 2);
                                    Imgproc.circle(frame, center, 2, greenColor);
                                    if(center.y > 100 && center.y <= 110)
                                    {
                                        carCount += rect.toArray().length;
                                        textfield_carcount.setText(String.valueOf(carCount));
                                        //Imgproc.rectangle(frame, objects.tl(), objects.br(),greeColor);
                                    }
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
            GlobalObjects.getInstance().shutdownScheduledExecutor(GlobalObjects.getInstance().grabber);
            GlobalObjects.getInstance().shutdownScheduledExecutor(GlobalObjects.getInstance().timer);
            GlobalObjects.getInstance().stopCamera(GlobalObjects.getInstance().videoCapture);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        jfxbutton_play.setGraphic(GlyphsDude.createIcon(MaterialDesignIcon.PLAY_PAUSE, "24px"));
        db.createDB();
    }    

    @FXML
    private void button_showreportsOnClick(ActionEvent event) 
    {
        GlobalObjects.getInstance().openNewWindow("Reports.fxml", "Reports", StageStyle.UTILITY);
    }
    private void startBackgroundCounter()
    {
        Runnable countGrab = new Runnable() 
        {
            @Override
            public void run() 
            {
                Date date = new Date();
                String count = textfield_carcount.getText().trim();
                String currentDateTime = dateFormat.format(date);
                String day = currentDay.format(date);
                db.insert(count, currentDateTime,day);
            }
        };
        GlobalObjects.getInstance().grabber = Executors.newSingleThreadScheduledExecutor();
        GlobalObjects.getInstance().grabber.scheduleAtFixedRate(countGrab, 0, intervals, unit);
    }

}
