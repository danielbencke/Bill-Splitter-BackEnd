package com.example.IntegratedCA.controller.model;


import java.util.ArrayList;
import java.util.List;

public class SummaryObject {

	private String name;
	private ArrayList<Double> expensesO;
	private int numberofUsers;
	private ArrayList<Double> expensesSum;
	private String calcExpenses;

	public SummaryObject(String name, ArrayList<Double> expensesO, int numberofUsers, ArrayList<Double> expensesSum,
			String calcExpenses) {
	
		this.name = name;
		this.expensesO = expensesO;
		this.numberofUsers = numberofUsers;
		this.expensesSum = expensesSum;
		this.calcExpenses = calcExpenses;

	}

	public String getCalcExpenses() {

		double averageExp = setExpensesSum(expensesSum) / getNumberofUsers();

		double userSumTotal = setExpensesO(expensesO);

		double dif = userSumTotal - averageExp;
		String status = "";

		if (dif < 0) {
			status = " you owe to the group the amount of " + Math.round(((Math.abs(dif))*100)/100);

		} else if (dif == 0) {
			status = " your balance is Ok";

		} else {

			status = " you receive from the group the amount of " + Math.round(((Math.abs(dif))*100)/100);
		}
		return status;
	}

	public void setCalcExpenses(String calcExpenses) {
		this.calcExpenses = calcExpenses;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Double> getExpensesO() {
		return expensesO;
	}

	public double setExpensesO(ArrayList<Double> expensesO) { // sum of user expenses

		int i = 0;
		double sum = 0;
		while (i < expensesO.size()) {
			sum = expensesO.get(i) + sum;
			i++;
		}
		return sum;
	}

	public int getNumberofUsers() {
		return numberofUsers;
	}

	public void setNumberofUsers(int numberUsers) {
		this.numberofUsers = numberUsers;
	}

	public ArrayList<Double> getExpensesSum() {
		return expensesSum;
	}

	public double setExpensesSum(ArrayList<Double> expensesSum) { // sum of total expenses for all users

		int i = 0;
		double sum = 0;
		while (i < expensesSum.size()) {
			sum = expensesSum.get(i) + sum;
			i++;
		}
		return sum;
	}

}