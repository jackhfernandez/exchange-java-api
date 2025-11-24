package service;

import java.net.URI;
import java.net.http.*;
import java.util.Map;

import com.google.gson.Gson;

import exception.ExchangeRateException;
import model.ExchangeRateResponse;

public class ExchangeRateService {

	private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
	private final String apiKey;
	private final HttpClient httpClient;
	private final Gson gson;

	public ExchangeRateService(String apiKey) {
		this.apiKey = apiKey;
		this.httpClient = HttpClient.newHttpClient();
		this.gson = new Gson();
	}

	public Map<String, Double> getAvailableCurrencies(String baseCurrency) throws ExchangeRateException {
		validateBaseCurrency(baseCurrency);

		URI uri = buildUri(baseCurrency);
		HttpRequest request = buildRequest(uri);

		try {
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return processResponse(response, baseCurrency);
		} catch (Exception e) {
			throw new ExchangeRateException("Error al obtener tasas de cambio para " + baseCurrency, e);
		}
	}

	private void validateBaseCurrency(String baseCurrency) throws ExchangeRateException {
		if (baseCurrency == null || baseCurrency.trim().isEmpty()) {
			throw new ExchangeRateException("La moneda base no puede estar vacia.");
		}
	}

	private URI buildUri(String baseCurrency) {
		return URI.create(BASE_URL + apiKey + "/latest/" + baseCurrency);
	}

	private HttpRequest buildRequest(URI uri) {
		return HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();
	}

	private Map<String, Double> processResponse(HttpResponse<String> response, String baseCurrency)
			throws ExchangeRateException {
		if (response.statusCode() != 200) {
			throw new ExchangeRateException("Error HTTP " +
					response.statusCode() + " al obtener tasas de cambio");
		}

		ExchangeRateResponse exchangeResponse = gson.fromJson(
				response.body(), ExchangeRateResponse.class);

		Map<String, Double> conversionRates = exchangeResponse.getConversion_rates();

		if (conversionRates == null || conversionRates.isEmpty()) {
			throw new ExchangeRateException("No se encontraron las tasas "
					+ "de conversion para " + baseCurrency);
		}
		
		return conversionRates;
	}
}
