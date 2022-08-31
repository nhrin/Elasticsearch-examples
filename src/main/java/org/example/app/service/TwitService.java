package org.example.app.service;

import org.example.app.entity.Twit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TwitService<T> {

    void saveTwit(T t);

    Page<T> findAll(Pageable pageable);

    List<T> findDocsByContent(String query);

    Optional<T> findById(String id);

}
