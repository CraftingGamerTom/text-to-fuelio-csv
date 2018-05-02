/**
 * Copyright (c) 2018 Thomas Rokicki
 */

package entities;

public class GasData {

	private String date;
	private double money;
	private double gallons;
	private int mileage;
	private boolean isFull;

	public GasData() {
		this.date = "0000-00-00";
		this.money = -1;
		this.gallons = -1;
		this.mileage = -1;
		this.isFull = true;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getGallons() {
		return gallons;
	}

	public void setGallons(double gallons) {
		this.gallons = gallons;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

}
