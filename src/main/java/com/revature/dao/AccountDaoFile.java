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

import com.revature.beans.Account;
import com.revature.beans.User;

/**
 * Implementation of AccountDAO which reads/writes to files
 */
public class AccountDaoFile implements AccountDao {
	// use this file location to persist the data to

	public static String fileLocation = "accounts.txt";
	//File accountFile = new File(fileLocation);
	public static List<Account> accountList = new ArrayList<Account>();
	
	public AccountDaoFile() {
		File accountFile = new File(fileLocation);
		if(!accountFile.exists()) {
			try {
				accountFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Account addAccount(Account a) {
		//accountList = getAccounts();
		accountList.add(a);
		//try (ObjectOutputStream actOutput = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
		try {
			FileOutputStream fileOutput = new FileOutputStream(fileLocation);
			ObjectOutputStream actOutput = new ObjectOutputStream(fileOutput);
			actOutput.writeObject(accountList);
			actOutput.close();
			//System.out.println("Account Successfully Registered!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}

	public Account getAccount(Integer actId) {
		accountList = getAccounts();
		for (Account a : accountList) {
			if (a.getId().equals(actId)) {
				return a;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Account> getAccounts() {
		//try (ObjectInputStream actInput = new ObjectInputStream(new FileInputStream(fileLocation))) {
		try {
			FileInputStream fileInput = new FileInputStream(fileLocation);
			ObjectInputStream actInput = new ObjectInputStream(fileInput);
			accountList = (ArrayList<Account>)actInput.readObject();
			actInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return accountList;
	}

	public List<Account> getAccountsByUser(User u) {
		List<Account> actByUser = new ArrayList<Account>();
		accountList = getAccounts();
		for(Account account : accountList) {
			if (u.getId().equals(account.getOwnerId())) {
				actByUser.add(account);
			}
		}
		return accountList;
	}

	public Account updateAccount(Account a) {
		accountList = getAccounts();
		if (accountList.contains(a)) {
			return a;
		}
		return a;
	}

	public boolean removeAccount(Account a) {
		accountList = getAccounts();
		boolean success = accountList.contains(a);
		if (success = true) {
			accountList.remove(a);
		}
		return success;
	}

}
