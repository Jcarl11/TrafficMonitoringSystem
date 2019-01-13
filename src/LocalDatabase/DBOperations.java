
package LocalDatabase;

import Utilities.Day;
import java.sql.*;
import java.util.HashMap;

public abstract class DBOperations 
{
    private String DATABASE_NAME = "jdbc:sqlite:LocalStorage.db";
    private PreparedStatement statement = null;
    private Connection connection = null;
    private ResultSet resultSet = null;
    
    protected void initializeDB()
    {
        try{connection = DriverManager.getConnection(DATABASE_NAME);}
        catch(Exception ex){ex.printStackTrace();}
    }
    public void createDB()
    {
        try
        {
            initializeDB();
            String command = "CREATE TABLE IF NOT EXISTS RAWDATA"
                    + "(COUNT VARCHAR(50),"
                    + "TIMESTAMP DATETIME,"
                    + "DAY VARCHAR(50));";
            statement = connection.prepareStatement(command);
            int result = statement.executeUpdate();
        }
        catch(Exception ex)
        {ex.printStackTrace();}
        finally{closeConnection();}
        
    }
    public void insert(String count, String timeStamp, String day,String facility, String facilityType)
    {
        try 
        {
            initializeDB();
            String command = "insert into RAWDATA(COUNT,TIMESTAMP,DAY,FACILITY,FACILITYTYPE)values(?,?,?,?,?);";
            statement = connection.prepareStatement(command);
            statement.setString(1, count);
            statement.setString(2, timeStamp);
            statement.setString(3, day);
            statement.setString(4, facility);
            statement.setString(5, facilityType);
            statement.executeUpdate();
        } 
        catch (SQLException sQLException) {sQLException.printStackTrace();}
        finally{closeConnection();}
        
    }
    public void retrieve(Day day){}
    public void update()
    {
        
    }
    public void delete()
    {
        
    }
    public void closeConnection()
    {
        try
        {
            if(statement != null)
                statement.close();
            if(connection != null)
                connection.close();
            if(resultSet != null)
                resultSet.close();
        }catch(Exception ex)
        {
            ex.printStackTrace();
}
    }
    public PreparedStatement getStatement() {return statement;}
    public Connection getConnection() {return connection;}
    public ResultSet getResultSet() {return resultSet;}
}
