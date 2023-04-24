package com.gradebook.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.gradebook.dao.GradeBookDAO;
import com.gradebook.model.Course;
import com.gradebook.model.Student;
import com.gradebook.model.Teacher;

public class GradeBookController {
	
	static Scanner input = new Scanner(System.in);
	GradeBookDAO dao = new GradeBookDAO();
	
	public void run() {
		
		while (true) {
			System.out.println("======================");
			System.out.println("===== Welcome to =====");
			System.out.println("===== Gradebook ======");
			System.out.println("======================");
			System.out.println("1. Create Account");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			System.out.print("Pick an option (1-3): ");
			int option = getMenuChoice();
			
			if (option == 1) {
				String[] teacherInfo = getInfo();
				dao.createAccount(teacherInfo);
			}
			else if (option == 2) {
				System.out.print("Are you a student or teacher? (S/T): ");
				String answer = getAnswer();
				
				if (answer.equalsIgnoreCase("S")) { 
					Student currStudent = studentLogin();
					sessionStudent(currStudent);
				}
				else {
					Teacher currUser = teacherLogin();
					sessionTeacher(currUser);
				}
			}
			else if (option == 3) {
				System.out.println("\nHave a great day!");
				return;
			}
		}
	}
	
	public String[] getInfo() {
		String[] stuff = new String[4];
		
		System.out.print("Enter your first name: ");
		String fName = input.nextLine();
		
		while (!fName.matches("^[a-zA-Z]{2,}$")) {
			System.out.println("Invalid name format. Try again.");
			System.out.print("Enter your first name: ");
			fName = input.nextLine();
		}
		
		System.out.print("Enter your last name: ");
		String lName = input.nextLine();
		
		while (!lName.matches("^[a-zA-Z]{2,}$")) {
			System.out.println("Invalid name format. Try again.");
			System.out.print("Enter your last name: ");
			lName = input.nextLine();
		}
		
		System.out.print("Enter a username to log in with (at least 8 chars and only alphanumeric characters): ");
		String username = input.nextLine();
		
		while (!username.matches("^[a-zA-Z0-9]{8,}$")) {
			System.out.println("Not a valid username. Try again.");
			System.out.print("Enter a username to log in with (at least 8 chars and only alphanumeric characters): ");
			username = input.nextLine();
		}
		
		String validUsername = getValidUsername(username);
		
		System.out.print("Lastly, choose a password (must be at least 8 characters): ");
		String password = input.nextLine();
		
		while (!password.matches("^.{8,}$")) {
			System.out.println("Invalid password friend.");
			System.out.print("Lastly, choose a password (must be at least 8 characters): ");
			password = input.nextLine();
		}
		
		stuff[0] = fName;
		stuff[1] = lName;
		stuff[2] = validUsername;
		stuff[3] = password;
		
		return stuff;
	}
	
	public String getAnswer() {
		
		String temp = input.nextLine();
		
		while (!temp.matches("^[sStT]$")) {
			System.out.println("Not a valid option.");
			System.out.print("Are you a student or teacher? (S/T): ");
			temp = input.nextLine();
		}
		return temp;
	}
	
	public String getValidUsername(String name) {
		
		List<String> usernames = dao.getUsernames();
		
		while (usernames.contains(name)) {
			System.out.println("That username is taken, pick another please.");
			System.out.print("Enter a username to log in with (at least 8 chars and only alphanumeric characters): ");
			name = input.nextLine();
		}
		
		return name;
	}
	
	public Teacher teacherLogin() {
		
		System.out.println("TEACHER LOGIN\n");
		while (true) {

			System.out.print("Username: ");
			String uname = input.nextLine();
			System.out.print("Password: ");
			String pword = input.nextLine();
			Teacher t = dao.getTeacher(uname, pword);
			
			if (t == null) {
				System.out.println("Invalid credentials. Try again.\n");
				continue;
			}
			return t;
		}
	}
	
	public Student studentLogin() {
		
		System.out.println("STUDENT LOGIN\n");
		while (true) {

			System.out.print("Username: ");
			String uname = input.nextLine();
			System.out.print("Password: ");
			String pword = input.nextLine();
			Student s = dao.getStudent(uname, pword);
			
			if (s == null) {
				System.out.println("Invalid credentials. Try again.\n");
				continue;
			}
			System.out.println();
			return s;
		}
	}
	
