package de.saxsys.tasksapp.ui.controls;

import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.tasksapp.model.TodoItem;
import de.saxsys.tasksapp.model.TodoItemStore;
import de.saxsys.tasksapp.ui.FilterHelper;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.ObservableList;

/**
 * @author manuel.mauky
 */
public class ControlsViewModel implements ViewModel {
	
	private StringProperty itemsLeftLabelText = new SimpleStringProperty();
	
	private NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
	
	public ControlsViewModel() {
		final ObservableList<TodoItem> items = TodoItemStore.getInstance().getItems();
		
		ObservableList<TodoItem> completedItems = FilterHelper.filterInverted(items, TodoItem::completedProperty);
		
		final IntegerBinding size = Bindings.size(completedItems);
		
		final StringBinding itemsLabel = Bindings.when(size.isEqualTo(1)).then("item").otherwise("items");
		itemsLeftLabelText.bind(Bindings.concat(size, " ", itemsLabel, " left"));
	}
	
	public StringProperty itemsLeftLabelTextProperty() {
		return itemsLeftLabelText;
	}
	
	public void all() {
		notificationCenter.publish("showAll");
	}
	
	public void active() {
		notificationCenter.publish("showActive");
	}
	
	public void completed() {
		notificationCenter.publish("showCompleted");
	}
}
