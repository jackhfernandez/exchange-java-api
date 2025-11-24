package util;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.*;
import config.AppConfig;
import model.ConversionRecord;

public class ConversionLogger {

	private static final String TXT_FILE = AppConfig.Files.CONVERSION_HISTORY_TXT;
	private static final String JSON_FILE = AppConfig.Files.CONVERSION_HISTORY_JSON;
	private static final String LOG_FORMAT =
			"[%s] %.2f %s -> %.2f %s (ExchangeRate: %.4f)%n";
	
	private final Gson gson;
	
	public ConversionLogger() {
		this.gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class,
						new LocalDateTimeAdapter())
				.setPrettyPrinting()
				.create();
	}
	
	public void logConversion(ConversionRecord record) {
		logToTextFile(record);
		logToJsonFile(record);
	}
	
	private void logToTextFile(ConversionRecord record) {
		
		try (PrintWriter writer = new PrintWriter(
				new FileWriter(TXT_FILE, true))){
			writer.printf(LOG_FORMAT,
					record.getTimestamp(),
					record.getOriginalAmount(),
					record.getBaseCurrency(),
					record.getConvertedAmount(),
					record.getTargetCurrency(),
					record.getExchangeRate()
				);
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo TXT: " +
					e.getMessage());
		}
	}
	
	private void logToJsonFile(ConversionRecord record) {
		try {
			Path path = Paths.get(JSON_FILE);
			List<ConversionRecord> records = loadExistingRecords(path);
			
			records.add(record);
			saveRecords(records);
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo JSON"
					+ e.getMessage());;
		}
	}
	
	private List<ConversionRecord> loadExistingRecords(Path path) throws IOException {
		
		if (Files.exists(path)) {
			String content = Files.readString(path);
			ConversionRecord[] existing = gson.fromJson(
					content, ConversionRecord[].class);
			return new ArrayList<>(Arrays.asList(existing));
		}
		return new ArrayList<>();
	}
	
	private void saveRecords(List<ConversionRecord> records) throws IOException {
		String updatedJson = gson.toJson(records);
		try (FileWriter writer = new FileWriter(JSON_FILE)) {
			writer.write(updatedJson);
		}
	}
}
