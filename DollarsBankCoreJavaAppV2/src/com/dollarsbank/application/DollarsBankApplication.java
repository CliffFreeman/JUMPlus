package com.dollarsbank.application;

import com.dollarsbank.controller.DollarsBankController;

public class DollarsBankApplication {
	
	static DollarsBankController app = new DollarsBankController();

	public static void main(String[] args) {
		app.run();
	}

}
