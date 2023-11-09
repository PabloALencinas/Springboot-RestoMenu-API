package com.pabloagustin.myfood.project.backend.controllers;

import com.pabloagustin.myfood.project.backend.models.Dish;
import com.pabloagustin.myfood.project.backend.models.ERole;
import com.pabloagustin.myfood.project.backend.models.Role;
import com.pabloagustin.myfood.project.backend.models.User;
import com.pabloagustin.myfood.project.backend.payload.LoginRequest;
import com.pabloagustin.myfood.project.backend.payload.MessageResponse;
import com.pabloagustin.myfood.project.backend.payload.SignupRequest;
import com.pabloagustin.myfood.project.backend.repositories.RoleRepository;
import com.pabloagustin.myfood.project.backend.repositories.UserRepository;
import com.pabloagustin.myfood.project.backend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthService authService;
	@Value("${project.image}")
	private String path;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
		return authService.login(loginRequest);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
		if (userRepository.existsByEmail(signupRequest.getUsername())){
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username already taken"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())){
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use"));
		}


		// Creating new user's account
		User user = new User(signupRequest.getUsername(),
				signupRequest.getEmail(),
				passwordEncoder.encode(signupRequest.getPassword()),
				signupRequest.getProfileImage()
				);

		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if(strRoles == null){
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
						roles.add(adminRole);
						break;
					case "mod":
						Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
						roles.add(modRole);
						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		// Creating a folder for the user. To upload files
		String username = signupRequest.getUsername();
		String userFolderPath = String.valueOf(Paths.get(path, username));

		if (!Files.exists(Path.of(userFolderPath))){
			try{
				Files.createDirectories(Path.of(userFolderPath));
			} catch (IOException e){
				return ResponseEntity.badRequest().body(new MessageResponse("Error creating user folder."));
			}
		}

		return ResponseEntity.ok(new MessageResponse("User registered successfully"));
	}
}
