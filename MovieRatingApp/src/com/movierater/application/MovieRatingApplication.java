package com.movierater.application;

import com.movierater.controller.MovieRaterController;

public class MovieRatingApplication {
	
	static MovieRaterController app = new MovieRaterController();

	public static void main(String[] args) {
		app.run();
	}

}