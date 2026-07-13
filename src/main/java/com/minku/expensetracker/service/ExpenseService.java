package com.minku.expensetracker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minku.expensetracker.dto.ExpenseRequestDto;
import com.minku.expensetracker.entity.Budget;
import com.minku.expensetracker.entity.Expense;
import com.minku.expensetracker.entity.User;
import com.minku.expensetracker.repository.BudgetRepository;
import com.minku.expensetracker.repository.ExpenseRepository;
import com.minku.expensetracker.repository.UserRepository;

@Service
@Transactional
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final UserRepository userRepo;
	private final BudgetRepository budgetRepo;

	public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepo, BudgetRepository budgetRepo) {

		this.expenseRepository = expenseRepository;
		this.userRepo = userRepo;
		this.budgetRepo = budgetRepo;
	}

	// Add Expense
	public Map<String, Object> addExpense(ExpenseRequestDto request) {

		Map<String, Object> response = new HashMap<>();

		try {

			User user = userRepo.findById(request.getUserId())
					.orElseThrow(() -> new RuntimeException("User Not Found"));

			Budget budget = budgetRepo.findByUserId(user.getId())
					.orElseThrow(() -> new RuntimeException("Budget Not Found"));

			double updatedSpent = budget.getSpentAmount() + request.getAmount();

			if (updatedSpent > budget.getMonthlyLimit()) {
				response.put("Status", 0);
				response.put("Error", "Expense exceeds monthly budget.");
				return response;
			}

			Expense expense = new Expense();
			expense.setTitle(request.getTitle());
			expense.setAmount(request.getAmount());
			expense.setCategory(request.getCategory());
			expense.setExpenseDate(request.getExpenseDate());
			expense.setUser(user);

			Expense savedExpense = expenseRepository.save(expense);

			budget.setSpentAmount(updatedSpent);
			budgetRepo.save(budget);

			response.put("Status", 1);
			response.put("Message", "Expense Added Successfully");
			response.put("Data", savedExpense);

		} catch (RuntimeException e) {

			response.put("Status", 0);
			response.put("Error", e.getMessage());
		}

		return response;
	}

	// Get All Expenses
	public Map<String, Object> getAllExpenses() {

		Map<String, Object> response = new HashMap<>();

		List<Expense> expenses = expenseRepository.findAll();

		if (expenses.isEmpty()) {
			response.put("Status", 0);
			response.put("Error", "No Expense Record Found");
		} else {
			response.put("Status", 1);
			response.put("Data", expenses);
		}

		return response;
	}

	// Get Expense By Id
	public Map<String, Object> getExpenseById(Long id) {

		Map<String, Object> response = new HashMap<>();

		try {

			Expense expense = expenseRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Expense Not Found"));

			response.put("Status", 1);
			response.put("Data", expense);

		} catch (RuntimeException e) {

			response.put("Status", 0);
			response.put("Error", e.getMessage());
		}

		return response;
	}

	// Update Expense
	public Map<String, Object> updateExpense(Long id, ExpenseRequestDto request) {

		Map<String, Object> response = new HashMap<>();

		try {

			Expense expense = expenseRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Expense Not Found"));

			Budget budget = budgetRepo.findByUserId(expense.getUser().getId())
					.orElseThrow(() -> new RuntimeException("Budget Not Found"));

			double oldAmount = expense.getAmount();
			double newAmount = request.getAmount();

			double updatedSpent = budget.getSpentAmount() - oldAmount + newAmount;

			if (updatedSpent > budget.getMonthlyLimit()) {
				response.put("Status", 0);
				response.put("Error", "Expense exceeds monthly budget.");
				return response;
			}

			expense.setTitle(request.getTitle());
			expense.setAmount(newAmount);
			expense.setCategory(request.getCategory());
			expense.setExpenseDate(request.getExpenseDate());

			Expense updatedExpense = expenseRepository.save(expense);

			budget.setSpentAmount(updatedSpent);
			budgetRepo.save(budget);

			response.put("Status", 1);
			response.put("Message", "Expense Updated Successfully");
			response.put("Data", updatedExpense);

		} catch (RuntimeException e) {

			response.put("Status", 0);
			response.put("Error", e.getMessage());
		}

		return response;
	}

	// Delete Expense
	public Map<String, Object> deleteExpense(Long id) {

		Map<String, Object> response = new HashMap<>();

		try {

			Expense expense = expenseRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Expense Not Found"));

			Budget budget = budgetRepo.findByUserId(expense.getUser().getId())
					.orElseThrow(() -> new RuntimeException("Budget Not Found"));

			double reducedAmount = budget.getSpentAmount() - expense.getAmount();

			budget.setSpentAmount(Math.max(reducedAmount, 0.0));
			budgetRepo.save(budget);

			expenseRepository.delete(expense);

			response.put("Status", 1);
			response.put("Message", "Expense Deleted Successfully");

		} catch (RuntimeException e) {

			response.put("Status", 0);
			response.put("Error", e.getMessage());
		}

		return response;
	}
}