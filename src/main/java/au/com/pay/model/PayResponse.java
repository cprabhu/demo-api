
package au.com.pay.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "id",
    "amount",
    "subscription_type",
    "invoice_dates"
})
@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class PayResponse {

    /**
     * The Id Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private final String id;
    @JsonProperty("amount")
    private final Amount amount;
    /**
     * The Subscription_type Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("subscription_type")
    private final String subscriptionType;
    @JsonProperty("invoice_dates")
    private final List<String> invoiceDates;

}
