package LocalDatabase;

import Utilities.Day;
import Utilities.GlobalObjects;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class RetrieveDaysAverage extends DBOperations
{
    private HashMap<Day, String> result = new HashMap<>();
    private PreparedStatement statement = null;
    private Connection connection = null;
    private ResultSet resultSet = null;
    
    @Override
    public void retrieve(Day day) 
    {
        try 
        {
            initializeDB();
            statement = getStatement();
            connection = getConnection();
            resultSet = getResultSet();
            String command = "SELECT avg(COUNT) 'AVERAGE' FROM RAWDATA WHERE DAY = '"+day.toString()+"';";
            statement = connection.prepareStatement(command);
            resultSet = statement.executeQuery();
            if(resultSet.next()) 
            {
                String average = resultSet.getString("AVERAGE");
                if(average != null)
                    result.put(day, average);
                else
                    result.put(day, "0.0");
            }
        } 
        catch (SQLException sQLException) {sQLException.printStackTrace();}
        finally{closeConnection();}
    }

    public HashMap<Day, String> getResult() {return result;}
}
