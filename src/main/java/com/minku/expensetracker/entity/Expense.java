package com.minku.expensetracker.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Title is required")
	@Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
	@Column(nullable = false, length = 100)
	private String title;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be greater than 0")
	@Column(nullable = false)
	private Double amount;

	@NotBlank(message = "Category is required")
	@Column(nullable = false, length = 50)
	private String category;

	@NotNull(message = "Expense date is required")
	@PastOrPresent(message = "Expense date cannot be in the future")
	@Column(nullable = false)
	private LocalDate expenseDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@NotNull(message = "User is required")
	private User user;
}
