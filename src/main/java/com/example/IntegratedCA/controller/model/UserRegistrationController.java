package com.example.IntegratedCA.controller.model;

import java.util.ArrayList;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.IntegratedCA.Exceptions.*;
import com.example.IntegratedCA.controller.*;

@RestController
public class UserRegistrationController {

	private List<User> users;

	public UserRegistrationController() {
		users = new ArrayList<>();
	}

	@PostMapping("/users")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void registerUser(
			@RequestParam(required = true) String username,
			@RequestParam(required = true) String password) {
		
		if (password.length() < 8) {
			throw new BadRequestException("Password should be at least 8 characters");
			
		}
		
		if(!containsAtLeastOneSymbols(password)) {
			throw new BadRequestException("Password must contain at least 1 of the following symbols: #@{}[]");
		}
		if (userExists(username.toUpperCase())) {
			throw new BadRequestException("User already exists");
		}
		
		users.add(new User(username, password));
		
	}
	
	
	@PostMapping("/users2")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void registerUser2(@RequestBody User user) {
		if (user.getPassword() == null) {
			throw new BadRequestException("Password cannot be null");
		}
		if (user.getUsername() == null) {
			throw new BadRequestException("Username cannot be null");
		}
		registerUser(user.getUsername(), user.getPassword());
	}
	
	
	@GetMapping("/users") //Users check
	public List<User> getUsers() {

		return users;
	}
	
	
	private boolean userExists(String user) {
		for(User u :users) {
			if (u.getUsername().contentEquals(user)){	
				return true;
			}
	}
	return false;
	
	}
	
	private boolean containsAtLeastOneSymbols(String string) {
		String symbols = "#@{}[]*";
		
		if(string != null) {
			for (int i = 0; i < symbols.length(); i++){
				if (string.indexOf(symbols.charAt(i)) != -1) {
					return true;
				}
			}
		}
		return false;
	}
}
