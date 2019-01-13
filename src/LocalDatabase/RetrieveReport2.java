
package LocalDatabase;

import Entities.RecordEntity;
import Utilities.Day;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RetrieveReport2 extends DBOperations
{
    private ArrayList<RecordEntity> recordEntityList = new ArrayList<>();
    
    private PreparedStatement statement = null;
    private Connection connection = null;
    private ResultSet resultSet = null;

    public void retrieveValue(int hour) 
    {
        try 
        {
            initializeDB();
            RecordEntity recordEntity = new RecordEntity();
            statement = getStatement();
            connection = getConnection();
            resultSet = getResultSet();
            String command = "SELECT AVG(COUNT) AS 'AVERAGE', FACILITY, FACILTYTYPE FROM RAWDATA WHERE strftime('%H',TIMESTAMP) = '" + hour + "' ";
            statement = connection.prepareStatement(command);
            resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                String average = resultSet.getString("AVERAGE");
                if(average != null)
                {
                    recordEntity.setTime(String.valueOf(hour));
                    recordEntity.setFacility(resultSet.getString("FACILITY"));
                    recordEntity.setFacilityType(resultSet.getString("FACILTYTYPE"));
                    recordEntityList.add(recordEntity);
                }
                    
            }
        } catch (SQLException sQLException) {sQLException.printStackTrace();}
        finally{closeConnection();}
    }
    public ArrayList<RecordEntity> getResult() {return recordEntityList;}
}
