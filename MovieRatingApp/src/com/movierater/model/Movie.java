package com.movierater.model;

public class Movie {
	
	private String title;

	
	public Movie(String title) {
		super();
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Movie [title=" + title + "]";
	}
}
