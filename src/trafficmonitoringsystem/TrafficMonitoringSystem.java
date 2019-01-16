
package trafficmonitoringsystem;

import Utilities.GlobalObjects;
import Utilities.UsersPreferences;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

/**
 *
 * @author Windows
 */
public class TrafficMonitoringSystem extends Application {
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        loader.setController(new MainController());
        BorderPane mainPane = loader.load();*/
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent event) 
            {
                GlobalObjects.getInstance().shutdownScheduledExecutor(GlobalObjects.getInstance().grabber);
                GlobalObjects.getInstance().shutdownScheduledExecutor(GlobalObjects.getInstance().timer);
                GlobalObjects.getInstance().stopCamera(GlobalObjects.getInstance().videoCapture);
            }
        });
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        if(UsersPreferences.getInstance().getPreference().get("sessionToken", null) != null)
            stage.show();
        else
            GlobalObjects.getInstance().openNewWindow("LoginRegister.fxml", "Login or Signup", StageStyle.DECORATED);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
