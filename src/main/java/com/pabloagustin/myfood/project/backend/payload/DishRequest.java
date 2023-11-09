package com.pabloagustin.myfood.project.backend.payload;

import com.pabloagustin.myfood.project.backend.models.Dish;
import com.pabloagustin.myfood.project.backend.repositories.DishRepository;
import jakarta.annotation.Nullable;

public class DishRequest {


	private String name;
	private String description;
	private String ingredients;

	public DishRequest(){}

	public DishRequest(String name, String description, String ingredients) {
		this.name = name;
		this.description = description;
		this.ingredients = ingredients;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
}
