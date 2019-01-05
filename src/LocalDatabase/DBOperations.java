
package LocalDatabase;

import java.sql.*;

public class DBOperations 
{
    private String DATABASE_NAME = "jdbc:sqlite:LocalStorage.db";
    private PreparedStatement statement = null;
    private Connection connection = null;
    private ResultSet resultSet = null;
    
    private void initializeDB()
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
                    + "TIMESTAMP DATETIME);";
            statement = connection.prepareStatement(command);
            int result = statement.executeUpdate();
        }
        catch(Exception ex)
        {ex.printStackTrace();}
        finally{closeConnection();}
        
    }
    public void insert(String count, String timeStamp)
    {
        try
        {
            initializeDB();
            String command = "insert into RAWDATA(COUNT,TIMESTAMP)values(?,?);";
            statement = connection.prepareStatement(command);
            statement.setString(1, count);
            statement.setString(2, timeStamp);
            statement.executeUpdate();
        }catch(Exception ex){ex.printStackTrace();}
        finally{closeConnection();}
    }
    public void retrieve()
    {
        
    }
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
}
