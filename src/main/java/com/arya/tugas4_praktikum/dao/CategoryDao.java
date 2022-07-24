package com.arya.tugas4_praktikum.dao;

import com.arya.tugas4_praktikum.model.Category;
import com.arya.tugas4_praktikum.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDao implements DaoInterface<Category> {

    private final Connection connection = DatabaseConnection.getConnection();

    @Override
    public ObservableList<Category> read() {
        ObservableList<Category> categories = FXCollections.observableArrayList();

        String query = "SELECT * FROM category";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Category category = new Category(
                        result.getInt("id"),
                        result.getString("name")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    public void create(Category data) {
        String query = "INSERT INTO category VALUES(?, ?)";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, data.getId());
            preparedStatement.setString(2, data.getName());
            int result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Category data) {
        String query = "UPDATE category" +
                "SET id = ?, name = ?" +
                "WHERE id = ?";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, data.getId());
            preparedStatement.setString(2, data.getName());
            preparedStatement.setInt(3, data.getId());
            int result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Category data) {
        String query = "delete from category where id = ?";
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
