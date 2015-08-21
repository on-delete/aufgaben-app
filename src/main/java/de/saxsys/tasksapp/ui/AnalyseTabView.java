package de.saxsys.tasksapp.ui;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.tasksapp.ui.analyse.PieChartView;
import javafx.fxml.FXML;

/**
 * Created by andre.krause on 21.08.2015.
 */
public class AnalyseTabView implements FxmlView<AnalyseTabViewModel> {

	@FXML
	public PieChartView pieChartViewController;

	public void updateChart() {
		pieChartViewController.updateChart();
	}
}
