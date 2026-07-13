package com.minku.expensetracker.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.minku.expensetracker.entity.Budget;
import com.minku.expensetracker.service.BudgetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/budget")
public class BudgetController {

	private final BudgetService budgetService;

	public BudgetController(BudgetService budgetService) {
		this.budgetService = budgetService;
	}

	// Add Budget
	@PostMapping("/add-budget/{userId}")
	public ResponseEntity<Map<String, Object>> addBudget(@PathVariable Long userId, @Valid @RequestBody Budget budget) {

		return ResponseEntity.ok(budgetService.addBudget(userId, budget));
	}

	// Get Budget By User ID
	@GetMapping("/get-budget/{userId}")
	public ResponseEntity<Map<String, Object>> getBudgetByUserId(@PathVariable Long userId) {

		return ResponseEntity.ok(budgetService.getBudgetByUserId(userId));
	}

}