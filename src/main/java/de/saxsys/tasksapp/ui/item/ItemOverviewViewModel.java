package de.saxsys.tasksapp.ui.item;

import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.itemlist.ListTransformation;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.tasksapp.model.TodoItem;
import de.saxsys.tasksapp.model.TodoItemStore;
import de.saxsys.tasksapp.ui.FilterHelper;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import java.io.IOException;

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

		//allItems.getModelList().add()
	}

	public void initItemList(){
		try {
			String result = Request.Get("http://localhost:3420/getAllTasks")
					.version(HttpVersion.HTTP_1_1)
					.execute().returnContent().asString();

			JSONObject json = new JSONObject(result);
			System.out.println(json.toString());
		}
		catch (IOException e){
			e.printStackTrace();
		}
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
