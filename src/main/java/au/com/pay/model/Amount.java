
package au.com.pay.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "value",
    "currency"
})
@Getter
@Setter
public class Amount {

    /**
     * The Value Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("value")
    public Integer value = 0;
    /**
     * The Currency Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("currency")
    public String currency = "";

}
