package au.com.pay.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import au.com.pay.DemoApiApplication;
import au.com.pay.model.PayErrorResponse;
import au.com.pay.model.PayResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class PayApiControllerTest {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	private String createUrlWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	@Test
	public void testWeeklySubscription_ForOneMonth() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "10");
		map.add("type", "weekly");
		map.add("startDate", "01/05/2018");
		map.add("endDate", "30/05/2018");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"), entity,
				PayResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().getAmount().getValue()).isEqualTo(10);
		assertThat(response.getBody().getSubscriptionType()).isEqualTo("WEEKLY");
		assertThat(response.getBody().getInvoiceDates()).hasSize(4);
	}

	@Test
	public void testWeeklySubscription_ForOneMonthWith5InvoiceDates() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "20");
		map.add("type", "weekly");
		map.add("startDate", "30/04/2018");
		map.add("endDate", "30/05/2018");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"), entity,
				PayResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().getAmount().getValue()).isEqualTo(20);
		assertThat(response.getBody().getSubscriptionType()).isEqualTo("WEEKLY");
		assertThat(response.getBody().getInvoiceDates()).hasSize(5);
	}

	@Test
	public void testMonthlySubscription_For1Month() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "20");
		map.add("type", "monthly");
		map.add("startDate", "01/05/2018");
		map.add("endDate", "30/05/2018");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"), entity,
				PayResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().getAmount().getValue()).isEqualTo(20);
		assertThat(response.getBody().getSubscriptionType()).isEqualTo("MONTHLY");
		assertThat(response.getBody().getInvoiceDates()).hasSize(1);
	}

	@Test
	public void testMonthlySubscription_For3Months() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "20");
		map.add("type", "monthly");
		map.add("startDate", "01/05/2018");
		map.add("endDate", "01/08/2018");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"), entity,
				PayResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().getAmount().getValue()).isEqualTo(20);
		assertThat(response.getBody().getSubscriptionType()).isEqualTo("MONTHLY");
		assertThat(response.getBody().getInvoiceDates()).hasSize(3);
	}

	@Test
	public void testMonthlySubscription_For3MonthsPlus() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "20");
		map.add("type", "monthly");
		map.add("startDate", "01/05/2018");
		map.add("endDate", "30/08/2018");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayErrorResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"),
				entity, PayErrorResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(400);
		assertThat(response.getBody().getMessage()).isEqualTo(
				"Duration between the period is more than 3 months. Please ensure that the period is for 3 months.");
	}

	@Test
	public void testDailySubscription_ForOneMonth() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "20");
		map.add("type", "daily");
		map.add("startDate", "01/05/2018");
		map.add("endDate", "30/05/2018");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"), entity,
				PayResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().getSubscriptionType()).isEqualTo("DAILY");
		assertThat(response.getBody().getInvoiceDates()).hasSize(30);
	}

	@Test
	public void testInvalidSubscription_InvalidType() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "20");
		map.add("type", "monthly2");
		map.add("startDate", "01/05/2018");
		map.add("endDate", "30/05/2018");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayErrorResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"),
				entity, PayErrorResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(400);
		assertThat(response.getBody().getMessage())
				.isEqualTo("monthly2 is an invalid subscription type. Please use a weekly, monthly or a daily type");
	}

	@Test
	public void testInvalidSubscription_InvalidDate() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "20");
		map.add("type", "monthly");
		map.add("startDate", "01/05/20189");
		map.add("endDate", "30/05/2018");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayErrorResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"),
				entity, PayErrorResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(400);
	}

	@Test
	public void testInvalidSubscription_MoreThan3Months() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "20");
		map.add("type", "monthly");
		map.add("startDate", "01/05/2018");
		map.add("endDate", "30/05/2019");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayErrorResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"),
				entity, PayErrorResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(400);
		assertThat(response.getBody().getMessage()).isEqualTo(
				"Duration between the period is more than 3 months. Please ensure that the period is for 3 months.");
	}
	
	@Test
	public void testMonthlySubscriptionInUSD_For1Month() {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("amount", "20");
		map.add("type", "monthly");
		map.add("startDate", "01/05/2018");
		map.add("endDate", "30/05/2018");
		map.add("currency", "USD");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<PayResponse> response = restTemplate.postForEntity(createUrlWithPort("/payapp/api/pay"), entity,
				PayResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().getAmount().getValue()).isEqualTo(20);
		assertThat(response.getBody().getAmount().getCurrency()).isEqualTo("USD");
		assertThat(response.getBody().getSubscriptionType()).isEqualTo("MONTHLY");
		assertThat(response.getBody().getInvoiceDates()).hasSize(1);
	}


}
