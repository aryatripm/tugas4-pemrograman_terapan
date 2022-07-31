package com.arya.tugas4_praktikum.controller;

import com.arya.tugas4_praktikum.Application;
import com.arya.tugas4_praktikum.dao.CategoryDao;
import com.arya.tugas4_praktikum.dao.ItemsDao;
import com.arya.tugas4_praktikum.model.Category;
import com.arya.tugas4_praktikum.model.Item;
import com.arya.tugas4_praktikum.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ItemsController {

    public TextField id;
    public TextField name;
    public TextField price;
    public TextArea description;
    public ComboBox<Category> category;
    public Button save;
    public Button reset;
    public Button update;
    public Button delete;
    public TableView<Item> table;
    public TableColumn<Item, Integer> idColumn;
    public TableColumn<Item, String> nameColumn;
    public TableColumn<Item, Double> priceColumn;
    public TableColumn<Item, Category> categoryColumn;

    private ObservableList<Item> items;
    private ObservableList<Category> categories;

    private ItemsDao itemsDao;
    private CategoryDao categoryDao;

    private CategoryController categoryController;
    private Stage stage;

    private Connection connection;

    public void initialize() {
        connection = DatabaseConnection.getConnection();
        stage = new Stage();
        itemsDao = new ItemsDao();
        categoryDao = new CategoryDao();

        items = FXCollections.observableArrayList();
        categories = FXCollections.observableArrayList();

        items.addAll(itemsDao.read());
        categories.addAll(categoryDao.read());

        table.setItems(items);
        idColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Item, Category>("category"));

        category.setItems(categories);
        category.getSelectionModel().selectFirst();

        update.setDisable(true);
        delete.setDisable(true);
    }

    public void showCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("category-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        categoryController = fxmlLoader.getController();
        categoryController.setCategories(categories);
        categoryController.setItemsController(this);

        stage.setTitle("Category Management");
        stage.setScene(scene);
//        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void save(ActionEvent actionEvent) {
        if (checkForm()) {
            Item item = new Item(
                    Integer.parseInt(id.getText()),
                    name.getText(),
                    Double.parseDouble(price.getText()),
                    description.getText(),
                    category.getValue()
            );

            itemsDao.create(item);
            items = itemsDao.read();
            table.setItems(items);

            resetForm();
        }
    }

    public void reset(ActionEvent actionEvent) {
        resetForm();

        table.getSelectionModel().clearSelection();

        save.setDisable(false);
        update.setDisable(true);
        delete.setDisable(true);
    }

    private void resetForm() {
        id.setText("");
        name.setText("");
        price.setText("");
        description.setText("");
        category.getSelectionModel().selectFirst();
    }

    public void update(ActionEvent actionEvent) {
        if (checkForm()) {
            Item item = table.getSelectionModel().getSelectedItem();

            item.setId(Integer.parseInt(id.getText()));
            item.setName(name.getText());
            item.setPrice(Double.parseDouble(price.getText()));
            item.setDescription(description.getText());
            item.setCategory(category.getValue());

            itemsDao.update(item);
            items = itemsDao.read();
            table.setItems(items);

            resetForm();
            save.setDisable(false);
            update.setDisable(true);
            delete.setDisable(true);
        }
    }

    public void delete(ActionEvent actionEvent) {
        if (checkForm()) {
            Item item = table.getSelectionModel().getSelectedItem();
            itemsDao.delete(item);
            items = itemsDao.read();
            table.setItems(items);

            resetForm();
            save.setDisable(false);
            update.setDisable(true);
            delete.setDisable(true);
        }
    }

    public void tableClicked(MouseEvent mouseEvent) {
        if (!table.getSelectionModel().getSelectedCells().isEmpty()) {
            Item item = table.getSelectionModel().getSelectedItem();
            id.setText(item.getId().toString());
            name.setText(item.getName());
            price.setText(item.getPrice().toString());
            description.setText(item.getDescription());
            category.getSelectionModel().select(item.getCategory());

            save.setDisable(true);
            update.setDisable(false);
            delete.setDisable(false);
        }
    }

    private boolean checkForm() {
        boolean result = true;
        if (id.getText().isEmpty() || name.getText().isEmpty() || price.getText().isEmpty() || description.getText().isEmpty() || category.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please fill all the fields");
            alert.show();
            result = false;
        }
        return result;
    }

    public void refrehCategories() {
        categories = categoryDao.read();
        category.setItems(categories);
    }

    public void simpleReport(ActionEvent actionEvent) throws JRException {
        Map parameter = new HashMap();
        JasperPrint jasperPrint = JasperFillManager.fillReport("report/Simple_Report.jasper", parameter, connection);
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);
    }

    public void groupReport(ActionEvent actionEvent) throws JRException {
        Map parameter = new HashMap();
        JasperPrint jasperPrint = JasperFillManager.fillReport("report/Group_Report.jasper", parameter, connection);
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);
    }
}