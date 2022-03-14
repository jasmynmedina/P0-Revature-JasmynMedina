package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.utils.ConnectionUtil;

public class TransactionDaoDB implements TransactionDao {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	List<Transaction> transList = new ArrayList<>();
	AccountDao actDao = new AccountDaoDB();

	
	public TransactionDaoDB() {
		try {
			conn = ConnectionUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Transaction addTransaction(Transaction t) {
		if (t.getType().equals(TransactionType.TRANSFER)) {
		String query = "INSERT INTO transactiontable (sender, recipient, amount, transactiontype) VALUES (?, ?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(query);
			//pstmt.setTimestamp(1, Timestamp.valueOf(t.getTimestamp()));
			pstmt.setInt(1, t.getSender().getId());
			pstmt.setInt(2, t.getRecipient().getId());
			pstmt.setDouble(3, t.getAmount());
			if (t.getType() == null) {
				pstmt.setString(4, "");
			} else {
				pstmt.setString(4, t.getType().toString());
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} else {
		String query = "INSERT INTO transactiontable (sender, amount, transactiontype) VALUES (?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(query);
			//pstmt.setTimestamp(1, Timestamp.valueOf(t.getTimestamp()));
			pstmt.setInt(1, t.getSender().getId());
			pstmt.setDouble(2, t.getAmount());
			if (t.getType() == null) {
				pstmt.setString(3, "");
			} else {
				pstmt.setString(3, t.getType().toString());
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		return t;
	}
	public List<Transaction> getAllTransactions() {
		String query = "SELECT * FROM transactiontable";
		String transType = "";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Transaction transaction = new Transaction();
				//transaction.setTimestamp(rs.getTimestamp(1).toLocalDateTime());
				transaction.setSender(actDao.getAccount(rs.getInt("sender")));
				transaction.setRecipient(actDao.getAccount(rs.getInt("recipient")));
				transaction.setAmount(rs.getDouble("amount"));
				transType = rs.getString("transactiontype");
				if(transType.equals("TRANSFER")) {
					transaction.setType(TransactionType.TRANSFER);
					transaction.setRecipient(actDao.getAccount(rs.getInt("recipient")));
				} if (transType.equals("DEPOSIT")) {
					transaction.setType(TransactionType.DEPOSIT);
				} else if (transType.equals("WITHDRAWAL")) {
					transaction.setType(TransactionType.WITHDRAWAL);
				}
				transList.add(transaction);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transList;
	}
	

}
