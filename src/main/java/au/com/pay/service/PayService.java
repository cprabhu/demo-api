package au.com.pay.service;

import java.time.LocalDate;

import au.com.pay.exception.PayNotProcessedException;
import au.com.pay.model.PayResponse;

public interface PayService {
	
	public PayResponse createSubscription(Integer amount, String currency, String type, LocalDate startDate, LocalDate endDate) throws PayNotProcessedException;

}
