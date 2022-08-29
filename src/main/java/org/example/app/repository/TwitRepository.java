package org.example.app.repository;

import org.example.app.entity.Twit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public interface TwitRepository extends JpaRepository<Twit, Integer> {

}
