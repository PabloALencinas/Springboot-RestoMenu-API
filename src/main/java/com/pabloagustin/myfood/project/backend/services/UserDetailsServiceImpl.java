package com.pabloagustin.myfood.project.backend.services;

import com.pabloagustin.myfood.project.backend.models.User;
import com.pabloagustin.myfood.project.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(("User not found")));

		return UserDetailsImpl.build(user);
	}
}
