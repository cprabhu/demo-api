package au.com.pay.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import au.com.pay.model.PayErrorResponse;

@RestControllerAdvice
public class PayControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<PayErrorResponse> handleConstraintVioldationException(ConstraintViolationException ex,
			WebRequest request) {
		return new ResponseEntity<PayErrorResponse>(
				PayErrorResponse.of(request.getParameter("type")
						+ " is an invalid subscription type. Please use a weekly, monthly or a daily type"),
				HttpStatus.BAD_REQUEST);
	}

}
