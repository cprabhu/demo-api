package au.com.pay.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import au.com.pay.exception.PayNotProcessedException;
import au.com.pay.model.Amount;
import au.com.pay.model.PayResponse;

/**
 * This is the service class which creates the subscription based on the input
 * parameters.
 * 
 * @author ChaitanyaPrabhu
 *
 */
@Service
public class PayServiceImpl implements PayService {

	@Override
	public PayResponse createSubscription(Integer amount, String currency, String type, LocalDate startDate,
			LocalDate endDate) throws PayNotProcessedException {
		List<String> invoiceDates = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		/* The period difference between shouldn't be more than 3 months */
		Period dateDuration = Period.between(startDate, endDate);
		if (dateDuration.toTotalMonths() > 3 || (dateDuration.toTotalMonths() == 3 && dateDuration.getDays() > 0))
			throw new PayNotProcessedException(
					"Duration between the period is more than 3 months. Please ensure that the period is for 3 months.",
					HttpStatus.BAD_REQUEST);

		switch (type.toLowerCase()) {
		
		case "weekly":
			LocalDate firstTuesday = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
			invoiceDates.add(firstTuesday.format(formatter));
			LocalDate nextTuesday = firstTuesday.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
			while (nextTuesday.isBefore(endDate)) {
				invoiceDates.add(nextTuesday.format(formatter));
				nextTuesday = nextTuesday.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
			}
			break;
			
		case "monthly":
			LocalDate firstInvoiceDate = startDate.with(for20thOfMonth());
			if (firstInvoiceDate.isBefore(startDate)) {
				firstInvoiceDate = firstInvoiceDate.plusMonths(1);
			}
			
			while (firstInvoiceDate.isBefore(endDate) || firstInvoiceDate.isEqual(endDate)) {
				invoiceDates.add(firstInvoiceDate.format(formatter));
				firstInvoiceDate = firstInvoiceDate.plusMonths(1);
			}
			
			break;
			
		case "daily":
			invoiceDates.add(startDate.format(formatter));
			LocalDate nextInvoiceDay = startDate.plusDays(1);
			while (nextInvoiceDay.isBefore(endDate) || nextInvoiceDay.isEqual(endDate)) {
				invoiceDates.add(nextInvoiceDay.format(formatter));
				nextInvoiceDay = nextInvoiceDay.plusDays(1);
			}
			break;
			
		default:
			throw new PayNotProcessedException(
					type + " is an invalid subscription type. Please use a weekly, monthly or a daily type",
					HttpStatus.BAD_REQUEST);
		}
		
		// Keep the ID of the invoice unique by generating a UUID
		String uuid = UUID.randomUUID().toString();
		// Create the currency value
		Amount amountResponse = new Amount();
		amountResponse.setValue(amount);
		amountResponse.setCurrency(currency != null ? currency : "AUD");

		return PayResponse.of(uuid, amountResponse, type.toUpperCase(), invoiceDates);
	}

	/**
	 * Temporal to get 20th of the month on the given date
	 * @return
	 */
	private TemporalAdjuster for20thOfMonth() {
		return (temporal) -> temporal.with(ChronoField.DAY_OF_MONTH, 20);
	}

}
