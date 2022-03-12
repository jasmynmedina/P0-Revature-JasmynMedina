package com.revature.services;

import com.revature.beans.User;
import com.revature.dao.AccountDao;
import com.revature.dao.UserDao;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on
 * users
 */
public class UserService {

	UserDao userDao;
	AccountDao accountDao;

	public UserService(UserDao udao, AccountDao adao) {
		this.userDao = udao;
		this.accountDao = adao;
	}

	/**
	 * Validates the username and password, and return the User object for that user
	 * 
	 * @throws InvalidCredentialsException if either username is not found or
	 *                                     password does not match
	 * @return the User who is now logged in
	 */
	public User login(String username, String password) {
		User valUser = userDao.getUser(username, password);

		if (valUser == null) {
			throw new InvalidCredentialsException();
		} else if (!valUser.getPassword().equals(password)) {
			throw new InvalidCredentialsException();
		} else {
			SessionCache.setCurrentUser(valUser);
			return valUser;
		}
	}

	/**
	 * Creates the specified User in the persistence layer
	 * 
	 * @param newUser the User to register
	 * @throws UsernameAlreadyExistsException if the given User's username is taken
	 */
	public void register(User newUser) {
		User regUser = userDao.getUser(newUser.getUsername(), newUser.getPassword());

		if (regUser != null) {
			//userDao.addUser(newUser);
			throw new UsernameAlreadyExistsException();
			
		} else {
			userDao.addUser(newUser);
			System.out.println("Successfully Registered!!!");

		}

	}
}

