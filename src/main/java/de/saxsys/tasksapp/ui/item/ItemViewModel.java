package de.saxsys.tasksapp.ui.item;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.tasksapp.model.TodoItem;
import de.saxsys.tasksapp.model.TodoItemStore;
import javafx.beans.property.*;

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
	}

	public void delete() {
		TodoItemStore.getInstance().getItems().remove(item);
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
