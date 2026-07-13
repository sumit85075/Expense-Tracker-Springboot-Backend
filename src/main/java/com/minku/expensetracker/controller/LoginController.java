package com.minku.expensetracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minku.expensetracker.dto.ChangePasswordDto;
import com.minku.expensetracker.dto.LoginRequestDTO;
import com.minku.expensetracker.service.LoginService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {

	private final LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@PostMapping("/login-user")
	public ResponseEntity<?> loginPage(@Valid @RequestBody LoginRequestDTO loginRequest, BindingResult result) {
		Map<String, String> response = new HashMap<>();

		if (result.hasErrors()) {
			response.put("Status", "0");
			response.put("Message", result.getFieldError().getDefaultMessage());
			return ResponseEntity.badRequest().body(response);
		}

		response = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto request, BindingResult result) {
		Map<String, String> response = new HashMap<>();
		if (result.hasErrors()) {
			response.put("Status", "0");
			response.put("Message", result.getFieldError().getDefaultMessage());
			return ResponseEntity.badRequest().body(response);
		}

		response = loginService.changePassword(request.getEmail(), request.getNewPassword(),
				request.getConfirmPassword());

		return ResponseEntity.ok(response);
	}
}
