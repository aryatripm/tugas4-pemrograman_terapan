package com.arya.tugas4_praktikum.dao;

import com.arya.tugas4_praktikum.model.Category;
import com.arya.tugas4_praktikum.model.Item;
import com.arya.tugas4_praktikum.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemsDao implements DaoInterface<Item> {

    private final Connection connection = DatabaseConnection.getConnection();

    @Override
    public ObservableList<Item> read() {
        ObservableList<Item> items = FXCollections.observableArrayList();

        String query = "SELECT item.id AS id, item.name AS name, price, description, category_id, category.name AS category_name FROM item JOIN category ON item.category_id = category.id";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Item item = new Item(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getDouble("price"),
                        result.getString("description"),
                        new Category(
                                result.getInt("category_id"),
                                result.getString("category_name")
                        )
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    @Override
    public void create(Item data) {
        String query = "INSERT INTO item VALUES(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, data.getId());
            preparedStatement.setString(2, data.getName());
            preparedStatement.setDouble(3, data.getPrice());
            preparedStatement.setString(4, data.getDescription());
            preparedStatement.setInt(5, data.getCategory().getId());
            int result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Item data) {
        String query = "UPDATE item " +
                "SET id = ?, name = ?, price = ?, description = ?, category_id = ? " +
                "WHERE id = ?";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, data.getId());
            preparedStatement.setString(2, data.getName());
            preparedStatement.setDouble(3, data.getPrice());
            preparedStatement.setString(4, data.getDescription());
            preparedStatement.setInt(5, data.getCategory().getId());
            preparedStatement.setInt(6, data.getId());
            int result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Item data) {
        String query = "DELETE FROM item WHERE id = ?";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, data.getId());
            int result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
