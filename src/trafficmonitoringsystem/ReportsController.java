package trafficmonitoringsystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Tab;

public class ReportsController implements Initializable 
{
    @FXML private Tab tab_averagevolume,tab_report2;
    @FXML private LineChart<?, ?> chart_averagevolume;
    @FXML private NumberAxis axis_y;
    @FXML private CategoryAxis axis_x;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
    }    
    
}
