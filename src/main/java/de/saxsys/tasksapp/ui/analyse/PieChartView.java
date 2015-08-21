package de.saxsys.tasksapp.ui.analyse;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;

/**
 * Created by andre.krause on 21.08.2015.
 */
public class PieChartView implements FxmlView<PieChartViewModel> {

	@FXML
	public PieChart taskChart;

	@InjectViewModel
	private PieChartViewModel viewModel;

	public void initialize() {
		taskChart.setData(viewModel.taskChartDataProperty());
	}

	public void updateChart() {
		viewModel.updateTaskChartData();
	}
}
