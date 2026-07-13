package com.minku.expensetracker.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.minku.expensetracker.dto.getUserResponseDTO;
import com.minku.expensetracker.entity.User;
import com.minku.expensetracker.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public String createNewUser(User user) {
		String email = user.getEmail();
		boolean isExist = userRepo.existsByEmail(email);

		if (isExist) {
			return "Email Already Exist";
		}

		String name = user.getName().trim().replaceAll("\\s+", " ");
		user.setName(name);

		String password = user.getPassword();
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$";

		if (!password.matches(regex)) {
			return "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character and be 8-12 characters long";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);
		return "Account Created Successfully";
	}

	public Map<String, Object> getUserDetails(long id) {
		Map<String, Object> userDetails = new HashMap<>();

		try {
			User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("ID does not exist"));

			getUserResponseDTO getResponseDTO = new getUserResponseDTO();
			getResponseDTO.setId(user.getId());
			getResponseDTO.setName(user.getName());
			getResponseDTO.setEmail(user.getEmail());
			// getResponseDTO.setRole(user.getRole());

			LocalDate createdDate = user.getCreatedDate();
			if (createdDate != null) {
				String date = createdDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				getResponseDTO.setCreatedDate(date);
			}

			userDetails.put("Status", 1);
			userDetails.put("User", getResponseDTO);
			return userDetails;

		} catch (RuntimeException e) {
			userDetails.put("Status", 0);
			userDetails.put("Exception", "ID does not exist");
			return userDetails;
		}
	}
}
