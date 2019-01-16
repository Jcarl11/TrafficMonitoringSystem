
package Database;

import java.util.concurrent.CompletableFuture;
import javafx.concurrent.Task;
import org.asynchttpclient.Response;

public class TaskExecutor 
{
    Task<?> myTask;
    DatabaseOperation databaseOperation = new DatabaseOperation();
    private TaskExecutor(){}
    private static TaskExecutor instance = null;
    public static TaskExecutor getInstance()
    {
        if(instance == null)
            instance = new TaskExecutor();
        return instance;
    }
    
    public void registerUser(String username, String password, String email)
    {
        myTask = new Task<Response>() 
        {
            @Override
            protected Response call() throws Exception 
            {
                CompletableFuture<Response> completableFuture = CompletableFuture.supplyAsync(() -> databaseOperation.registerUser(username, password, email))
                        .thenApply(data -> databaseOperation.logoutUser(data));
                return completableFuture.get();
            }
        };
        new Thread(myTask).start();
    }
    public Task<?> getMyTask() {return myTask;}
}
