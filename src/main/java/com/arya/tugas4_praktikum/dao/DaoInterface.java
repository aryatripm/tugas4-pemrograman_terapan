package com.arya.tugas4_praktikum.dao;

import javafx.collections.ObservableList;

public interface DaoInterface<T> {
    ObservableList<T> read();
    void create(T data);
    void update(T data);
    void delete(T data);
}
