package com.pabloagustin.myfood.project.backend.controllers;


import com.pabloagustin.myfood.project.backend.models.Dish;
import com.pabloagustin.myfood.project.backend.models.User;
import com.pabloagustin.myfood.project.backend.payload.*;
import com.pabloagustin.myfood.project.backend.repositories.DishRepository;
import com.pabloagustin.myfood.project.backend.repositories.UserRepository;
import com.pabloagustin.myfood.project.backend.services.UserService;
import com.pabloagustin.myfood.project.backend.services.dish.DishService;
import com.pabloagustin.myfood.project.backend.services.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class TestController {

	private final UserService userService;
	private final FileService fileService;
	private final UserRepository userRepository;
	private final DishService dishService;
	private final DishRepository dishRepository;

	@Value("${project.image}")
	private String path;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@PostMapping("/user/update/{userId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> updatingUser(@PathVariable Long userId, @RequestBody UpdateRequest updateRequest) {
		User userUpdating = userService.updateUser(userId, updateRequest);
		return new ResponseEntity<>(userUpdating, HttpStatus.OK);
	}

	@PostMapping("/user/{userId}/upload")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<FileResponse> fileUpload(
			@RequestParam("image") MultipartFile image,
			@PathVariable Long userId
	){
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()){
			User currentUser = user.get();
			String fileName = null;
			try {
				fileName = this.fileService.uploadImage(path + currentUser.getUsername() + "/" , image);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(new FileResponse(null, "Image is not updload due to error " +
						"on server"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(new FileResponse(fileName, "Image is successfully uploaded"), HttpStatus.OK);
		} else {
			throw new UsernameNotFoundException("User not found");
		}

	}

	@PutMapping("/user/{userId}/profile-image")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> updateUserProfileImage(@PathVariable Long userId,
	                                                @RequestParam("image") MultipartFile image){
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()){
			User currentUser = user.get();
			String fileName = null;
			try{
				fileName = this.fileService.uploadImage(path + currentUser.getUsername() + "/", image);
				currentUser.setProfileImage(fileName);

				userRepository.save(currentUser);
				return ResponseEntity.ok(new MessageResponse("Profile picture updated successfully"));
			} catch (IOException e){
				return new ResponseEntity<>(new FileResponse(null, "Image is not updload due to error " +
						"on server"), HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
		}
	}

	@PostMapping("/user/{userId}/create-dish")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> createNewUserDish(@PathVariable Long userId, @RequestBody Dish dish){
		dishService.createDish(userId, dish);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/user/{userId}/{dishId}/update-dish")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> updateUserDish(@PathVariable Long userId,
	                                        @PathVariable Long dishId,
	                                        @RequestBody DishRequest dishRequest){
		Optional<Dish> dish = dishRepository.findById(dishId);
		if (dish.isPresent()){
			Dish currentDish = dish.get();
			if (Objects.equals(currentDish.getUser().getId(), userId)){
				dishService.updateDish(userId, dishId, dishRequest);
				return new ResponseEntity<>("Dish updated successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("User id is not equal", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Dish not found", HttpStatus.FOUND);
		}
	}

	@DeleteMapping("/admin/delete-user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUserById(@PathVariable Long userId){
		userRepository.deleteById(userId);
		return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
	}
}
