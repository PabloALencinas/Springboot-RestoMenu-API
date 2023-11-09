package com.pabloagustin.myfood.project.backend.services.dish;

import com.pabloagustin.myfood.project.backend.models.Dish;
import com.pabloagustin.myfood.project.backend.models.User;
import com.pabloagustin.myfood.project.backend.payload.DishRequest;
import com.pabloagustin.myfood.project.backend.repositories.DishRepository;
import com.pabloagustin.myfood.project.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishService {

	private final DishRepository dishRepository;
	private final UserRepository userRepository;
	public void createDish(Long userId, Dish dishRequest){

		Optional<User> currentUser = userRepository.findById(userId);

		if(currentUser.isPresent()){
			User user = currentUser.get();

			Dish newDish = new Dish();
			newDish.setName(dishRequest.getName());
			newDish.setDescription(dishRequest.getDescription());
			newDish.setIngredients(dishRequest.getIngredients());
			newDish.setUser(user);

			dishRepository.save(newDish);

		} else {
			throw new UsernameNotFoundException("User not found");
		}
	}

	public void updateDish (Long userId, Long dishId, DishRequest dishRequest){

		Optional<User> currentUser = userRepository.findById(userId);

		if(currentUser.isPresent()){

			Optional<Dish> dish = dishRepository.findById(dishId);
			if(dish.isPresent()){
				Dish currentDish = dish.get();
				if(dishRequest.getName() == null){
					currentDish.setName(currentDish.getName());
				} else {
					currentDish.setName(dishRequest.getName());
				}

				if(dishRequest.getDescription() == null){
					currentDish.setDescription(currentDish.getDescription());
				} else {
					currentDish.setDescription(dishRequest.getDescription());
				}

				if(dishRequest.getIngredients() == null){
					currentDish.setIngredients(currentDish.getIngredients());
				} else {
					currentDish.setIngredients(dishRequest.getIngredients());
				}

				dishRepository.save(currentDish);
			} else {
				throw new UsernameNotFoundException("Dish not found");
			}
		} else {
			throw new UsernameNotFoundException("User not found");
		}


	}

}
