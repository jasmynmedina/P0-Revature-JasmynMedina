package com.revature.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;

/**
 * Implementation of UserDAO that reads and writes to a file
 */
public class UserDaoFile implements UserDao {

	public static String fileLocation = "users.txt";
	// File userFile = new File(fileLocation);

	public static List<User> userList = new ArrayList<>();

	public UserDaoFile() {
		File userFile = new File(fileLocation);
		if (!userFile.exists()) {
			try {
				userFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public User addUser(User user) {
		userList = getAllUsers();
		userList.add(user);
		try (ObjectOutputStream userOutput = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
			userOutput.writeObject(user);
			userOutput.close();
			// System.out.println("User Successfully Registered!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}

	public User getUser(Integer userId) {
		userList = getAllUsers();
		for (User u : userList) {
			if (u.getId().equals(userId)) {
				return u;
			}
		}

		return null;
	}

	public User getUser(String username, String pass) {
		userList = getAllUsers();

		for (User u : userList) {
			if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(pass)) {
			return u;
			}
		}
		return null;
	}
	

	//@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		try (ObjectInputStream userInput = new ObjectInputStream(new FileInputStream(fileLocation))) {
			//userList =(List<User>)userInput.readObject();
			do {
			userList.add((User) userInput.readObject());
			} while(userInput.readObject() != null); 
			userInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public User updateUser(User u) {
		userList = getAllUsers();
		for (User user : userList) {
			if (user.getId().equals(u.getId())) {
				userList.remove(u);
				userList.add(u);
				return u;
			}
			try (ObjectOutputStream userOutput = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
				userOutput.writeObject(userList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public boolean removeUser(User u) {
		userList = getAllUsers();
		boolean success = false;
		if (userList.contains(u)) {
			userList.remove(u);
			success = true;
		}
		return success;
	}

}