package com.minku.expensetracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minku.expensetracker.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

	Optional<Budget> findByUserId(Long userId);

}