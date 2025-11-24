package config;

public class AppConfig {

	private static final String API_KEY_ENV = "EXCHANGE_API_KEY";
	private static final String API_KEY_PROPERTY = "exchange.api.key";
	
	public static String getApiKey() {
		String apiKey = System.getenv(API_KEY_ENV);
		if (apiKey == null || apiKey.isEmpty()) {
			return System.getProperty(API_KEY_PROPERTY);
		}
		
		if (apiKey == null || apiKey.isEmpty()) {
			throw new IllegalStateException(
				"API KEY no configurada. Por favor configura la variable de entorno" +
				"EXCHANGE_API_KEY o la propiedad del sistema exchange.api.key"
			);
		}
		return apiKey;
	}
	
	public static final class Files {
		public static final String CONVERSION_HISTORY_TXT = "conversion_history.txt";
		public static final String CONVERSION_HISTORY_JSON = "conversion_history.json";
	}
	
	public static final class UI{
		public static final int WINDOW_WITH = 530;
		public static final int WINDOW_HEIGHT = 370;
		public static final int CURRENCY_COLUMNS = 5;
	}
}
