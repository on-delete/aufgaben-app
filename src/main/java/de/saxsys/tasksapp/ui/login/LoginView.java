package de.saxsys.tasksapp.ui.login;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.beans.EventHandler;

/**
 * Created by Andre on 22.09.2015.
 */
public class LoginView implements FxmlView<LoginViewModel> {

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @InjectViewModel
    private LoginViewModel viewModel;

    public void initialize(){
        usernameField.textProperty().bindBidirectional(viewModel.usernameTextProperty());
        usernameField.disableProperty().bindBidirectional(viewModel.fieldsDisabledProperty());

        passwordField.textProperty().bindBidirectional(viewModel.passwordTextProperty());
        passwordField.disableProperty().bindBidirectional(viewModel.fieldsDisabledProperty());
    }

    public void close(){viewModel.close();}

    public void login(){viewModel.login();}

    public void setOnline(){viewModel.setOnline();}

    public void setLocal(){viewModel.setLocal();}
}
