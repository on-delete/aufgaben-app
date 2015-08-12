package de.saxsys.tasksapp;

import de.saxsys.tasksapp.ui.MainView;
import de.saxsys.tasksapp.ui.TasksTabView;
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
		stage.setTitle("TodoMVVM");
		
		final Parent parent = FluentViewLoader.fxmlView(MainView.class).load().getView();
		
		stage.setScene(new Scene(parent));
		stage.setResizable(false);
		stage.show();
	}
}