	public void sessionTeacher(Teacher teacher) {
		
		while (true) {
			
			System.out.println("\n=======================");
			System.out.println("====== Your Menu ======");
			System.out.println("=======================");
			System.out.println("1. Add Class");
			System.out.println("2. View Classes");
			System.out.println("3. Add Student");
			System.out.println("4. View Class Information");
			System.out.println("5. Update grade");
			System.out.println("6. Delete student");
			System.out.println("7. Logout");
			System.out.print("Choose an option (1-7): ");
			int option = getTeacherMenuChoice();
			System.out.println();
			
			if (option == 1) {
				System.out.print("What is the name of your class: ");
				String title = input.nextLine();
				dao.addClass(teacher, title);
			}
			
			else if (option == 2) {
				
				List<Course> allClasses = dao.getCoursesByTeacher(teacher);
				allClasses.forEach(System.out::println);
			}
			
			else if (option == 3) {
				
				List<Course> allClasses = dao.getCoursesByTeacher(teacher);
				int courseId = getCourseId(allClasses);
				List<Student> unenrolledStudents = dao.getUnenrolledStudentsByCourse(courseId);
				int studentChoice = getStudentChoice(unenrolledStudents);
				dao.addStudentByCourse(courseId, studentChoice);
			}
			
			else if (option == 4) {
				List<Course> allClasses = dao.getCoursesByTeacher(teacher);
				int courseId = getCourseIdForInformation(allClasses);
				List<Student> studentsInClass = dao.getStudentsByClass(courseId);
				List<Integer> grades = new ArrayList<>();
				
				int sumOfGrades = 0;
				float median = 0.0f;
				float mean = 0.0f;
				
				
				for (Student s : studentsInClass) {
					grades.add(s.getGrade());
					sumOfGrades += s.getGrade();
				}
				
				Collections.sort(grades);
				int size = grades.size();
				
				if (size == 0) {
					System.out.println("No students enrolled in this course.");
					median = 0.0f;
					mean = 0.0f;
				}

				else {
					if (size % 2 != 0) {
						median = grades.get(size/2);
					}
					else {
						median = (grades.get(size/2) + grades.get((size/2)-1)) / 2;
					}
					mean = (float)sumOfGrades / size;
				}
				
				for (Student student : studentsInClass) {
					System.out.println("[ " + student.getFirstName() + " " + student.getLastName() + ", " +
									   student.getGrade() + " ]");
				}
				System.out.println("\nMean Grade: " + String.format("%.1f", mean));
				System.out.println("Median Grade: " + median);
			}
			
			else if (option == 5) {
				
				List<Course> allClasses = dao.getCoursesByTeacher(teacher);
				int courseId = getCourseIdForUpdatingGrade(allClasses);
				List<Student> studentsInClass = dao.getStudentsByClass(courseId);
				
				for (Student s : studentsInClass) {
					System.out.println(s.getId() + ": " + s.getFirstName() + " " + s.getLastName());
				}
				System.out.print("\nEnter the id of the student you wish to change grades: ");
				int studentID = getStudentIdForGradeChange(studentsInClass); 
				int newGrade = getNewGrade();
				dao.updateGrade(studentID, newGrade, courseId);
			}
			
			else if (option == 6) {
				
				List<Course> allClasses = dao.getCoursesByTeacher(teacher);
				int courseId = getCourseIdForUpdatingGrade(allClasses);
				List<Student> studentsInClass = dao.getStudentsByClass(courseId);
				
				for (Student s : studentsInClass) {
					System.out.println(s.getId() + ": " + s.getFirstName() + " " + s.getLastName());
				}
				System.out.print("\nEnter the id of the student you wish to remove: ");
				int studentID = getStudentIdForRemoval(studentsInClass);
				dao.deleteStudent(courseId, studentID);
			}
			
			else if (option == 7) {
				
				System.out.println("You are now logged out " + teacher.getFirstName() + "\n");
				return;
			}
		}
	}
	
	public void sessionStudent(Student student) {
		
		while (true) {
			
			System.out.println("\n=======================");
			System.out.println("====== Your Menu ======");
			System.out.println("=======================");
			System.out.println("1. View Grades");
			System.out.println("2. Logout");
			System.out.print("Choose an option (1 or 2): ");
			int option = getTeacherMenuChoice();
			System.out.println();
			
			if (option == 1) {
				List<Student> allStudents = dao.getGradesByStudent(student);
				
				for (Student s : allStudents) {
					System.out.println("[" + s.getTitle() + ": " + s.getGrade() + "]");
				}
			}
			
			else if (option == 2) {
				
				System.out.println("You are now logged out " + student.getFirstName() + "\n");
				return;
			}
		}
	}
	
	public int getMenuChoice() {
		
		String option = input.nextLine();
		while (!option.matches("^[1-3]$")) {
			System.out.println("Not a valid menu choice.");
			System.out.print("Pick an option (1-3): ");
			option = input.nextLine();
		}
		return Integer.parseInt(option);
	}
	
	public int getTeacherMenuChoice() {
		String option = input.nextLine();
		
		while (!option.matches("^[1-7]$")) {
			System.out.println("Invalid option.");
			System.out.print("Choose an option (1-7): ");
			option = input.nextLine();
		}
		
		return Integer.parseInt(option);
	}
	
