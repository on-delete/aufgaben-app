package de.saxsys.tasksapp.ui;

import de.saxsys.mvvmfx.FxmlView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

/**
 * @author manuel.mauky
 */
public class MainView implements FxmlView<MainViewModel> {

	@FXML
	public AnalyseTabView analyseTabViewController;

	@FXML
	TabPane tabPane;

	public void initialize() {
		tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			if((int) newValue == 1){
				analyseTabViewController.updateChart();
			}
		});
	}
}
