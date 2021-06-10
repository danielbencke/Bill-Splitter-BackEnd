package com.example.IntegratedCA.controller.model;

public class Item {

	private String name;
	private double price;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Item() {

	}

	public Item(String name, double price, String username) {

		this.name = name;
		this.price = price;
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
