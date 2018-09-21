package au.com.pay.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public class CheckCCTransaction {
	
	@Getter
	private class Transaction {
		private String ccKey;
		
		private LocalDateTime transactionDate;
		
		private BigDecimal transactionAmount;

		public Transaction(String ccKey, String transactionDate, String transactionAmount) {
			this.ccKey = ccKey;
			this.transactionDate = LocalDateTime.parse(transactionDate);
			this.transactionAmount = new BigDecimal(transactionAmount);
		}
	}
	
	public List<String> saveTransactionValues(List<String> transactions, String date, String treshold) {
		final BigDecimal tresholdValue = new BigDecimal(treshold);
		final LocalDateTime dateValue = LocalDateTime.parse(date);
		
		/* Parse transactions to store a list of transaction objects */
		final List<Transaction> parsedTransactions = parseTransactions(transactions);
		
		/* Process each transaction to find a fraudulent CC by filtering the transactions
		 * of a given date, and adding the transaction amount for same CC.
		 */
		Map<String, BigDecimal> transactionList = new HashMap<>();
		List<String> resultList = new ArrayList<>();
		parsedTransactions.stream().filter(t -> t.getTransactionDate().equals(dateValue)).forEach(t -> {
			BigDecimal addedValue = transactionList.putIfAbsent(t.getCcKey(), t.getTransactionAmount());
			if (addedValue != null) {
				BigDecimal newValue = addedValue.add(t.getTransactionAmount());
				transactionList.put(t.getCcKey(), newValue);
			}
			if (transactionList.get(t.getCcKey()).compareTo(tresholdValue) == 1) {
				resultList.add(t.getCcKey());
			}
		});
		
		return resultList;
	}

	private List<Transaction> parseTransactions(List<String> transactions) {
		List<Transaction> parsedTransactions = new ArrayList<>();
		transactions.forEach(t -> {
			String[] tParams = t.split(",");
			Transaction singleTransaction = new Transaction(tParams[0], tParams[1], tParams[2]);
			parsedTransactions.add(singleTransaction);
		});
		return parsedTransactions;
	}

}
