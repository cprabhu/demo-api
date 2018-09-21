package au.com.pay.rest;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import au.com.pay.exception.PayNotProcessedException;
import au.com.pay.model.PayErrorResponse;
import au.com.pay.service.PayService;

@RestController
@RequestMapping("/payapp/api")
@Validated
public class PayRestController {

	private static final String SUBSCRIPTION_TYPE_REGEX_PATTERN = "(?i)(weekly|monthly|daily)";

	@Autowired
	private PayService service;

	@PostMapping(path = "/pay", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> createSubscription(@RequestParam("amount") Integer amount,
			@RequestParam(value = "currency", required = false) String currency,
			@Pattern(regexp = SUBSCRIPTION_TYPE_REGEX_PATTERN) @Valid @RequestParam("type") String type,
			@DateTimeFormat(pattern = "dd/MM/yyyy") @Valid @RequestParam("startDate") LocalDate startDate,
			@DateTimeFormat(pattern = "dd/MM/yyyy") @Valid @RequestParam("endDate") LocalDate endDate) {
		try {
			return new ResponseEntity<>(service.createSubscription(amount, currency, type, startDate, endDate),
					HttpStatus.OK);
		} catch (PayNotProcessedException e) {
			return new ResponseEntity<>(PayErrorResponse.of(e.getMessage()), e.getStatus());
		}
	}

}
