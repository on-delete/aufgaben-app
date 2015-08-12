package de.saxsys.tasksapp.model;

import javafx.beans.property.*;

/**
 * @author manuel.mauky
 */
public class TodoItem {

	private IntegerProperty id = new SimpleIntegerProperty();

	private StringProperty text = new SimpleStringProperty();

	private BooleanProperty completed = new SimpleBooleanProperty(false);

	public TodoItem(String text) {
		this.text.set(text);
	}

	public int getId() {
		return id.get();
	}

	public IntegerProperty idProperty() {
		return id;
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public String getText() {
		return text.get();
	}

	public StringProperty textProperty() {
		return text;
	}

	public void setText(String text) {
		this.text.set(text);
	}

	public boolean isCompleted() {
		return completed.get();
	}

	public BooleanProperty completedProperty() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed.set(completed);
	}
}
