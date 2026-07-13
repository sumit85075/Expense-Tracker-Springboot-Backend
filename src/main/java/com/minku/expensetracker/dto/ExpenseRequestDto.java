package com.minku.expensetracker.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ExpenseRequestDto {

	@NotBlank(message = "Title is required")
	private String title;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be greater than 0")
	private Double amount;

	@NotBlank(message = "Category is required")
	private String category;

	@NotNull(message = "User id is required")
	private Long userId;

	@NotNull(message = "Expense date is required")
	@PastOrPresent(message = "Expense date cannot be in the future")
	private LocalDate expenseDate;
}
