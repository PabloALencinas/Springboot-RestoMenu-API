package com.pabloagustin.myfood.project.backend.repositories;

import com.pabloagustin.myfood.project.backend.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {

}
