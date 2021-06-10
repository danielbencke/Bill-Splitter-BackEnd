package com.example.IntegratedCA.controller;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.IntegratedCA.Exceptions.BadRequestException;
import com.example.IntegratedCA.Exceptions.UnauthorizedRequestException;
import com.example.IntegratedCA.controller.model.*;
import com.example.IntegratedCA.util.*;
import com.fasterxml.jackson.annotation.JsonInclude;


import io.jsonwebtoken.Claims;

/**
 * 
 * @author daniel API development for a mobile application. The Trip Bill
 *         Splitter App uses React Native framework and have the following
 *         functions: - Login - Add Expenses - Close/ Open trip - Display
 *         Summary Expenses
 *
 */
@CrossOrigin("*")
@RestController
public class MainController {

	private Map<String, ArrayList<Item>> trips;
	public boolean info = true;
	public Map<String, ArrayList<Double>> expByUser;
	public List<User> listUsers;
	public ArrayList<Double> expenses = new ArrayList<Double>();
	public List<String> exp;
	String calcExpenses;

	public MainController() {
		trips = new HashMap<>();
		expByUser = new HashMap<>();
		listUsers = new ArrayList<>();
		listUsers.add(new User("GREG", "SECRET"));
		listUsers.add(new User("AMILCAR", "SECRET"));
		listUsers.add(new User("DAVID", "SECRET"));

	}

	/**
	 * @param username
	 * @param password
	 * @return JWT Token or Unauthorized Acess
	 */
	@CrossOrigin("*")
	@GetMapping("/login") // Username and password
	public String login(@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "password", required = true) String password) {

		for (User u : listUsers) {
			if (u.getUsername().contentEquals(username.toUpperCase()) && u.getPassword().contentEquals(password.toUpperCase())) {
				return JWTIssuer.createJWT(username, "ca-showcase", username, 86400000);
			}

		}

		throw new UnauthorizedRequestException("Unknown user"); // status code 401

	}

	/**
	 * 
	 * @param trip  = "daniel-trip"
	 * @param token
	 * @param item  = RequestBody - object: (name and price)
	 * @return trips HashMap with user and Item added
	 */
	@PostMapping("/{trip}/expense") // Authorization Bearer <token>
	public Map<String, ArrayList<Item>> addExpense(@PathVariable("trip") String trip,

			@RequestHeader(name = "Authorization", required = true) String token,
			@RequestBody(required = true) Item item) {

		if (info == false) { // check if the trip is opened to add expenses
			throw new BadRequestException("This trip is closed. Expenses cannot be added"); // status code 400
		}

		Claims claims = JWTIssuer.decodeJWT(token.split(" ")[1]); // Create JWT TOKEN and extract "subClaim"(user)
																	// information from it, add into Item object
		String subClaim = claims.get("sub", String.class);

		item.setUsername(subClaim);

		if (trips.get(trip) == null) {// If trip has still being not created, create a new one into "Trips" HashMap
			trips.put(trip, new ArrayList<Item>());

		}

		if (expByUser.get(subClaim) == null) { // If trip has still being not created, create a new one into "Trips"
												// HashMap
			expByUser.put(subClaim, new ArrayList<Double>());

		}

		trips.get(trip).add(item); // add trip and item object into "Trips" HashMap
		expenses.add(item.getPrice()); // add value from item object into expenses list

		expByUser.get(subClaim).add(item.getPrice()); // add into "expByUser" HashMap : currently user logged in and
														// price from Item object created.

		return trips;
	}

	/**
	 * 
	 * @param trip
	 * @return all items added in this trip including user information
	 */
	@GetMapping("/{trip}")
	public List<Item> trip(@PathVariable("trip") String trip) {
		return trips.get(trip);

	}

	/**
	 * Close the trip, no more expenses can be added into the HashMap
	 * 
	 * @param trip
	 */
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PostMapping("/{trip}/close")
	public void closeTrip(@PathVariable("trip") String trip) {
		info = false;
	}

	/**
	 * Reopen the trip, expenses can be added again into the HashMap
	 * 
	 * @param trip
	 */
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PostMapping("/{trip}/open")
	public void openTrip(@PathVariable("trip") String trip) {
		info = true;
	}

	/**
	 * Create SummaryObject for user logged in,
	 * 
	 * @param user
	 * @return Object for display summary expenses
	 */
	@GetMapping("summary/{user}")
	public SummaryObject ExpenseUser(@PathVariable("user") String user) {
		SummaryObject objectTest = new SummaryObject(user, expByUser.get(user), expByUser.size(), expenses,
				calcExpenses);
		return objectTest;
	}

	/**
	 * 
	 * @return expByUser HashMap with key= users, list: prices
	 */
	@GetMapping("/summary")
	public Map<String, ArrayList<Double>> getResponse2() {
		return expByUser;
	}

	/**
	 * 
	 * @return the sum of all expenses added
	 */
	@GetMapping("{trip}/summary")
	public double getSummary() {
		int i = 0;
		double sum = 0;
		while (i < expenses.size()) {
			sum = expenses.get(i) + sum;
			i++;
		}
		return sum;
	}
	
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PostMapping("/{trip}/clear")
	public void clear(@RequestHeader(name = "Authorization", required = true) String token) {
		
		 expByUser.clear();
		 trips.clear();
		 expenses.clear();
		
	}
	

}
