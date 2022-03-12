package com.revature.driver;

import java.util.Scanner;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.dao.AccountDaoDB;
import com.revature.dao.TransactionDaoDB;
import com.revature.dao.UserDaoDB;
import com.revature.dao.UserDaoFile;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.services.AccountService;
import com.revature.services.UserService;
import com.revature.utils.SessionCache;

/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {

	public static void main(String[] args) {
//		UserDaoFile userDao = new UserDaoFile();
//		AccountDaoFile accountDao = new AccountDaoFile();
//		TransactionDaoFile transDao = new TransactionDaoFile();
		UserDaoDB userDao = new UserDaoDB();
		AccountDaoDB accountDao = new AccountDaoDB();
		TransactionDaoDB transDao = new TransactionDaoDB();
		UserService userServ = new UserService(userDao, accountDao);
		AccountService actServ = new AccountService(accountDao);

		Scanner sc = new Scanner(System.in);
		boolean login = false;
		int option = 0;
		int choice = 0;
		while (choice < 4) {
			System.out.println("+-------------------------------+");
			System.out.println("|      Welcome to Jasmyn's      |");
			System.out.println("|            Bank App           |");
			System.out.println("+-------------------------------+");
			System.out.println("Please choose an option below:");
			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("3. Display");
			System.out.println("4. Exit");
			System.out.println("Enter choice: ");
			choice = sc.nextInt();

			switch (choice) {
			case 1:
				User newUser = new User();
				int id = 0;
				UserType custType = User.UserType.CUSTOMER;
				UserType empType = User.UserType.EMPLOYEE;
				String firstname;
				String lastname;
				String username;
				String password;
				
				System.out.println("Are you a 1. CUSTOMER or 2. EMPLOYEE : ");
				option = sc.nextInt();
				if (option == 1) {
					newUser.setUserType(User.UserType.CUSTOMER);
					id = UserDaoFile.userList.size();
					System.out.println("Enter your first name: ");
					firstname = sc.next();
					System.out.println("Enter your last name: ");
					lastname = sc.next();
					System.out.println("Enter username: ");
					username = sc.next();
					System.out.println("Enter password: ");
					password = sc.next();
					newUser = new User(id, username, password, firstname, lastname, custType);
					try {
					userServ.register(newUser);
					login = true;
					} catch (UsernameAlreadyExistsException e) {
						e.printStackTrace();
					}
					
				} if (option == 2) {
					newUser.setUserType(User.UserType.EMPLOYEE);
					id = UserDaoFile.userList.size();
					System.out.println("Enter your first name: ");
					firstname = sc.next();
					System.out.println("Enter your last name: ");
					lastname = sc.next();
					System.out.println("Enter username: ");
					username = sc.next();
					System.out.println("Enter password: ");
					password = sc.next();
					newUser = new User(id, username, password, firstname, lastname, empType);
					try {
						userServ.register(newUser);
						} catch (UsernameAlreadyExistsException e) {
							e.printStackTrace();
						}
				}
			break;
				
//				id = UserDaoFile.userList.size();
//				System.out.println("Enter your first name: ");
//				String firstname = sc.next();
//				System.out.println("Enter your last name: ");
//				String lastname = sc.next();
//				System.out.println("Enter username: ");
//				String username = sc.next();
//				System.out.println("Enter password: ");
//				String password = sc.next();
//				newUser = new User(id++, username, password, firstname, lastname, custType);
//				userServ.register(userDao.addUser(newUser));
//				login = true;
				
			case 2:
				System.out.println("+-------------------------------+");
				System.out.println("|    Welcome Back to Jasmyn's   |");
				System.out.println("|        Bank App Sign In       |");
				System.out.println("+-------------------------------+");
				System.out.println("Enter your username: ");
				username = sc.next();
				System.out.println("Enter your password: ");
				password = sc.next();
				
				try {
					userServ.login(username, password);
					login = true;
				} catch (InvalidCredentialsException e) {
					e.printStackTrace();
					break;
				}

				// Access Account
				int customer = 0;
				int customer1 = 0; 
				int actType = 0;
				double amount = 0.0d;
				AccountType chkAct = Account.AccountType.CHECKING;
				AccountType saveAct = Account.AccountType.SAVINGS;
				Account act = new Account();
				if (login = true && customer < 5) {
					System.out.println("Choose an option below: ");
					System.out.println("1. Create account");
					System.out.println("2. Deposit");
					System.out.println("3. Withdraw");
					System.out.println("4. Transfer");
					System.out.println("5. View balance");
					System.out.println("6. I'm an EMPLOYEE");
					System.out.println("Enter choice:");
					customer = sc.nextInt();
					switch (customer) {
					case 1:
						System.out.println("Would you like to open a 1. CHECKING or 2. SAVING account?");
						actType = sc.nextInt();
						if (actType == 1) {
							act.setType(chkAct);
							actServ.createNewAccount(SessionCache.getCurrentUser().get());
							userDao.updateUser(SessionCache.getCurrentUser().get());
							System.out.println("Great! Your checking account has been created!!");	
						} if (actType == 2) {
							act.setType(saveAct);
							actServ.createNewAccount(SessionCache.getCurrentUser().get());
							userDao.updateUser(SessionCache.getCurrentUser().get());
							System.out.println("Great! Your saving account has been created!!");		
						}
						break;
					case 2:
						int actId = 0;
						int toActId = 0;
						System.out.println("Enter account ID: ");
						actId = sc.nextInt();
						System.out.println("How much would you like to DEPOSIT: ");
						amount = sc.nextDouble();
						try {
						actServ.deposit(accountDao.getAccount(actId), amount);
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						} catch (OverdraftException e) {
							e.printStackTrace();
						}
						break;
					case 3:
						System.out.println("Enter account ID: ");
						actId = sc.nextInt();
						System.out.println("How much would you like to WITHDRAW: ");
						amount = sc.nextDouble();
						try {
						actServ.withdraw(accountDao.getAccount(actId), amount);
						} catch (OverdraftException e) {
							e.printStackTrace();
						} catch (UnsupportedOperationException e) {
						e.printStackTrace();
						}
						break;
					case 4:
						System.out.println("Enter account ID: ");
						actId = sc.nextInt();
						System.out.println("How much would you like to TRANSFER: ");
						amount = sc.nextDouble();
						System.out.println("Enter to account ID: ");
						toActId = sc.nextInt();
						try {
						actServ.transfer(accountDao.getAccount(actId), accountDao.getAccount(toActId), amount);
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						} catch (OverdraftException e) {
							e.printStackTrace();
						}
						break;
					case 5:
						System.out.println("Enter account ID: ");
						actId = sc.nextInt();
						System.out.println(accountDao.getAccount(actId));
						break;
					case 6:
						System.out.println("As an EMPLOYEE choose an option below: ");
						System.out.println("1. APPROVE/REJECT Account");
						System.out.println("2. View Log of Transactions");
						System.out.println("Enter choice:");
						customer1 = sc.nextInt();
						switch (customer1) {
						case 1:
							System.out.println("Enter account ID: ");
							actId = sc.nextInt();
							System.out.println("Enter 1 to approve or 2 to reject: ");
							int app = sc.nextInt();
							if (actServ.approveOrRejectAccount(accountDao.getAccount(actId), app == 1)) {
								accountDao.getAccount(actId).setApproved(true);
								System.out.println("Account APPROVED!!!!");
							} else {
								accountDao.getAccount(actId).setApproved(false);
								System.out.println("Account REJECTED!!!!");
							}

							break;
						case 2:
							System.out.println("Enter account ID to view TRANSACTIONS: ");
							actId = sc.nextInt();
							System.out.println(actServ.actDao.getAccount(actId).getTransactions());
							break;
						}
					}
					break;
				}

			case 3:
				for (User u : userDao.getAllUsers()){
					System.out.println(u);
				}
				for (Account a : accountDao.getAccounts()) {
					System.out.println(a);
				}
				for (Transaction t : transDao.getAllTransactions()) {
					System.out.println(t);
				}
				break;
			case 4:
				choice = 4;
				System.out.println("You are now leaving Jasmyn's Bank!");
				sc.close();
			
		}

	}
}

	
}