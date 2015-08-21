package de.saxsys.tasksapp.ui.analyse;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.tasksapp.model.TodoItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.concurrent.FutureCallback;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by andre.krause on 21.08.2015.
 */
public class PieChartViewModel implements ViewModel {

	private ObservableList<PieChart.Data> taskChartData = FXCollections.observableArrayList();

	public ObservableList<PieChart.Data> taskChartDataProperty() {
		return taskChartData;
	}

	public void updateTaskChartData() {
		Async.newInstance().execute(Request.Get("http://localhost:3421/getTaskStatus"), new FutureCallback<Content>() {
			public void failed(final Exception ex) {
				System.out.println(ex.getMessage());
			}

			public void completed(final Content content) {
				Platform.runLater(() -> {
					taskChartData.clear();
					JSONObject json = new JSONObject(content.toString());
					taskChartData.add(new PieChart.Data("Finished", Integer.getInteger(json.get("finished").toString())));
					taskChartData.add(new PieChart.Data("Open", Integer.getInteger(json.get("open").toString())));
				});
			}

			public void cancelled() {
			}
		});
	}
}
