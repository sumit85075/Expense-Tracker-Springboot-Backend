package com.minku.expensetracker.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.minku.expensetracker.JWT.JWTService;
import com.minku.expensetracker.entity.User;
import com.minku.expensetracker.repository.UserRepository;

@Service
public class LoginService {

	private final UserRepository userRepo;
	private final JWTService jwtService;
	private final PasswordEncoder passwordEncoder;

	public LoginService(UserRepository userRepo, JWTService jwtService, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}

	public Map<String, String> login(String email, String password) {
		Map<String, String> holdToken = new HashMap<>();
		User user = userRepo.findByEmail(email);

		if (user == null) {
			holdToken.put("Message", "User not found");
			holdToken.put("Status", "0");
			return holdToken;
		}

		if (!passwordEncoder.matches(password, user.getPassword())) {
			holdToken.put("Message", "Invalid Password");
			holdToken.put("Status", "0");
			return holdToken;
		}

		String token = jwtService.generateToken(user);
		holdToken.put("Token", token);
		holdToken.put("Status", "1");
		return holdToken;
	}

	// Update Password
	public Map<String, String> changePassword(String email, String newPassword, String confirmPassword) {

		Map<String, String> response = new HashMap<>();

		User user = userRepo.findByEmail(email);

		if (user == null) {
			response.put("Status", "0");
			response.put("Message", "Invalid Email");
			return response;
		}

		if (!newPassword.equals(confirmPassword)) {
			response.put("Status", "0");
			response.put("Message", "New Password and Confirm Password do not match");
			return response;
		}

		user.setPassword(passwordEncoder.encode(newPassword));

		userRepo.save(user);

		response.put("Status", "1");
		response.put("Message", "Password changed successfully");

		return response;
	}
}
