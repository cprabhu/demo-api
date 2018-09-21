package au.com.pay.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

/**
 * Query class to check if the current date falls under Australian Summer or not.
 * @author ChaitanyaPrabhu
 *
 */
public class SummerSeasonQuery implements TemporalQuery<Boolean> {

	@Override
	public Boolean queryFrom(TemporalAccessor temporal) {
		Boolean isSummer = false;

		LocalDate date = LocalDate.from(temporal);

		MonthDay novemberFirst = MonthDay.of(Month.NOVEMBER.getValue(), 1);
		MonthDay marchFirst = MonthDay.of(Month.MARCH.getValue(), 1);

		if (date.isAfter(novemberFirst.atYear(date.getYear())) && 
				date.isBefore(marchFirst.atYear(date.getYear()).plusYears(1))) {
			isSummer = true;
		}

		return isSummer;
	}

}
