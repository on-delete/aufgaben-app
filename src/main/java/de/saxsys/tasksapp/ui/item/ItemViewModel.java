package de.saxsys.tasksapp.ui.item;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.tasksapp.model.TodoItem;
import de.saxsys.tasksapp.model.TodoItemStore;
import javafx.application.Platform;
import javafx.beans.property.*;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;

/**
 * @author manuel.mauky
 */
public class ItemViewModel implements ViewModel {

	private BooleanProperty completed = new SimpleBooleanProperty();

	private BooleanProperty editMode = new SimpleBooleanProperty();

	private StringProperty content = new SimpleStringProperty();

	private TodoItem item;

	public ItemViewModel(TodoItem item) {
		this.item = item;
		content.bindBidirectional(item.textProperty());
		completed.bindBidirectional(item.completedProperty());

		completed.addListener((observable, oldValue, newValue) -> {
			Async.newInstance().execute(Request.Put("http://localhost:3420/updateTaskStatus")
					.version(HttpVersion.HTTP_1_1)
					.bodyString("{\"taskId\":\"" + this.item.getId() + "\", \"status\":\"" + newValue + "\"};", ContentType.APPLICATION_JSON), new FutureCallback<Content>() {
				public void failed(final Exception ex) {
					System.out.println(ex.getMessage());
				}

				public void completed(final Content content) {
					System.out.println("Status geändert!");
				}

				public void cancelled() {
				}
			});
		});
	}

	public void delete() {
		Async.newInstance().execute(Request.Delete("http://localhost:3420/deleteTask/" + item.getId())
				.version(HttpVersion.HTTP_1_1), new FutureCallback<Content>() {
			public void failed(final Exception ex) {
				System.out.println(ex.getMessage());
			}

			public void completed(final Content content) {
				System.out.println("Task gelöscht!");
				Platform.runLater(() -> TodoItemStore.getInstance().getItems().remove(item));
			}

			public void cancelled() {
			}
		});
	}

	public StringProperty contentProperty() {
		return content;
	}

	public BooleanProperty completedProperty() {
		return completed;
	}

	public boolean isCompleted() {
		return completed.get();
	}

	public BooleanProperty editModeProperty() {
		return editMode;
	}

	public ReadOnlyBooleanProperty textStrikeThrough() {
		return completed;
	}
}
