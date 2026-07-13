package com.minku.expensetracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minku.expensetracker.entity.User;
import com.minku.expensetracker.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/create-user")
	public ResponseEntity<?> createAccount(@Valid @RequestBody User user, BindingResult result) {

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			String message = result.getFieldError().getDefaultMessage();
			response.put("Status", "0");
			response.put("Message", message);
			return ResponseEntity.badRequest().body(response);
		}

		String serviceMsg = userService.createNewUser(user);

		if (serviceMsg.contains("Successful")) {
			response.put("Status", "1");
			response.put("Message", serviceMsg);
		} else {
			response.put("Status", "0");
			response.put("Message", serviceMsg);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-user-details/{id}")
	public ResponseEntity<?> getUserDetails(@PathVariable long id) {
		Map<String, Object> response = userService.getUserDetails(id);
		return ResponseEntity.ok(response);
	}
}
