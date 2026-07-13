package com.minku.expensetracker.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.minku.expensetracker.dto.ExpenseRequestDto;
import com.minku.expensetracker.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/expense-api")
public class ExpenseController {

	private final ExpenseService expenseService;

	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	@PostMapping("/add")
	public ResponseEntity<?> addExpense(@Valid @RequestBody ExpenseRequestDto request, BindingResult result) {

		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(Map.of("Status", 0, "Error", result.getFieldError().getDefaultMessage()));
		}

		return ResponseEntity.ok(expenseService.addExpense(request));
	}

	@GetMapping("/get-all-expense")
	public ResponseEntity<?> getAllExpenses() {
		return ResponseEntity.ok(expenseService.getAllExpenses());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getExpenseById(@PathVariable Long id) {
		return ResponseEntity.ok(expenseService.getExpenseById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequestDto request,
			BindingResult result) {

		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(Map.of("Status", 0, "Error", result.getFieldError().getDefaultMessage()));
		}

		return ResponseEntity.ok(expenseService.updateExpense(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
		return ResponseEntity.ok(expenseService.deleteExpense(id));
	}
}