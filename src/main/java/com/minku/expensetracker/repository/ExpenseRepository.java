package com.minku.expensetracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minku.expensetracker.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByUserId(Long userId);

	List<Expense> findByUserIdAndCategory(Long userId, String category);

	List<Expense> findByUserIdAndExpenseDate(Long userId, LocalDate expenseDate);
}