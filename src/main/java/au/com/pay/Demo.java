package au.com.pay;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class Demo implements ApplicationListener<ApplicationReadyEvent> {
	
	@Value(value = "classpath:Input.txt")
	private Resource input;
	
	@Value(value = "classpath:out.csv")
	private Resource output;
	
	public List<String> parseInput() {
		List<String> inputStrList = null;
		try {
			inputStrList = Files.readAllLines(input.getFile().toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Demo.parseInput() " +inputStrList.size());
		return inputStrList;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		List<String> parsedList = parseInput().stream()
				.filter(transactionString -> transactionString.substring(7, 11).equalsIgnoreCase("1234"))
				.collect(Collectors.toList());
		
		parsedList.forEach(str -> {
			System.out.println("Demo.onApplicationEvent() quantity long sign "+str.substring(53,54));
			System.out.println("Demo.onApplicationEvent() quantity long "+Integer.parseInt(str.substring(54, 62)));
			System.out.println("Demo.onApplicationEvent() quantity short sign "+str.substring(63,64));
			System.out.println("Demo.onApplicationEvent() quantity short "+Integer.parseInt(str.substring(64, 74)));
		});
		
		try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("output/out.csv"));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Client_Information", "Product_Information", "Total_Transaction_Amount"))) {
			csvPrinter.printRecord("1","A", "10");
			csvPrinter.printRecord("2","B", "10");
			csvPrinter.printRecord("3","C", "10");
			csvPrinter.printRecord("4","E", "10");
			csvPrinter.flush();
			System.out.println("Demo.onApplicationEvent() csv written");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
