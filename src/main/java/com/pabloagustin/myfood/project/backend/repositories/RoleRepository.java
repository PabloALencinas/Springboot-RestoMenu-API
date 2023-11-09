package com.pabloagustin.myfood.project.backend.repositories;

import com.pabloagustin.myfood.project.backend.models.ERole;
import com.pabloagustin.myfood.project.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByName(ERole name);
}
