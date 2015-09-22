package de.saxsys.tasksapp.ui.login;

import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.application.Platform;
import javafx.beans.property.*;

/**
 * Created by Andre on 22.09.2015.
 */
public class LoginViewModel implements ViewModel {

    private StringProperty usernameText = new SimpleStringProperty();
    private StringProperty passwordText = new SimpleStringProperty();

    private BooleanProperty fieldsDisabled = new SimpleBooleanProperty(false);

    private NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();

    public StringProperty usernameTextProperty() {
        return usernameText;
    }

    public StringProperty passwordTextProperty() {
        return passwordText;
    }

    public BooleanProperty fieldsDisabledProperty() {
        return fieldsDisabled;
    }

    public void close() {
        Platform.exit();
        System.exit(0);
    }

    public void login() {
        if(fieldsDisabled.getValue()){

        }
        else{

        }

        notificationCenter.publish("showMainView");
    }

    public void setOnline() {
        fieldsDisabled.setValue(false);
    }

    public void setLocal() {
        fieldsDisabled.setValue(true);
    }
}
