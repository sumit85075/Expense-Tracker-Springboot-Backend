package com.minku.expensetracker.JWT;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.minku.expensetracker.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	private static final String SECRET = "mysecretkeymysecretkeymysecretkey123";
	private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

	public String generateToken(User user) {
		System.out.println("Generating token............");
		return Jwts.builder().subject(user.getEmail()).claim("id", user.getId()).claim("name", user.getName())
				.claim("role", user.getRole()).claim("email", user.getEmail()).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 Hour for ease of testing
				.signWith(key).compact();
	}

	public Claims extractClaims(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}

	public String extractEmail(String token) {
		return extractClaims(token).getSubject();
	}

	public boolean validateToken(String token) {
		extractClaims(token);
		return true;
	}
}
