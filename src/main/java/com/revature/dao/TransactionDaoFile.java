package com.revature.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Transaction;

public class TransactionDaoFile implements TransactionDao {

	public static String fileLocation = "transactions.txt";
	//File transFile = new File(fileLocation);
	public static List<Transaction> transList = new ArrayList<Transaction>();
	List<Account> acountList = new ArrayList<Account>();
	
	public TransactionDaoFile() {
		File transFile = new File(fileLocation);
		if(!transFile.exists()) {
			try {
				transFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	@SuppressWarnings("unchecked")
	public List<Transaction> getAllTransactions() {
		//try (ObjectInputStream transInput = new ObjectInputStream(new FileInputStream(fileLocation))) {
		try {
			FileInputStream transFile = new FileInputStream(fileLocation);
			ObjectInputStream transInput = new ObjectInputStream(transFile);
			acountList = (ArrayList<Account>)transInput.readObject();
			transInput.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for (Account account : acountList) {
			if (account.getTransactions() != null) {
				List<Transaction> transactions = account.getTransactions();
				for (Transaction t : transactions) {
					transList.add(t);
				}
			}
		}

		return transList;
	}
}

/*
 * transList = getAllTransactions(); transList.add(t); try { FileOutputStream
 * transOutFile = new FileOutputStream(transFile); ObjectOutputStream
 * transOutput = new ObjectOutputStream(transOutFile);
 * transOutput.writeObject(t); } catch (IOException e) { e.printStackTrace(); }
 * return t; }
 * 
 * public List<Transaction> getAllTransactions() { try { FileInputStream
 * transInFile = new FileInputStream(fileLocation); ObjectInputStream transInput
 * = new ObjectInputStream(transInFile); transList.add((Transaction)
 * transInput.readObject()); } catch (FileNotFoundException e) {
 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch
 * (ClassNotFoundException e) { e.printStackTrace(); } return transList; } }
 */