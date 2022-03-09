package com.revature.dao;

import java.sql.Connection;
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
	//private PreparedStatement pstmt;
	private ResultSet rs;
	List<Transaction> transList = new ArrayList<>();
	
	public TransactionDaoDB() {
		try {
			conn = ConnectionUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Transaction> getAllTransactions() {
		String query = "SELECT * FROM transactionsTable";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Transaction transaction = new Transaction();
				transaction.setTimestamp(rs.getTimestamp(1).toLocalDateTime());
				transaction.setSender((Account) rs.getObject("sender"));
				transaction.setRecipient((Account) rs.getObject("recipient"));
				transaction.setAmount(rs.getDouble("amount"));
				transaction.setType((TransactionType) rs.getObject("transactiontype"));
				if(rs.getObject("transactiontype") == TransactionType.TRANSFER) {
					transaction.setRecipient((Account) rs.getObject("recipient"));
				}
				transList.add(transaction);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transList;
	}

	

}
