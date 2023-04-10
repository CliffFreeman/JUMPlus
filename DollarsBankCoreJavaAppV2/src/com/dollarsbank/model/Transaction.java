package com.dollarsbank.model;

import java.sql.Timestamp;

public class Transaction {
	
	private String type;
	private float amount;
	private Timestamp time;
	
	
	public Transaction(String type, float amount, Timestamp time) {
		super();
		this.type = type;
		this.amount = amount;
		this.time = time;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public float getAmount() {
		return amount;
	}


	public void setAmount(float amount) {
		this.amount = amount;
	}


	public Timestamp getTime() {
		return time;
	}


	public void setTime(Timestamp time) {
		this.time = time;
	}


	@Override
	public String toString() {
		return "Transaction [type=" + type + ", amount=" + amount + ", time=" + time + "]";
	}
}
