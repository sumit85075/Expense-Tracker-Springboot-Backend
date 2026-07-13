package com.minku.expensetracker.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExpenseResponseDto {

	private Long id;
	private String title;
	private Double amount;
	private String category;
	private LocalDate expenseDate;

}