	public int getStudentMenuChoice() {
		String option = input.nextLine();
		
		while (!option.matches("^[1-2]$")) {
			System.out.println("Invalid option.");
			System.out.print("Choose an option (1-2): ");
			option = input.nextLine();
		}
		
		return Integer.parseInt(option);
	}
	
	public int getClassChoice(List<String> classes) {
		
		int count = 1;
		int size = classes.size() - 1;
		
		for (String cls : classes) {
			System.out.println(count + ": " + cls);
		}
		
		System.out.print("Which class did you want to add a student to?: ");
		String choice = input.nextLine();
		
		while (!choice.matches("^[0-"+size+"]$")) {
			System.out.println("Invalid class choice.");
			System.out.print("Which class did you want to add a student to?: ");
			choice = input.nextLine();
		}
		return Integer.parseInt(choice);
	}
	
	public int getStudentChoice(List<Student> unenrolledStudents) {
		
		List<Integer> ids = new ArrayList<>();
		for (Student s : unenrolledStudents) {
			ids.add(s.getId());
			System.out.println(s.getId() + ": " + s.getFirstName() + " " + s.getLastName());
		}
		
		System.out.print("Pick a student by their ID that you wish to add: ");
		String temp = input.nextLine();
		
		while (!ids.contains(Integer.parseInt(temp))) {
			System.out.println("Not a valid student selection.");
			System.out.print("Pick a student by their ID that you wish to add: ");
			temp = input.nextLine();
		}
		System.out.println();
		return Integer.parseInt(temp);
	}
	
	public int getCourseId(List<Course> courses) {
		
		List<String> ids = new ArrayList<>();
		
		for (Course c : courses) {
			ids.add(c.getId() + "");
			System.out.println(c.getId() + ": " + c.getTitle());
		}
		
		System.out.print("Enter the id of the class you want to add a student to: ");
		String choice = input.nextLine();
		
		while (!ids.contains(choice)) {
			System.out.println("Not a valid class id.");
			System.out.print("Enter the id of the class you want to add a student to: ");
			choice = input.nextLine();
		}
		System.out.println();
		return Integer.parseInt(choice);
	}
	
	public int getCourseIdForInformation(List<Course> courses) {
		
		List<String> ids = new ArrayList<>();
		
		for (Course c : courses) {
			ids.add(c.getId() + "");
			System.out.println(c.getId() + ": " + c.getTitle());
		}
		
		System.out.print("Enter the id of the class you want to view info for: ");
		String choice = input.nextLine();
		
		while (!ids.contains(choice)) {
			System.out.println("Not a valid class id.");
			System.out.print("Enter the id of the class you want to view info for: ");
			choice = input.nextLine();
		}
		System.out.println();
		return Integer.parseInt(choice);
	}
	
	public int getCourseIdForUpdatingGrade(List<Course> courses) {
		
		List<String> ids = new ArrayList<>();
		
		for (Course c : courses) {
			ids.add(c.getId() + "");
			System.out.println(c.getId() + ": " + c.getTitle());
		}
		
		System.out.print("Enter the id of the class you want to change a grade: ");
		String choice = input.nextLine();
		
		while (!ids.contains(choice)) {
			System.out.println("Not a valid class id.");
			System.out.print("Enter the id of the class you want to change a grade: ");
			choice = input.nextLine();
		}
		System.out.println();
		return Integer.parseInt(choice);
	}
	
	public int getStudentIdForGradeChange(List<Student> studentsInClass) {
		
		List<String> ids = new ArrayList<>();
		
		for (Student s : studentsInClass) {
			ids.add(s.getId() + "");
		}
		
		String temp = input.nextLine();
		
		while (!ids.contains(temp)) {
			System.out.println("Invalid Student ID.");
			System.out.print("\nEnter the id of the student you wish to change grades: ");
			temp = input.nextLine();
		}
		System.out.println();
		return Integer.parseInt(temp);
	}
	
	public int getNewGrade() {
		
		System.out.print("Enter new grade for this student: ");
		String temp = input.nextLine();
		
		while (!temp.matches("^[0-9][0-9]?$|^100$")) {
			System.out.println("Invalid grade. Try again.");
			System.out.print("Enter new grade for this student: ");
			temp = input.nextLine();
		}
		System.out.println();
		return Integer.parseInt(temp);
	}
	
	public int getStudentIdForRemoval(List<Student> studentsInClass) {
		
		List<String> ids = new ArrayList<>();
		
		for (Student s : studentsInClass) {
			ids.add(s.getId() + "");
		}
		String temp = input.nextLine();
		
		while (!ids.contains(temp)) {
			System.out.println("Invalid Student ID.");
			System.out.print("\nEnter the id of the student you wish to remove: ");
			temp = input.nextLine();
		}
		System.out.println();
		return Integer.parseInt(temp);
	}
}






































