package util;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.*;
import model.ConversionRecord;

public class ConversionLogger {

	private static final String TXT_FILE = "conversion_history.txt";
	private static final String JSON_FILE = "conversion_history.json";
	
	private static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
			.setPrettyPrinting()
			.create();
	
	public static void logToTextFile(ConversionRecord record) {
		try (PrintWriter writer = new PrintWriter(
				new FileWriter(TXT_FILE, true))){
			writer.printf("[%s] %.2f %s -> %.2f %s (ExchangeRate: %.4f)%n",
					record.getTimestamp(),
					record.getOriginalAmount(),
					record.getBaseCurrency(),
					record.getConvertedAmount(),
					record.getTargetCurrency(),
					record.getExchangeRate()
				);
		} catch (IOException e) {
			System.out.println("Error al escribir en el archivo .txt: " +
					e.getMessage());
		}
	}
	
	public static void logToJsonFile(ConversionRecord record) {
		try {
			java.nio.file.Path path = java.nio.file.Paths.get(JSON_FILE);
			ArrayList<ConversionRecord> records;
			
			if (java.nio.file.Files.exists(path)) {
				String content = java.nio.file.Files.readString(path);
				ConversionRecord[] existing = gson.fromJson(
						content, ConversionRecord[].class);
				records = new java.util.ArrayList<>(
						java.util.Arrays.asList(existing));
			} else {
				records = new java.util.ArrayList<>();
			}
			
			records.add(record);
			String updatedJson = gson.toJson(records);
			
			try (FileWriter writer = new FileWriter(JSON_FILE)) {
				writer.write(updatedJson);
			}
		} catch (IOException e) {
			System.out.println("Error al escribir en el archivo .json"
					+ e.getMessage());;
		}
	}
	
	public static void logConversion(ConversionRecord record) {
		logToTextFile(record);
		logToJsonFile(record);
	}
	
	public static class LocalDateTimeAdapter implements JsonSerializer
		<LocalDateTime>, JsonDeserializer<LocalDateTime> {

		private static final DateTimeFormatter formatter =
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		@Override
		public LocalDateTime deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context)
				throws JsonParseException {
			return LocalDateTime.parse(json.getAsString(), formatter);
		}

		@Override
		public JsonElement serialize(LocalDateTime dateTime, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(dateTime.format(formatter));
		}
	}
}
