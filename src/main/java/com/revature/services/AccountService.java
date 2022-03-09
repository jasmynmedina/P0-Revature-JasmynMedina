package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.dao.AccountDao;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on Accounts
 */
public class AccountService {

	public AccountDao actDao;
	public static final double STARTING_BALANCE = 0.01;
	List<Account> accountList = new ArrayList<Account>();
	List<Transaction> transactions = new ArrayList<Transaction>();

	public AccountService(AccountDao dao) {
		this.actDao = dao;
	}

	/**
	 * Withdraws funds from the specified account
	 * @throws OverdraftException if amount is greater than the account balance
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void withdraw(Account a, Double amount) {
		if (!a.isApproved()) {
			throw new UnsupportedOperationException();
		}

		if (amount < 0) {
			throw new UnsupportedOperationException();
		} else {
			Double balance = a.getBalance();
			if (balance > amount) {
				a.setBalance(balance - amount);

				Transaction t = new Transaction();
				t.setType(TransactionType.WITHDRAWAL);
				t.setAmount(amount);
				t.setTimestamp();

				if (a.getTransactions() != null) {
					transactions = a.getTransactions();
				}
				transactions.add(t);

				a.setTransactions(transactions);
				actDao.updateAccount(a);

			} else {
				throw new OverdraftException();
			}
		}
	}

	/**
	 * Deposit funds to an account
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void deposit(Account a, Double amount) {
		if (!a.isApproved()) {
			throw new UnsupportedOperationException();
		}

		if (amount < 0) {
			throw new UnsupportedOperationException();
		} else {
			Double balance = a.getBalance();
			a.setBalance(balance + amount);

			Transaction t = new Transaction();
			t.setType(TransactionType.DEPOSIT);
			t.setAmount(amount);
			t.setTimestamp();

			List<Transaction> transactions = new ArrayList<Transaction>();
			if (a.getTransactions() != null) {
				transactions = a.getTransactions();
			}
			transactions.add(t);

			a.setTransactions(transactions);
			actDao.updateAccount(a);
		}
	}

	/**
	 * Transfers funds between accounts
	 * @throws UnsupportedOperationException if amount is negative or 
	 * the transaction would result in a negative balance for either account
	 * or if either account is not approved
	 * @param fromAct the account to withdraw from
	 * @param toAct the account to deposit to
	 * @param amount the monetary value to transfer
	 */
	public void transfer(Account fromAct, Account toAct, double amount) {
		if (!fromAct.isApproved()) {
			throw new UnsupportedOperationException();
		}
		if (!toAct.isApproved()) {
			throw new UnsupportedOperationException();
		}
		if (amount < 0) {
			throw new UnsupportedOperationException();
		}

		Double fromActBal = fromAct.getBalance();
		if (fromActBal < amount) {
			throw new UnsupportedOperationException();
		} else {
			fromAct.setBalance(fromActBal - amount);
			toAct.setBalance(toAct.getBalance() + amount);

			// Creates transaction

			Transaction t = new Transaction();
			t.setSender(fromAct);
			t.setRecipient(toAct);
			t.setAmount(amount);
			t.setTimestamp();

			if (fromAct.getTransactions() != null) {
				transactions = fromAct.getTransactions();
			}
			transactions.add(t);
			fromAct.setTransactions(transactions);
			actDao.updateAccount(fromAct);

			List<Transaction> transactions2 = new ArrayList<Transaction>();
			if (toAct.getTransactions() != null) {
				transactions2 = toAct.getTransactions();
			}
			transactions2.add(t);
			toAct.setTransactions(transactions2);
			actDao.updateAccount(toAct);
		}
	}

	/**
	 * Creates a new account for a given User
	 * @return the Account object that was created
	 */
	public Account createNewAccount(User u) {
		Account account = new Account();
		account.setId(u.getId());
		account.setBalance(0.01);
		accountList.add(account);
		u.setAccounts(accountList);
		actDao.addAccount(account);
		return account;
	}

	/**
	 * Approve or reject an account.
	 * @param a
	 * @param approval
	 * @throws UnauthorizedException if logged in user is not an Employee
	 * @return true if account is approved, or false if unapproved
	 */
	public boolean approveOrRejectAccount(Account a, boolean approval) {
		Optional<User> u = SessionCache.getCurrentUser();
		User user = new User();
		user = u.get();
		if (user.getUserType().equals(UserType.CUSTOMER)) {
			throw new UnauthorizedException();
		}else {
			a.setApproved(true);
			return true;
			}
		}
	}