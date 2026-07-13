package com.minku.expensetracker.dto;

import lombok.Data;

@Data
public class getUserResponseDTO {

	private long id;
	private String name;
	private String email;
	// private String role;
	private String createdDate;

}
