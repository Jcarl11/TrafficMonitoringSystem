package trafficmonitoringsystem;

import LocalDatabase.DBOperations;
import LocalDatabase.RetrieveDaysAverage;
import Utilities.Day;
import Utilities.GlobalObjects;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;

public class ReportsController implements Initializable 
{
    RetrieveDaysAverage db = new RetrieveDaysAverage();
    @FXML private Tab tab_averagevolume,tab_report2;
    @FXML private LineChart<?, ?> chart_averagevolume;
    @FXML private NumberAxis axis_y;
    @FXML private CategoryAxis axis_x;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(()->db.retrieve(Day.Monday))
                .thenRun(()->db.retrieve(Day.Tuesday))
                .thenRun(()->db.retrieve(Day.Wednesday))
                .thenRun(()->db.retrieve(Day.Thursday))
                .thenRun(()->db.retrieve(Day.Friday))
                .thenRun(()->db.retrieve(Day.Saturday))
                .thenRun(()->db.retrieve(Day.Sunday));
        try {
            cf.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HashMap<Day, String> result = db.getResult();
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data(Day.Monday.toString(), Double.valueOf(result.get(Day.Monday))));
        series.getData().add(new XYChart.Data(Day.Tuesday.toString(), Double.valueOf(result.get(Day.Tuesday))));
        series.getData().add(new XYChart.Data(Day.Wednesday.toString(), Double.valueOf(result.get(Day.Wednesday))));
        series.getData().add(new XYChart.Data(Day.Thursday.toString(), Double.valueOf(result.get(Day.Thursday))));
        series.getData().add(new XYChart.Data(Day.Friday.toString(), Double.valueOf(result.get(Day.Friday))));
        series.getData().add(new XYChart.Data(Day.Saturday.toString(), Double.valueOf(result.get(Day.Saturday))));
        series.getData().add(new XYChart.Data(Day.Sunday.toString(), Double.valueOf(result.get(Day.Sunday))));
        chart_averagevolume.getData().add(series);
    }    
    
}
