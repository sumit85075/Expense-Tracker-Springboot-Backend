package com.minku.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minku.expensetracker.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	public User findByEmail(String email);

}
