package util;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import service.ExchangeRateService;

public class CurrencySelector {

	public static String selectBaseCurrency(Scanner scanner) {
		return selectCurrency(scanner, "USD",
				"Seleccione la moneda base (ingrese el numero): ");
	}
	
	public static String selectTargetCurrency(
			Scanner scanner, String baseCurrency) {
		return selectCurrency(scanner, baseCurrency,
				"Seleccione el numero de la moneda destino: ");
	}

	private static String selectCurrency(Scanner scanner,
			String baseCurrency, String promptMessage) {
		ExchangeRateService exchangeRateService = new ExchangeRateService(promptMessage);
		try {
			Map<String, Double> conversionRates = exchangeRateService
					.getAvailableCurrencies(baseCurrency);
			String[] currencies = extractCurrencies(conversionRates);
			
			while (true) {
				System.out.println("Monedas disponibles para convertir: ");
				printCurrenciesInColumns(currencies, 5);
				
				System.out.println(promptMessage + " ");
				
				if (!scanner.hasNextInt()) {
					System.out.println("Entradad no valida."
							+ "Por favor ingrese un numero.");
					scanner.next();
					continue;
				}
				
				int selectedCurrencyIndex = scanner.nextInt() - 1;
				
				if (selectedCurrencyIndex < 0 ||
						selectedCurrencyIndex >= currencies.length) {
					System.out.println("Opcion invalida. Intente de nuevo");
				} else {
					return currencies[selectedCurrencyIndex];
				}
			}
		} catch (Exception e) {
			System.out.println("Error al obtener las monedas disponibles: "
					+ e.getMessage());
			return null;
		}
	}
	
	private static String[] extractCurrencies(
			Map<String, Double> conversionRates) {
		Set<String> keys = conversionRates.keySet();
		return keys.toArray( new String[0]);
	}
	
	private static void printCurrenciesInColumns(
			String[] currencies, int columns) {
		for (int i=0; i<currencies.length; i++) {
			System.out.printf("%-4d. %-10s", i+1, currencies[i]);
			
			if ((i+1) % columns == 0) {
				System.out.println();
			}
		}
		
		if (currencies.length % columns != 0) {
			System.out.println();
		}
	}
}
