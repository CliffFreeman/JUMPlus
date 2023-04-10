package com.dollarsbank.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dollarsbank.connection.ConnectionManager;
import com.dollarsbank.model.User;
import com.dollarsbank.utility.Colors;

public class DollarsBankController {
	
	Colors c = new Colors();
	static Scanner s = new Scanner(System.in);
	private Connection conn = ConnectionManager.getConnection();
	public List<User> users = new ArrayList<>();
	
	public void run() {
		
		System.out.println(c.GREEN + "$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println(c.GREEN + "$ Welcome to Dollars Bank! $");
		System.out.println(c.GREEN + "$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + c.RESET);
		
		while (true) {
			
			users = getAllUsers();
			System.out.println(c.CYAN + "1. Create new account" + c.RESET);
			System.out.println(c.YELLOW + "2. Login" + c.RESET);
			System.out.println(c.RED + "3. Exit" + c.RESET);
			System.out.print("Pick an option (1, 2, or 3): ");
			int choice = getMenuChoice();
			System.out.println();
			
			if (choice == 1) {
				createNewUser();
			}
			
			else if (choice == 2) {
				User curruser = login();
				session(curruser);
			}
			
			else if (choice == 3) {
				System.out.println(c.CYAN + "Have a great day!\n" + c.RESET);
				s.close();
				return;
			}
		}
	}
	
	
	public void createNewUser() {
		
		System.out.println(c.GREEN + "Let's create an account!" + c.RESET);
		System.out.println();
		String[] names = getNames();
		System.out.println();
		String username = getUsername();
		System.out.println();
		String password = getPassword();
		System.out.println();
		float initialDeposit = getInitDeposit();
		System.out.println();
		
		String statement = "INSERT INTO user VALUES(null, ?, ?, ?, ?, ?);";
		
		try (PreparedStatement ps = conn.prepareStatement(statement)) {
			
			ps.setString(1, names[0]);
			ps.setString(2, names[1]);
			ps.setString(3, username);
			ps.setString(4, password);
			ps.setFloat(5, initialDeposit);
			
			ps.execute();
			
			String statement2 = "SELECT * FROM user ORDER BY account_id DESC LIMIT 1";
			try (PreparedStatement ps2 = conn.prepareStatement(statement2);
				 ResultSet rs = ps2.executeQuery();) {
				
				int acct = 0;
				
				while (rs.next()) {
					acct = rs.getInt("account_id");
				}
				
				
				String statement3 = "INSERT INTO transaction VALUES(null, ?, ?, ?, ?);";
				
				try (PreparedStatement ps3 = conn.prepareStatement(statement3)) {
					
					ps3.setInt(1, acct);
					ps3.setString(2, "Initial Deposit");
					ps3.setFloat(3, initialDeposit);
					ps3.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
					
					ps3.execute();
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(c.GREEN + "User created!" + c.RESET + "\n");
	}
	
	
	public User login() {
		
		System.out.println("LOGIN\n");
		while (true) {
			
			System.out.print("Username: ");
			String un = s.nextLine();
			System.out.print("Password: ");
			String pw = s.nextLine();
			
			for (User usr : users) {
				
				if (usr.getUsername().equals(un) && usr.getPassword().equals(pw)) {
					System.out.println();
					System.out.println(c.GREEN + "Welcome " + usr.getFirstName() + "!" + c.RESET);
					return usr;
				}
			}
			System.out.println(c.RED + "Invalid credentials. Try again.\n" + c.RESET);
		}
	}
	
	
	public void session(User user) {
		
		while (true) {
			
			System.out.println(c.CYAN + "===================");
			System.out.println("==== Main Menu ====");
			System.out.println("===================" + c.RESET);
			System.out.println(c.GREEN + "1. Deposit" + c.RESET);
			System.out.println(c.RED + "2. Withdrawal" + c.RESET);
			System.out.println(c.YELLOW + "3. View account info" + c.RESET);
			System.out.println(c.CYAN + "4. View 5 most recent transactions" + c.RESET);
			System.out.println(c.MAGENTA + "5. Logout\n" + c.RESET);
			int choice = getSessionMenuChoice();
			System.out.println();
			
			// for deposits
			if (choice == 1) {
				
				float amount = getAmount("D", user);
				
				String insertStmt = "INSERT INTO transaction VALUES(null, ?, ?, ?, ?);";
				
				// insert transaction into transaction table
				try (PreparedStatement ps = conn.prepareStatement(insertStmt)) {
					
					ps.setInt(1, user.getAccountNumber());
					ps.setString(2, "Deposit");
					ps.setFloat(3, amount);
					ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
					
					ps.execute();
					
					// retrieve the user's non-updated balance
					float oldBalance = getCurrentBalance(user);
					
					// update balance with deposit
					setNewBalance(user, oldBalance, amount);
					
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
				
				System.out.println(c.GREEN + "Deposit of $ " + amount + " made successfully!\n" + c.RESET);
			}
			
			else if (choice == 2) {
				
				float amount = getAmount("W", user);
				float oldBal = getCurrentBalance(user);
				
				// check for overdraft
				while (amount > oldBal) {
					
					System.out.println(c.RED + "This results in a negative balance. Cannot process. Try again." + c.RESET);
					amount = getAmount("W", user);
				}
				
				String insertStmt = "INSERT INTO transaction VALUES(null, ?, ?, ?, ?);";
				
				// insert transaction into transaction table
				try (PreparedStatement ps = conn.prepareStatement(insertStmt)) {
					
					ps.setInt(1, user.getAccountNumber());
					ps.setString(2, "Withdrawal");
					ps.setFloat(3, -amount);
					ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
					
					ps.execute();
					
					// retrieve the user's non-updated balance
					float oldBalance = getCurrentBalance(user);
					
					// update balance with deposit
					setNewBalance(user, oldBalance, -amount);
					
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
				
				System.out.println(c.GREEN + "Withdrawal of $ " + amount + " made successfully!\n" + c.RESET);
			}
			
			else if (choice == 3) {
				
				System.out.println(c.GREEN + "=================");
				System.out.println("=== Your Info ===");
				System.out.println("=================\n" + c.RESET);
				
				String acct = "SELECT * FROM user WHERE account_id = ?";
				
				try (PreparedStatement ps = conn.prepareStatement(acct)) {
					
					ps.setInt(1, user.getAccountNumber());
					ResultSet rs = ps.executeQuery();
					
					while (rs.next()) {
						String name = rs.getString("first_name") + " " + rs.getString("last_name");
						int num = rs.getInt("account_id");
						float bal = rs.getFloat("balance");
						
						System.out.println(c.CYAN + "Name: " + c.RESET + name);
						System.out.println(c.CYAN + "Account #: " + c.RESET + num);
						System.out.println(c.CYAN + "Balance: " + c.RESET + "$ " + bal);
						System.out.println();
					}
					
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			else if (choice == 4) {
				
				String transQuery = "SELECT * FROM transaction WHERE account_id = ? ORDER BY time DESC LIMIT 5";
				
				try (PreparedStatement ps = conn.prepareStatement(transQuery)) {
					
					ps.setInt(1, user.getAccountNumber());
					ResultSet rs = ps.executeQuery();
					
					while (rs.next()) {
						
						String type = rs.getString("type");
						float amount = rs.getFloat("amount");
						Timestamp time = rs.getTimestamp("time");
						
						if (type.equals("Initial Deposit")) {
							System.out.println("[ " + time + " ] " + c.GREEN + "INITIAL DEPOSIT " + c.RESET +
											   "of " + c.GREEN + "$ " + amount + c.RESET);
						}
						else if (type.equals("Deposit")) {
							System.out.println("[ " + time + " ] " + c.GREEN + "DEPOSIT " + c.RESET +
											   "of " + c.GREEN + "$ " + amount + c.RESET);
						}
						else if (type.equals("Withdrawal")) {
							System.out.println("[ " + time + " ] " + c.RED + "WITHDRAWAL " + c.RESET +
											   "of " + c.RED + "$ " + amount + c.RESET);
						}
					}
					System.out.println();
					
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			else if (choice == 5) {
				
				System.out.println(c.GREEN + "You are now logged out, thanks!\n" + c.RESET);
				return;
			}
		}
	}
	
	

	
	
	// HELPER METHODS
	
	// READ
	public List<User> getAllUsers() {
		
		String statement = "SELECT * FROM user";
		
		try (PreparedStatement ps = conn.prepareStatement(statement);
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				
				int acctNum = rs.getInt("account_id");
				String fName = rs.getString("first_name");
				String lName = rs.getString("last_name");
				String uName = rs.getString("username");
				String pass = rs.getString("password");
				float bal = rs.getFloat("balance");
				users.add(new User(fName, lName, uName, pass, acctNum, bal));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
		
	public int getMenuChoice() {
		
		String temp = s.nextLine();
			
		while (!temp.matches("^[1-3]$")) {
			System.out.println(c.RED + "Not a valid menu choice. Try again please." + c.RESET);
			System.out.print("Pick an option (1, 2, or 3): ");
			temp = s.nextLine();
		}
		
		return Integer.parseInt(temp);
	}
	
	public String[] getNames() {
		
		String[] tempNames = new String[2];
		System.out.print("Enter your first name: ");
		String tempFirst = s.nextLine();
		
		while (!tempFirst.matches("^[a-zA-Z]{3,}$")) {
			System.out.println(c.RED + "Not a valid name format. Try again." + c.RESET);
			System.out.print("Enter your first name: ");
			tempFirst = s.nextLine();
		}
		
		tempNames[0] = tempFirst;
		
		System.out.print("\nEnter your last name: ");
		String tempLast = s.nextLine();
		
		while (!tempLast.matches("^[a-zA-Z]{3,}$")) {
			System.out.println(c.RED + "Not a valid name format. Try again." + c.RESET);
			System.out.print("Enter your last name: ");
			tempLast = s.nextLine();
		}
		
		tempNames[1] = tempLast;
		
		return tempNames;
	}
	
	public String getUsername() {
		
		System.out.print("Enter a username you want (can contain letters, numbers, and at least 8 characters: ");
		String tempUsername = s.nextLine();
		
		while (true) {
			
			// check for valid username format
			while (!tempUsername.matches("^[a-zA-Z0-9]{8,}$")) {
				System.out.println(c.RED + "Not a valid username. Try again." + c.RESET);
				System.out.print("Enter a username you want (can contain letters, numbers, and at least 8 characters: ");
				tempUsername = s.nextLine();
			}
			
			// check for duplicate username
			for (User person : users) {
				
				if (person.getUsername().equals(tempUsername)) {
					
					System.out.println(c.RED + "That username is already taken! Please pick another one." + c.RESET);
					System.out.print("Enter a username you want (can contain letters, numbers, and at least 8 characters: ");
					tempUsername = s.nextLine();
					break;
				}
			}
			
			return tempUsername;
		}
	}
	
	public String getPassword() {
		
		System.out.print("Enter a password (must be at least 8 chars and can contain special characters): ");
		String tempPass = s.nextLine();
		
		// check for valid username format
		while (!tempPass.matches("^.{8,}$")) {
			System.out.println(c.RED + "Not a valid password, it must be at least 8 characters. Try again." + c.RESET);
			System.out.print("Enter a password (must be at least 8 chars and can contain special characters): ");
			tempPass = s.nextLine();
		}
		
		return tempPass;
	}
	
	public float getInitDeposit() {
		
		System.out.print("Enter your initial deposit (use XX.XX format): ");
		String tempDepo = s.nextLine();
		
		while (!tempDepo.matches("^\\$?\\d+(\\.(\\d{2}))?$")) {
			System.out.println(c.RED + "Not a valid currency format. Try again." + c.RESET);
			System.out.print("Enter your initial deposit (use XX.XX format): ");
			tempDepo = s.nextLine();
		}
		
		return Float.parseFloat(tempDepo);
	}
	
	public int getSessionMenuChoice() {
		
		System.out.print("Pick an option 1-5: ");
		String temp = s.nextLine();
		
		while (!temp.matches("^[1-5]$")) {
			System.out.println(c.RED + "Not a valid menu choice. Try again." + c.RESET);
			System.out.print("Pick an option 1-5: ");
			temp = s.nextLine();
		}
		return Integer.parseInt(temp);
	}
	
	public float getAmount(String type, User usr) {
		
		String tempAmount = "";
		
		// for deposits
		if (type.equals("D")) {
			
			System.out.print("Enter an amount to deposit (use XX.XX format): ");
			tempAmount = s.nextLine();
			
			while (!tempAmount.matches("^\\$?\\d+(\\.(\\d{2}))?$")) {
				
				System.out.println(c.RED + "Not a valid currency format. Try again." + c.RESET);
				System.out.print("Enter an amount to deposit (use XX.XX format): ");
				tempAmount = s.nextLine();
			}
		}
		
		// for withdrawals
		else if (type.equals("W")) {
			
			System.out.print("Enter an amount to withdraw (use XX.XX format): ");
			tempAmount = s.nextLine();
			
			while (!tempAmount.matches("^\\$?\\d+(\\.(\\d{2}))?$")) {
				
				System.out.println(c.RED + "Not a valid currency format. Try again." + c.RESET);
				System.out.print("Enter an amount to deposit (use XX.XX format): ");
				tempAmount = s.nextLine();
			}
		}
		
		return Float.parseFloat(tempAmount);
	}
	
	public float getCurrentBalance(User user) {
		
		// retrieve the user's non-updated balance
		String getBalance = "SELECT * FROM user WHERE account_id = ?";
		float oldBal = 0.0f;
		
		try (PreparedStatement ps2 = conn.prepareStatement(getBalance)) {
		
			ps2.setInt(1, user.getAccountNumber());
			ResultSet rs = ps2.executeQuery();
			
			while (rs.next()) {
				
				oldBal = rs.getFloat("balance");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return oldBal;
	}
	
	public void setNewBalance(User user, float oldBal, float amount) {
		
		// update balance with deposit
		String updateBalance = "UPDATE user SET balance = ? WHERE account_id = ?";
		
		try (PreparedStatement ps3 = conn.prepareStatement(updateBalance)) {
			
			ps3.setFloat(1, oldBal + amount);
			ps3.setInt(2, user.getAccountNumber());
			ps3.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}



























































