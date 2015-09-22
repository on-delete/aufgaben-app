package de.saxsys.tasksapp.controller;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.tasksapp.ui.MainView;
import de.saxsys.tasksapp.ui.login.LoginView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Andre on 22.09.2015.
 */
public class ViewController {
    
    private Stage primaryStage;
    
    public ViewController(Stage primaryStage){
        this.primaryStage = primaryStage;

        final NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
        notificationCenter.subscribe("showMainView", (key, payload) -> showMainView());
        notificationCenter.subscribe("showLoginView", (key, payload) -> showLoginView());
    }

    private void showLoginView() {
        this.primaryStage.setScene(new Scene(FluentViewLoader.fxmlView(LoginView.class).load().getView()));
    }

    private void showMainView() {
        this.primaryStage.setScene(new Scene(FluentViewLoader.fxmlView(MainView.class).load().getView()));
    }
}
