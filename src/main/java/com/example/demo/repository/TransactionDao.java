package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ErrorRequest;
import com.example.demo.entity.TransactionResponse;
import com.example.demo.entity.TransctionRequest;
import com.example.demo.utils.MaskAccountNumber;

@Repository
public class TransactionDao {
	
	Logger logger=LoggerFactory.getLogger(TransactionDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<TransactionResponse> getAllTransactionhistory(TransctionRequest trequest) throws Exception {
		Optional<List<TransactionResponse>> transactionResponses=null;
		try {
		String newstartDate=trequest.getStartDate().split("T")[0];
		String newendDate=trequest.getEndDate().split("T")[0];
		String sql = "SELECT sid, account_number, sender_name, receiver_name, payment FROM transaction "
				+ "where account_number='"+trequest.getAccountNumber()+"'"
				+ "and transactionDate between '"+newstartDate+"' and '"+newendDate+"'"
		        +" ORDER BY transactionDate DESC";
		
		logger.info("Executing SQL query to fetch transaction history for account number: "+MaskAccountNumber.maskAccountNumber(trequest.getAccountNumber()));
		transactionResponses=Optional.ofNullable(jdbcTemplate.query(sql, (rs, rowNum) -> new TransactionResponse(
				rs.getInt("sid"),
				rs.getString("account_number"),
				rs.getString("sender_name"),
				rs.getString("receiver_name"),
				rs.getString("payment")
		)));
		if(transactionResponses.isEmpty() || transactionResponses.get().isEmpty()) {
			logger.error("No transaction history found for account number: "+MaskAccountNumber.maskAccountNumber(trequest.getAccountNumber()));
			throw new Exception("No transaction history found for account number: "+MaskAccountNumber.maskAccountNumber(trequest.getAccountNumber()));
		}
		return transactionResponses.get();
	  }catch(Exception e) {
		   logger.error("Error while fetching transaction history for account number: "+e.getMessage());
		   //throw new EmployeeNotFoundException(new ErrorRequest("500","No transaction history found for account number: "+trequest.getAccountNumber()));
		   throw e;
	   }
	}
	
}
