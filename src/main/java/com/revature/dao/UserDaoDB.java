package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of UserDAO that reads/writes to a relational database
 */
public class UserDaoDB implements UserDao {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	public static List<User> userList = new ArrayList<>();
	
	public UserDaoDB() {
		try {
			conn = ConnectionUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public User addUser(User user) {
		String query = "INSERT INTO userTable (id, username, password, first_name, last_name, usertype) VALUES (?, ?, ?, ?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, user.getId());
			pstmt.setString(2, user.getUsername());
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, user.getFirstName());
			pstmt.setString(5, user.getLastName());
			if (user.getUserType() == null) {
				pstmt.setString(6, "");
			} else {
				pstmt.setString(6, user.getUserType().toString());
			}
			pstmt.execute();
			return user;
			//pstmt.setString(6, user.getUserType().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public User getUser(Integer userId) {
		String query = "SELECT * FROM usertable WHERE id = " + userId;
		User user = new User();
		String userType = "";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				userType = rs.getString("usertype");
				if (userType.equals("CUSTOMER")) {
					user.setUserType(UserType.CUSTOMER);
				} else if (userType.equals("EMPLOYEE")) {
					user.setUserType(UserType.EMPLOYEE);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	public User getUser(String username, String pass) {
		String query = "SELECT * FROM usertable WHERE username = " + username + " AND password = " + pass;
		User user = new User();
		String userType = "";

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				//user.setUserType(rs.getString("usertype"));
				userType = rs.getString("usertype");
				if (userType.equals("CUSTOMER")) {
					user.setUserType(UserType.CUSTOMER);
				} else if (userType.equals("EMPLOYEE")) {
					user.setUserType(UserType.EMPLOYEE);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	public List<User> getAllUsers() {
		String userType = "";
		String query = "SELECT * FROM usertable";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				userType = rs.getString("usertype");
				if (userType.equals("CUSTOMER")) {
					user.setUserType(UserType.CUSTOMER);
				} else if (userType.equals("EMPLOYEE")) {
					user.setUserType(UserType.EMPLOYEE);
				}
				//user.setUserType(rs.getString("usertype"));
				userList.add(user);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public User updateUser(User u) {
			String query = "UPDATE usertable SET username=?, password=?, first_name=?, last_name=?, usertype=? WHERE id=" + u.getId();
			try {
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, u.getUsername());
				pstmt.setString(2, u.getPassword());
				pstmt.setString(3, u.getFirstName());
				pstmt.setString(4, u.getLastName());
				/*if (u.getUserType() == null) {
					pstmt.setString(6, "");
				} else {
					pstmt.setString(6, u.getUserType().toString());
				}*/
				pstmt.setString(5, u.getUserType().toString());
				pstmt.executeUpdate();
				
	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return u;
		}
	
		public boolean removeUser(User u) {
			String query = "DELETE FROM usertable WHERE id=" + u.getId();
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return true;
		}
	
	}
