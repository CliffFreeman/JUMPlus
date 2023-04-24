package com.gradebook.application;

import com.gradebook.controller.GradeBookController;

public class GradeBookApplication {
	static GradeBookController app = new GradeBookController();
	
	public static void main(String[] args) {
		app.run();
	}
}
