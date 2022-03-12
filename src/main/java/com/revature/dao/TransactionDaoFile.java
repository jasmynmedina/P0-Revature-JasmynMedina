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
import com.revature.beans.Transaction;

public class TransactionDaoFile implements TransactionDao {

	public static String fileLocation = "transactions.txt";
	//File transFile = new File(fileLocation);
	List<Transaction> transList = new ArrayList<Transaction>();
	List<Account> accountList = new ArrayList<Account>();
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
	public Transaction addTransaction(Transaction transactions) {
		transList = getAllTransactions();
		transList.add(transactions);
		try (ObjectOutputStream transOutput = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
			transOutput.writeObject(transactions);
			transOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transactions;
	}

//	public List<Transaction> getAllTransactions() {
//		Transaction t = new Transaction();
//		tranOsList.add(t);
//		try (ObjectOutputStream transOutput = new ObjectOutputStream(new FileOutputStream(fileLocation))) {
//			transOutput.writeObject(transList);
//			transOutput.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try (ObjectInputStream transInput = new ObjectInputStream(new FileInputStream(fileLocation))) {
//			List transList = (List<Transaction>)transInput.readObject();
//			transInput.close();
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		return transList;
//	}}
	public List<Transaction> getAllTransactions() {
		//try (ObjectInputStream transInput = new ObjectInputStream(new FileInputStream(fileLocation))) {
		try {
			FileInputStream transFile = new FileInputStream(fileLocation);
			ObjectInputStream transInput = new ObjectInputStream(transFile);
			List transList = (List<Transaction>)transInput.readObject();
			transInput.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for (Account account : accountList) {
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
