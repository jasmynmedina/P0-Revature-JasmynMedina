package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
import com.revature.beans.User;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of AccountDAO which reads/writes to a database
 */
public class AccountDaoDB implements AccountDao {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	List<Account> accountList = new ArrayList<>();

	public AccountDaoDB() {
		try {
			conn = ConnectionUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Account addAccount(Account a) {
		String query = "INSERT INTO accountTable (id, ownerId, balance, approved, accountType) VALUES (?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, a.getId());
			pstmt.setInt(2, a.getOwnerId());
			pstmt.setDouble(3, a.getBalance());
			pstmt.setBoolean(4, a.isApproved());
			if (a.getType() == null) {
				pstmt.setString(6, "");
			} else {
				pstmt.setString(6, a.getType().toString());
			}
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return a;
	}

	public Account getAccount(Integer actId) {
		String actType = "";
		String query = "SELECT * FROM accountTable WHERE id=" + actId;
		Account a = new Account();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				a.setId(rs.getInt("id"));
				a.setOwnerId(rs.getInt("ownerId"));
				a.setBalance(rs.getDouble("balance"));
				a.setApproved(rs.getBoolean("approved"));
				actType = rs.getString("accountType");
				if (actType.equals("CHECKING")) {
					a.setType(AccountType.CHECKING);
				} else if (actType.equals("SAVINGS")) {
					a.setType(AccountType.SAVINGS);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return a;
	}

	public List<Account> getAccounts() {
		String actType = "";
		String query = "SELECT * FROM accountTable";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setOwnerId(rs.getInt("ownerId"));
				a.setBalance(rs.getDouble("balance"));
				a.setApproved(rs.getBoolean("approved"));
				actType = rs.getString("accountType");
				if (actType.equals("CHECKING")) {
					a.setType(AccountType.CHECKING);
				} else if (actType.equals("SAVINGS")) {
					a.setType(AccountType.SAVINGS);
				}
				accountList.add(a);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accountList;
	}

	public List<Account> getAccountsByUser(User u) {
		String actType = "";
		String query = "SELECT * FROM accountTable WHERE ownerId=" + u.getId();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Account a = new Account();
				a.setId(rs.getInt("id"));
				a.setOwnerId(rs.getInt("ownerId"));
				a.setBalance(rs.getDouble("balance"));
				a.setApproved(rs.getBoolean("approved"));
				actType = rs.getString("accountType");
				if (actType.equals("CHECKING")) {
					a.setType(AccountType.CHECKING);
				} else if (actType.equals("SAVINGS")) {
					a.setType(AccountType.SAVINGS);
				}
				accountList.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accountList;
	}

	public Account updateAccount(Account a) {
		String query = "UPDATE accountTable SET balance=?, WHERE id=?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setDouble(1, a.getBalance());
			pstmt.setInt(2, a.getOwnerId());
			pstmt.executeUpdate();
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean removeAccount(Account a) {
		String query = "DELETE accountTable WHERE id=?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, a.getId());
			pstmt.executeQuery();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
