package de.saxsys.tasksapp.ui.additems;

import de.saxsys.tasksapp.model.TodoItem;
import de.saxsys.tasksapp.model.TodoItemStore;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import de.saxsys.mvvmfx.ViewModel;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

/**
 * @author manuel.mauky
 */
public class AddItemsViewModel implements ViewModel {


    private BooleanProperty allSelected = new SimpleBooleanProperty();
    private StringProperty newItemValue = new SimpleStringProperty("");

    private ReadOnlyBooleanWrapper allSelectedVisible = new ReadOnlyBooleanWrapper();

    public AddItemsViewModel() {
        allSelected.addListener((obs, oldV, newV) -> {
            TodoItemStore.getInstance().getItems()
                    .forEach(item -> item.setCompleted(newV));
        });

        allSelectedVisible.bind(Bindings.isEmpty(TodoItemStore.getInstance().getItems()).not());
    }


    public void addItem() {
        final String newValue = newItemValue.get();
        if (newValue != null && !newValue.trim().isEmpty()) {
            Async.newInstance().execute(Request.Post("http://localhost:3420/addTask")
                    .version(HttpVersion.HTTP_1_1)
                    .bodyString("{\"task\":\"" + newValue + "\"};", ContentType.APPLICATION_JSON), new FutureCallback<Content>() {
                public void failed(final Exception ex) {
                    System.out.println(ex.getMessage());
                }

                public void completed(final Content content) {
                    Platform.runLater(() -> {
                        TodoItem newItem = new TodoItem(newValue);
                        TodoItemStore.getInstance().getItems().add(newItem);

                        JSONObject json = new JSONObject(content.toString());
                        newItem.setId(Integer.parseInt(json.get("id").toString()));

                        newItemValue.set("");
                    });
                }

                public void cancelled() {
                }
            });
        }
    }

    public StringProperty newItemValueProperty() {
        return newItemValue;
    }


    public BooleanProperty allSelectedProperty() {
        return allSelected;
    }

    public ReadOnlyBooleanProperty allSelectedVisibleProperty() {
        return allSelectedVisible;
    }
}
