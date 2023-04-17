package com.movierater.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.movierater.connection.ConnectionManager;
import com.movierater.model.User;

public class MovieRaterController {
	
	private static final Scanner input = new Scanner(System.in);
	private Connection conn = ConnectionManager.getConnection();
	private List<User> users = new ArrayList<>();
	
	public void run() {
		
		while (true) {
			getAllUsers();
			System.out.println("==========================");
			System.out.println("= Welcome to Movie Rater =");
			System.out.println("==========================");
			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("3. View Movies");
			System.out.println("4. Exit");
			System.out.print("Choose an option (1-4): ");
			int choice = getMenuChoice();
			System.out.println();
			
			if (choice == 1) {
				registerUser();
			}
			
			else if (choice == 2) {
				User curruser = login();
				session(curruser);
			}
			
			else if (choice == 3) {
				showMovies();
			}
			
			else if (choice == 4) {
				System.out.println("Have a great day!\n");
				return;
			}
		}
	}
	
	// OPTION 1 goes here
	public void registerUser() {
		
		String[] info = getInfo();
		
		String insertStmt = "INSERT INTO user VALUES(null, ?, ?);";
		try (PreparedStatement ps = conn.prepareStatement(insertStmt)) {
			
			ps.setString(1, info[0]);
			ps.setString(2, info[1]);
			ps.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("\nUser created!\n");
	}
	
	// OPTION 2 goes here
	public User login() {
		
		while (true) {
			
			System.out.println("LOGIN\n");
			System.out.print("Your email: ");
			String email = getEmail();
			System.out.print("Your password: ");
			String pass = getPassword();
			
			for (User u : users) {
				
				if (email.equals(u.getEmail()) && pass.equals(u.getPassword())) {
					System.out.println();
					return u;
				}
			}
			System.out.println("Invalid credentials. Try again.");
		}
	}
	
	// AND HERE....
	public void session(User user) {
		
		System.out.println("Welcome!\n");
		while (true) {
			System.out.println("===============");
			System.out.println("== Your Menu ==");
			System.out.println("===============");
			System.out.println("1. View Movie Info");
			System.out.println("2. Rate a movie");
			System.out.println("3. Delete rating");
			System.out.println("4. View your ratings");
			System.out.println("5. Logout");
			int option = getSessionMenuChoice();
			System.out.println();
			
			if (option == 1) {
				viewMovieInfo();
			}
			
			else if (option == 2) {
				rateMovie(user);
			}
			
			else if (option == 3) {
				deleteRating(user);
			}
			
			else if (option == 4) {
				showUserRatings(user);
			}
			else if (option == 5) {
				System.out.println("You're now logged out!\n");
				return;
			}
		}
	}

	public void showMovies() {
		System.out.println("\nMovie List");
		System.out.println("====================");
		
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM movie;");
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				System.out.println(rs.getString("title"));
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	// OPTION 1 here
	public void viewMovieInfo() {
		System.out.println("=================================");
		System.out.println("Movies (Avg Rating, # of Ratings)");
		System.out.println("=================================");
		
		String query = "SELECT movie, sum(rate) as 'Sum', count(movie) as 'Count'," +
					   "round((sum(rate) / count(movie)), 1) as 'Average' FROM rating " +
					   "GROUP BY movie;";
		try (PreparedStatement ps = conn.prepareStatement(query);
			 ResultSet rs = ps.executeQuery();) {
			
			while (rs.next()) {
				System.out.println(rs.getString("movie") + " (" + 
								   rs.getFloat("Average") + ", " +
								   rs.getInt("Count") + ")\n");
			}
			System.out.println();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// OPTION 2
	public void rateMovie(User user) {
		
		// display movies
		getAllMovies();
		
		// get the user's selection
		String movieChoice = getMovieOption();
		
		if (alreadyRated(movieChoice, user)) {
			changeRating(movieChoice, user);
		}
		else {
			System.out.println("Movie: " + movieChoice);
			System.out.println("0: Terrible");
			System.out.println("1: Meh");
			System.out.println("2: Ok-ish");
			System.out.println("3: Watchable");
			System.out.println("4: Great");
			System.out.println("5: Huge fan of it!");
			System.out.println("6: Exit");
			System.out.print("What would you rate " + movieChoice + "? (0-5): ");
			String rateChoice = input.nextLine();
			
			if (rateChoice.equals("6")) {
				System.out.println("Guess you'll rate it next time huh?\n");
				return;
			}
			
			while (!rateChoice.matches("^[0-5]$")) {
				System.out.println("Try again fam. That option isn't allowed.");
				System.out.print("What would you rate " + movieChoice + "? (0-5): ");
				rateChoice = input.nextLine();
			}
			
			String insertStmt = "INSERT INTO rating VALUES(null, ?, ?, ?);";
			try (PreparedStatement ps = conn.prepareStatement(insertStmt)) {
				
				ps.setInt(1, user.getId());
				ps.setString(2, movieChoice);
				ps.setInt(3, Integer.parseInt(rateChoice));
				ps.execute();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("Thank you for you rating!\n");
		}
		return;
	}
	
	public void showUserRatings(User user) {
		
		String getter = "SELECT * FROM rating WHERE user_id = ?;";
		System.out.println("====================");
		System.out.println("=== Your Ratings ===");
		System.out.println("====================");
		
		try (PreparedStatement ps = conn.prepareStatement(getter)) {
			
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				System.out.println(rs.getString("movie") + ": " + rs.getInt("rate"));
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public void deleteRating(User user) {
		
		System.out.print("Enter the name of the movie that you want to delete your rating: ");
		String name = input.nextLine();
		
		while (true) {
			
			try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM rating WHERE user_id = ?")) {
				
				ps.setInt(1, user.getId());
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					
					if (name.equals(rs.getString("movie"))) {
						
						String deleteStmt = "DELETE FROM rating WHERE user_id = ? AND movie = ?;";
						try (PreparedStatement ps2 = conn.prepareStatement(deleteStmt)) {
							
							ps2.setInt(1, user.getId());
							ps2.setString(2, name);
							ps2.execute();
							
							System.out.println("Your rating has been deleted successfully!\n");
							return;
						}
					}
				}
				System.out.println("That movie wasn't found from your list.");
				System.out.print("Enter the name of the movie that you want to delete your rating: ");
				name = input.nextLine();
			}
			
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	// HELPER FUNCTIONS
	public int getMenuChoice() {
		
		String tempChoice = input.nextLine();
		
		while (!tempChoice.matches("^[1-4]$")) {
			System.out.println("Invalid option. Try again.");
			System.out.print("Choose an option (1-4): ");
			tempChoice = input.nextLine();
		}
		
		return Integer.parseInt(tempChoice);
	}
	
	public void getAllUsers() {
		
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM user");
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				users.add(new User(rs.getInt("user_id"),rs.getString("email"), rs.getString("password")));
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getEmail() {
		
		String tempEmail = input.nextLine();
		
		while (!tempEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			System.out.println("Not a valid email. Try again.");
			System.out.println("Email: ");
			tempEmail = input.nextLine();
		}
		return tempEmail;
	}
	
	public String getPassword() {
		
		String tempPass = input.nextLine();
		
		while (!tempPass.matches("^.{8,}$")) {
			System.out.println("Invalid password format. Try again.");
			System.out.print("Password: ");
			tempPass = input.nextLine();
		}
		
		return tempPass;
	}
	
	public String[] getInfo() {
		
		String[] info = new String[2];
		System.out.println("Let's start by getting your email...");
		System.out.print("Enter your email: ");
		String tempEmail = input.nextLine();
		
		while (!tempEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			System.out.println("Not a valid email format. Try again boss.");
			System.out.print("Enter your email: ");
			tempEmail = input.nextLine();
		}
		info[0] = tempEmail;	// confirmed email
		
		System.out.print("Enter a password (must be at least 8 characters): ");
		String pass1 = input.nextLine();
		
		while (!pass1.matches("^.{8,}$")) {
			System.out.println("Not a valid password. Try again.");
			System.out.print("Enter a password (must be > 8 characters: ");
			pass1 = input.nextLine();
		}
		
		System.out.print("Confirm password: ");
		String pass2 = input.nextLine();
		
		while (!pass1.equals(pass2)) {
			System.out.println("Passwords don't match. Try again silly!");
			System.out.print("Enter a password (must be > 8 characters: ");
			pass1 = input.nextLine();
			
			while (!pass1.matches("^.{8,}$")) {
				System.out.println("Not a valid password. Try again.");
				System.out.print("Enter a password (must be > 8 characters: ");
				pass1 = input.nextLine();
			}
			
			System.out.print("Confirm password: ");
			pass2 = input.nextLine();
		}
		
		info[1] = pass1;
		
		return info;
	}
	
	public void getAllMovies() {
		
		System.out.println("======================");
		
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM movie;");
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				System.out.println(rs.getString("title"));
			}
		}
		
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println("======================");
	}
	
	public int getSessionMenuChoice() {
		
		System.out.print("Pick an option (1-5): ");
		String choice = input.nextLine();
		
		while (!choice.matches("^[1-5]$")) {
			System.out.println("Not a valid menu choice. Try again friend.");
			System.out.print("Pick an option (1-5): ");
			choice = input.nextLine();
		}
		
		return Integer.parseInt(choice);
	}
	
	public String getMovieOption() {
		
		System.out.print("Enter the title of the movie you want to rate: ");
		String temp = input.nextLine();
		
		while (true) {
			
			try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM movie;");
				 ResultSet rs = ps.executeQuery()) {
					
				while (rs.next()) {
					
					if (temp.equals(rs.getString("title"))) {
						return temp;
					}
				}
				System.out.println("Not a valid movie option. Try again.");
				System.out.print("Enter the title of the movie you want to rate: ");
				temp = input.nextLine();
			}
			
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String changeRateChoice() {
		
		String choice = input.nextLine();
		
		while (!choice.matches("^[yYnN]$")) {
			System.out.println("Not a valid choice pal. Try again.");
			System.out.print("Did you want to change your rating of it? (y/n): ");
			choice = input.nextLine();
		}
		return choice;
	}
	
	public boolean alreadyRated(String title, User user) {
		
		// get all movies the user has rated
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM rating WHERE user_id = ?;")) {
			
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				
				if (rs.getString("movie").equals(title)) {
					return true;
				}
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void changeRating(String title, User user) {
		
		System.out.println("You've already rated this movie silly goose...");
		System.out.print("Did you want to change it? (y/n): ");
		String choice = changeRateChoice();
		
		if (choice.equalsIgnoreCase("y")) {
			System.out.println("Movie: " + title);
			System.out.println("0: Terrible");
			System.out.println("1: Meh");
			System.out.println("2: Ok-ish");
			System.out.println("3: Watchable");
			System.out.println("4: Great");
			System.out.println("5: Huge fan of it!");
			System.out.println("6: Exit");
			
			if (choice.equals("6")) {
				System.out.println("Changed your mind huh? Okeydokey then...");
			}
			
			else {
				System.out.print("What rating do you give " + title + "? (0-5): ");
				int rate = getRate();
				
				String update = "UPDATE rating SET rate = ? WHERE user_id = ?;";
				try (PreparedStatement ps = conn.prepareStatement(update)) {
					
					ps.setInt(1, rate);
					ps.setInt(2, user.getId());
					ps.execute();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("Thank you for you rating!\n");
			}
		}
		else {
			System.out.println("Alrighty then. Returning you to previous menu....\n");
			return;
		}
	}
	
	public int getRate() {
		
		String temp = input.nextLine();
		
		while (!temp.matches("^[0-5]$")) {
			System.out.println("Invalid rating compadre. Give it another go...");
			System.out.println("What rating do you give the movie? (0-5): ");
			temp = input.nextLine();
		}
		return Integer.parseInt(temp);
	}
}


























