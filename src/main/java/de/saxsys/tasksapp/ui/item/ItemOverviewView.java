package de.saxsys.tasksapp.ui.item;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * @author manuel.mauky
 */
public class ItemOverviewView implements FxmlView<ItemOverviewViewModel> {

	@FXML
	public ListView<ItemViewModel> items;

	@InjectViewModel
	private ItemOverviewViewModel viewModel;

	public void initialize() {
		items.setPlaceholder(new Label("Keine Aufgaben vorhanden!"));
		viewModel.initItemList();
		items.setItems(viewModel.itemsProperty());

		items.setCellFactory(CachedViewModelCellFactory.create(
				vm -> FluentViewLoader.fxmlView(ItemView.class).viewModel(vm).load()));
	}

}
