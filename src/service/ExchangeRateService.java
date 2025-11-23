package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.google.gson.Gson;

import model.ExchangeRateResponse;

public class ExchangeRateService {

	String apiKey = "ADD_YOUR_OWN_APIKEY"; // Solicita e ingresa tu apikey
	
	public Map<String, Double> getAvailableCurrencies(String baseCurrency)
			throws Exception {
		URI uri = URI.create("https://v6.exchangerate-api.com/v6/" + apiKey +
				"/latest/" + baseCurrency);
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
		
		HttpResponse<String> response = client.send(request,
				HttpResponse.BodyHandlers.ofString());
		
		if (response.statusCode() == 200) {
			ExchangeRateResponse exchangeResponse = new
					Gson().fromJson(response.body(),ExchangeRateResponse.class);
			Map<String, Double> conversionRates =
					exchangeResponse.getConversion_rates();
			
			if (conversionRates == null || conversionRates.isEmpty()) {
				throw new Exception("Error. No se encontraron las tasas "
						+ "de conversion para la moneda base " + baseCurrency);
			}
			return conversionRates;
		} else {
			throw new Exception("Error al obtener las monedas disponibles: " + 
					response.statusCode());
		}
	}
}
