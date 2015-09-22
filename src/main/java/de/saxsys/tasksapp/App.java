package de.saxsys.tasksapp;

import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.tasksapp.controller.ViewController;
import de.saxsys.tasksapp.ui.MainView;
import de.saxsys.tasksapp.ui.TasksTabView;
import de.saxsys.tasksapp.ui.login.LoginView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import de.saxsys.mvvmfx.FluentViewLoader;

/**
 * @author manuel.mauky
 */
public class App extends Application {
	
	public static void main(String... args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		ViewController viewController = new ViewController(stage);

		NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
		notificationCenter.publish("showLoginView");

		stage.setTitle("TodoMVVM");
		stage.setResizable(false);
		stage.show();
	}
}
