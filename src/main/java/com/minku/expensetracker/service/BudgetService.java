package com.minku.expensetracker.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.minku.expensetracker.entity.Budget;
import com.minku.expensetracker.entity.User;
import com.minku.expensetracker.repository.BudgetRepository;
import com.minku.expensetracker.repository.UserRepository;

@Service
public class BudgetService {

	private final BudgetRepository budgetRepo;
	private final UserRepository userRepo;

	public BudgetService(BudgetRepository budgetRepo, UserRepository userRepo) {

		this.budgetRepo = budgetRepo;
		this.userRepo = userRepo;
	}

	// Add Budget
	public Map<String, Object> addBudget(Long userId, Budget budget) {

		Map<String, Object> response = new HashMap<>();

		// Check User
		User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// Check Budget Already Exists
		if (budgetRepo.findByUserId(userId).isPresent()) {
			response.put("Status", 0);
			response.put("Message", "Budget already exists.");
			return response;
		}

		// Set User
		budget.setUser(user);

		// Default Spent Amount
		if (budget.getSpentAmount() == null) {
			budget.setSpentAmount(0.0);
		}

		// Save Budget
		Budget savedBudget = budgetRepo.save(budget);

		response.put("Status", 1);
		response.put("Message", "Budget Added Successfully");
		response.put("Data", savedBudget);

		return response;
	}

	// Get Budget
	public Map<String, Object> getBudgetByUserId(Long userId) {

		Map<String, Object> response = new HashMap<>();

		Budget budget = budgetRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Budget not found"));

		response.put("Status", 1);
		response.put("Data", budget);

		return response;
	}
}