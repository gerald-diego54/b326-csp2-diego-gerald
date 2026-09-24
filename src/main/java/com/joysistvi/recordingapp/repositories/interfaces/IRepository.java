package com.joysistvi.recordingapp.repositories.interfaces;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {

    List<T> findAll();

    boolean save(T entity);

    Optional<T> findById(ID id);

    boolean update(T entity);

    boolean deleteById(ID id);
}