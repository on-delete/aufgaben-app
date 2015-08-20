package de.saxsys.tasksapp.ui.item;

import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.itemlist.ListTransformation;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.tasksapp.model.TodoItem;
import de.saxsys.tasksapp.model.TodoItemStore;
import de.saxsys.tasksapp.ui.FilterHelper;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.concurrent.FutureCallback;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author manuel.mauky
 */
public class ItemOverviewViewModel implements ViewModel {

	private ListProperty<ItemViewModel> items = new SimpleListProperty<>();

	private final ListTransformation<TodoItem, ItemViewModel> allItems;
	private final ObservableList<ItemViewModel> completedItems;
	private final ObservableList<ItemViewModel> activeItems;

	public ItemOverviewViewModel() {
		allItems = new ListTransformation<>(TodoItemStore.getInstance().getItems(), ItemViewModel::new);

		completedItems = FilterHelper.filter(allItems.getTargetList(), ItemViewModel::completedProperty);
		activeItems = FilterHelper.filterInverted(allItems.getTargetList(), ItemViewModel::completedProperty);

		showAllItems();


		final NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
		notificationCenter.subscribe("showAll", (key, payload) -> showAllItems());
		notificationCenter.subscribe("showActive", (key, payload) -> showActiveItems());
		notificationCenter.subscribe("showCompleted", (key, payload) -> showCompletedItems());
	}

	public void initItemList(){

		Async.newInstance().execute(Request.Get("http://localhost:3420/getAllTasks"), new FutureCallback<Content>() {
			public void failed(final Exception ex) {
				System.out.println(ex.getMessage());
			}

			public void completed(final Content content) {
				Platform.runLater(() -> {
					JSONArray resultArray = new JSONObject(content.toString()).getJSONArray("tasks");
					for (int i = 0; i < resultArray.length(); i++) {
						JSONObject tempItem = (JSONObject) resultArray.get(i);
						allItems.getModelList().add(new TodoItem(tempItem.getInt("id"), tempItem.getString("title"), tempItem.getBoolean("status")));
					}
				});
			}

			public void cancelled() {
			}
		});
	}

	private void showAllItems() {
		items.set(allItems.getTargetList());
	}

	private void showCompletedItems() {
		items.setValue(completedItems);
	}

	private void showActiveItems() {
		items.setValue(activeItems);
	}


	public ObservableList<ItemViewModel> itemsProperty() {
		return items;
	}
}
