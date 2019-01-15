package trafficmonitoringsystem;

import Utilities.GlobalObjects;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;

public class LoginRegisterController implements Initializable 
{
    @FXML private AnchorPane anchorpane_register,anchorpane_login;
    @FXML private JFXTextField register_textfield_email,register_textfield_username;
    @FXML private JFXPasswordField register_textfield_password,register_textfield_repassword,login_textfield_password;
    @FXML private JFXButton register_button_submit,login_button_login;
    @FXML private ProgressIndicator register_progress,login_progress;
    @FXML private JFXTextField login_textfield_username;
    @FXML private CheckBox login_checkbox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
    }    

    @FXML
    private void register_button_submitOnClick(ActionEvent event) 
    {
        if(!registerHasNullFields())
        {
            if(register_textfield_password.getText().trim().equals(register_textfield_repassword.getText().trim()))
            {
                GlobalObjects.getInstance().showMessage("OK", anchorpane_register);
            }
            else
                GlobalObjects.getInstance().showMessage("Password does not match", anchorpane_register);
        }
        else
            GlobalObjects.getInstance().showMessage("Don't leave null fields", anchorpane_register);
    }

    @FXML
    private void login_button_loginOnClick(ActionEvent event) {
    }
    private boolean registerHasNullFields()
    {
        boolean hasNull = true;
        if(!register_textfield_email.getText().isEmpty() && !register_textfield_username.getText().isEmpty() && 
                !register_textfield_password.getText().isEmpty() && !register_textfield_repassword.getText().isEmpty())
            hasNull = false;
        
        return hasNull;
    }
}
