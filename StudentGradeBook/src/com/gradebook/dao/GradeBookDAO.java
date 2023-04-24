package com.gradebook.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.gradebook.connection.ConnectionManager;
import com.gradebook.model.Course;
import com.gradebook.model.Student;
import com.gradebook.model.Teacher;

public class GradeBookDAO {
	
	private Connection conn = ConnectionManager.getConnection();
	static Scanner input = new Scanner(System.in);
	
	
	// CREATE
	public void createAccount(String[] info) {
		String insertStmt = "INSERT INTO teacher VALUES (null, ?, ?, ?, ?);";
		
		try (PreparedStatement ps = conn.prepareStatement(insertStmt)) {
			ps.setString(1, info[0]);
			ps.setString(2, info[1]);
			ps.setString(3, info[2]);
			ps.setString(4, info[3]);
			ps.execute();
			System.out.println("You have successfully created your account " + info[0]);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addClass(Teacher teacher, String title) {
		
		String insert = "INSERT INTO class VALUES(null, ?);";
		try (PreparedStatement ps = conn.prepareStatement(insert)) {
			ps.setString(1, title);
			ps.execute();
			System.out.println("Class: " + title + " added!");
			
			String insert2 = "INSERT INTO teacher_class VALUES(?, ?);";
			try (PreparedStatement ps2 = conn.prepareStatement(insert2)) {
				ps2.setString(1, title);
				ps2.setInt(2, teacher.getId());
				ps2.execute();
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addStudentByCourse(int courseId, int studentId) {
		
		String enrolledInsert = "INSERT INTO enrolled VALUES(?, ?, 0);";
		
		try (PreparedStatement ps = conn.prepareStatement(enrolledInsert)) {
			
			ps.setInt(1, courseId);
			ps.setInt(2, studentId);
			ps.execute();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Student added!\n");
	}

	
	// READ
	public List<Teacher> getAllTeachers() {
		
		List<Teacher> teachers = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM teacher");
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				int id = rs.getInt("teacher_id");
				String fn = rs.getString("first_name");
				String ln = rs.getString("last_name");
				String un = rs.getString("username");
				String pw = rs.getString("password");
				teachers.add(new Teacher(id, fn, ln, un, pw));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return teachers;
	}
	
	public List<Student> getAllStudents() {
		
		List<Student> students = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM teacher");
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				int id = rs.getInt("teacher_id");
				String fn = rs.getString("first_name");
				String ln = rs.getString("last_name");
				String un = rs.getString("username");
				String pw = rs.getString("password");
				students.add(new Student(id, fn, ln, un, pw));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}
	
	public List<String> getUsernames() {
		
		List<String> usernames = new ArrayList<>();
		
		try (PreparedStatement ps = conn.prepareStatement("SELECT username FROM teacher;");
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				usernames.add(rs.getString("username"));
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usernames;
	}
	
	public Teacher getTeacher(String username, String password) {
		
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM teacher;");
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				int tempId = rs.getInt("teacher_id");
				String tempFirstName = rs.getString("first_name");
				String tempLastName = rs.getString("last_name");
				String tempUsername = rs.getString("username");
				String tempPassword = rs.getString("password");
				
				if (username.equals(tempUsername) && password.equals(tempPassword)) {
					System.out.println("Welcome " + tempFirstName + "!");
					return new Teacher(tempId, tempFirstName, tempLastName, tempUsername, tempPassword);
				}
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Student getStudent(String username, String password) {
		
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM student;");
			 ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				int tempId = rs.getInt("student_id");
				String tempFirstName = rs.getString("first_name");
				String tempLastName = rs.getString("last_name");
				String tempUsername = rs.getString("username");
				String tempPassword = rs.getString("password");
				
				if (username.equals(tempUsername) && password.equals(tempPassword)) {
					System.out.println("Welcome " + tempFirstName + "!");
					return new Student(tempId, tempFirstName, tempLastName, tempUsername, tempPassword);
				}
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Student> getGradesByStudent(Student student) {
		
		List<Student> students = new ArrayList<>();
		String getStmt = "SELECT student_id, first_name, last_name, title, grade FROM student " +
						 "JOIN enrolled USING(student_id) " +
						 "JOIN class USING(class_id) " +
						 "WHERE student_id = ?;";
		
		try (PreparedStatement ps = conn.prepareStatement(getStmt)) {
			
			ps.setInt(1, student.getId());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("student_id");
				String fn = rs.getString("first_name");
				String ln = rs.getString("last_name");
				String title = rs.getString("title");
				int grade = rs.getInt("grade");
				students.add(new Student(id, fn, ln, title, grade));
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}
	
	public List<Student> getStudentsByClass(int course) {
		
		List<Student> students = new ArrayList<>();
		String query = "SELECT DISTINCT student_id, first_name, last_name, grade FROM student " +
					   "JOIN enrolled USING(student_id) " +
					   "JOIN class USING(class_id) " +
					   "WHERE class_id = ? " +
					   "ORDER BY first_name, grade;";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setInt(1, course);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int sID = rs.getInt("student_id");
				String fn = rs.getString("first_name");
				String ln = rs.getString("last_name");
				int grade = rs.getInt("grade");
				students.add(new Student(sID, fn, ln, grade));
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}
	
	public List<Course> getCoursesByTeacher(Teacher teacher) {
		
		List<Course> classes = new ArrayList<>();
		String query = "select * from class join teacher_class using (title) where teacher_id = ? order by class_id;";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setInt(1, teacher.getId());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("class_id");
				String title = rs.getString("title");
				classes.add(new Course(id, title));
			}
			
			if (classes.isEmpty()) {
				System.out.println("You have no classes yet");
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	public List<Student> getUnenrolledStudentsByCourse(int courseId) {
		
		List<Student> studentsEnrolled = new ArrayList<>();
		List<Student> studentsUnenrolled = new ArrayList<>();
		String getStmt = "SELECT * FROM student " +
						 "JOIN enrolled USING (student_id) " +
						 "JOIN class USING (class_id) " +
						 "WHERE class_id = ?;";
				
		try (PreparedStatement ps = conn.prepareStatement(getStmt)) {
			
			ps.setInt(1, courseId);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				
				int studentId = rs.getInt("student_id");
				String fName = rs.getString("first_name");
				String lName = rs.getString("last_name");
				
				studentsEnrolled.add(new Student(studentId, fName, lName));
			}
			
			String partial = "";
			for (int i = 0; i < studentsEnrolled.size(); i++) {
				partial += studentsEnrolled.get(i).getId();
				if (i != studentsEnrolled.size() - 1) {
					partial += ", ";
				}
				else {
					break;
				}
					
			}
			
			String getStmt2 = "SELECT DISTINCT student_id, first_name, last_name FROM student " +
							  "JOIN enrolled USING(student_id) " +
							  "JOIN class USING(class_id) " +
							  "WHERE class_id <> ? AND student_id NOT IN(" + partial + ");";
							  
			try (PreparedStatement ps2 = conn.prepareStatement(getStmt2)) {
				
				ps2.setInt(1, courseId);
				ResultSet rs2 = ps2.executeQuery();
				
				while (rs2.next()) {
					int sID = rs2.getInt("student_id");
					String fn = rs2.getString("first_name");
					String ln = rs2.getString("last_name");
					studentsUnenrolled.add(new Student(sID, fn, ln));
				}
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return studentsUnenrolled;
	}
	
	
	// UPDATE
	public void updateGrade(int studentId, int grade, int course) {
		
		String updateStmt = "UPDATE enrolled SET grade = ? WHERE student_id = ? AND class_id = ?;";
		
		try (PreparedStatement ps = conn.prepareStatement(updateStmt)) {
			
			ps.setInt(1, grade);
			ps.setInt(2, studentId);
			ps.setInt(3, course);
			ps.execute();
			System.out.println("\nGrade updated!");
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
	// DELETE
	public void deleteStudent(int course, int student) {
		
		String deleteStmt = "DELETE FROM enrolled WHERE class_id = ? AND student_id = ?;";
		
		try (PreparedStatement ps = conn.prepareStatement(deleteStmt)) {
			
			ps.setInt(1, course);
			ps.setInt(2, student);
			ps.execute();
			System.out.println("\nStudent removed from this class!\n");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}









































