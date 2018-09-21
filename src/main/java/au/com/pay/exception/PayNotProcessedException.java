package au.com.pay.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PayNotProcessedException extends Exception {

	private String message;

	private HttpStatus status;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3829460715248997341L;

	public PayNotProcessedException(String message, HttpStatus status) {
		super();
		this.message = message;
		this.status = status;
	}

}
