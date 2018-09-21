package au.com.pay.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
	"message" 
})
@Getter
@RequiredArgsConstructor(staticName="of")
public class PayErrorResponse {

	@JsonProperty("message")
	private final String message;

}
