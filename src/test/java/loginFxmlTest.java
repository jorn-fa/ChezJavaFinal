import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.loadui.testfx.GuiTest;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.robot.Motion;

import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

public class loginFxmlTest extends GuiTest {




    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("View/LogIn.fxml"));
            return parent;
        } catch (IOException ex) {
            // TODO ...
        }
        return parent;
    }

    @Before
    public void hasGui(){

        TextField firstname=find("#firstNameField");
        TextField lastName=find("#lastNameField");
        PasswordField password=find("#passwordField");
        Button login=find("#loginButton");
        Button Cancel=find("#cancelButton");

    }

    @Test
    public void firstNameField(){
        TextField firstName =find("#firstNameField");
        firstName.setText("test");
        verifyThat("#firstNameField" , hasText("test"));
    }

}





