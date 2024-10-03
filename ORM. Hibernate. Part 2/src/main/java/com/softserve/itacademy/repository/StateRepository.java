package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    // Method to retrieve a State by its name
    State findByName(String name);

    // Method to retrieve all States sorted by name in ascending order
    @Query("SELECT s FROM State s ORDER BY s.name ASC")
    List<State> findAllByOrderByNameAsc();
}
