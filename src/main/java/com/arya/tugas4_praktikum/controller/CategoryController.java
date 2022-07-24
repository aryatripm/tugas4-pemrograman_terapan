package com.arya.tugas4_praktikum.controller;

import com.arya.tugas4_praktikum.dao.CategoryDao;
import com.arya.tugas4_praktikum.model.Category;
import com.arya.tugas4_praktikum.model.Item;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class CategoryController {
    public TextArea id;
    public TextArea name;
    public TableView<Category> table;
    public TableColumn<Category, Integer> idColumn;
    public TableColumn<Category, String> nameColumn;

    private ObservableList<Category> categories;

    private CategoryDao categoryDao;
    private ItemsController itemsController;

    public void initialize() {
        categoryDao = new CategoryDao();

        idColumn.setCellValueFactory(new PropertyValueFactory<Category, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
    }

    public void save(ActionEvent actionEvent) {
        if (checkForm()) {
            Category category = new Category(
                    Integer.parseInt(id.getText()),
                    name.getText()
            );

            categoryDao.create(category);
            categories = categoryDao.read();
            table.setItems(categories);

            itemsController.refrehCategories();
        }
    }

    private boolean checkForm() {
        boolean result = true;
        if (id.getText().isEmpty() || name.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.show();
            result = false;
        }
        return result;
    }

    public void setCategories(ObservableList<Category> categoryList) {
        categories = categoryList;
        table.setItems(categories);
    }

    public void setItemsController(ItemsController controller) {
        itemsController = controller;
    }
}
