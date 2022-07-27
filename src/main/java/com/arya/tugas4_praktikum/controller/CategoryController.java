package com.arya.tugas4_praktikum.controller;

import com.arya.tugas4_praktikum.dao.CategoryDao;
import com.arya.tugas4_praktikum.model.Category;
import com.arya.tugas4_praktikum.model.Item;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class CategoryController {
    public TextArea id;
    public TextArea name;
    public TableView<Category> table;
    public TableColumn<Category, Integer> idColumn;
    public TableColumn<Category, String> nameColumn;
    public Button saveButton;
    public Button resetButton;
    public Button updateButton;
    public Button deleteButton;

    private ObservableList<Category> categories;

    private CategoryDao categoryDao;
    private ItemsController itemsController;

    public void initialize() {
        categoryDao = new CategoryDao();

        idColumn.setCellValueFactory(new PropertyValueFactory<Category, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));

        updateButton.setDisable(true);
        deleteButton.setDisable(true);
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
            resetForm();
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

    public void reset(ActionEvent actionEvent) {
        resetForm();

        table.getSelectionModel().clearSelection();

        saveButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void resetForm() {
        id.setText("");
        name.setText("");
    }

    public void update(ActionEvent actionEvent) {
        if (checkForm()) {
            Category category = table.getSelectionModel().getSelectedItem();

            category.setId(Integer.parseInt(id.getText()));
            category.setName(name.getText());

            categoryDao.update(category);
            categories = categoryDao.read();
            table.setItems(categories);

            itemsController.refrehCategories();

            resetForm();
            saveButton.setDisable(false);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

    public void delete(ActionEvent actionEvent) {
        if (checkForm()) {
            Category category = table.getSelectionModel().getSelectedItem();
            categoryDao.delete(category);
            categories = categoryDao.read();
            table.setItems(categories);

            resetForm();
            saveButton.setDisable(false);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

    public void dataSelected(MouseEvent mouseEvent) {
        if (!table.getSelectionModel().getSelectedCells().isEmpty()) {
            Category category = table.getSelectionModel().getSelectedItem();
            id.setText(category.getId().toString());
            name.setText(category.getName());

            saveButton.setDisable(true);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }
}
