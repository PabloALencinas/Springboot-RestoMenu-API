package com.pabloagustin.myfood.project.backend.services;

import com.pabloagustin.myfood.project.backend.models.User;
import com.pabloagustin.myfood.project.backend.payload.UpdateRequest;
import com.pabloagustin.myfood.project.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public User updateUser(Long userId, UpdateRequest updateRequest){
		Optional<User> user = userRepository.findById(userId);

		if (user.isPresent()){
			User currentUser = user.get();
			currentUser.setUsername(updateRequest.getUsername());
			currentUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

			return userRepository.save(currentUser);

		} else {
			throw new UsernameNotFoundException("User not found");
		}
	}

}
