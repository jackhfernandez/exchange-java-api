package main;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

import model.ConversionRecord;
import service.ExchangeRateService;
import util.ConversionCalculator;
import util.ConversionLogger;
import util.CurrencySelector;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		ExchangeRateService exchangeRateService = new ExchangeRateService();
		ConversionCalculator conversionCalculator = new ConversionCalculator();
		
		while (true) {
			try {
				String baseCurrency = CurrencySelector.selectBaseCurrency(scanner);
				
				if (baseCurrency == null) {
					System.out.println("Seleccion de moneda base invalida."
							+ "Terminando el programa.");
					break;
				}
				
				String targetCurrency = CurrencySelector.selectTargetCurrency(
						scanner, baseCurrency);
				
				if (targetCurrency == null) {
					System.out.println("Seleccion de moneda destino invalida."
							+ "Terminando el programa");
					break;
				}
				
				System.out.println("Ingrese la cantidad a convertir de " + 
						baseCurrency + " a " + targetCurrency + ":"
					);
				double amount = scanner.nextDouble();
				
				Map<String, Double> conversionRates = exchangeRateService
						.getAvailableCurrencies(baseCurrency);
				double convertedAmount = conversionCalculator.convert(
						amount, conversionRates, targetCurrency);
				double exchangeRate = conversionRates.get(targetCurrency);
				
				if (convertedAmount != 0) {
					System.out.printf("%.2f %s = %.2f %s%n", amount,
							baseCurrency, convertedAmount, targetCurrency);
					
					ConversionRecord record = new ConversionRecord(
							LocalDateTime.now(),
							baseCurrency,
							targetCurrency,
							amount,
							convertedAmount,
							exchangeRate
					);
					ConversionLogger.logConversion(record);
				}
				
				int option;
				do {
					System.out.println("Elija una opcion: ");
					System.out.println("1. Hacer otra conversion");
					System.out.println("2. Salir");
					System.out.println("\nSelecione una opcion: ");
					
					while (!scanner.hasNextInt()) {
						System.out.println("Entradad invalida. (Elija 1 o 2)");
						scanner.next();
					}
					
					option = scanner.nextInt();
					
					if (option == 1) {
						break;
					} else if (option == 2) {
						System.out.println("Gracias por usar el programa.");
						scanner.close();
						return;
					} else {
						System.out.println("Opcion invalida. (Elija 1 o 2");
					}
				} while (true);
				
			} catch (Exception e) {
				System.out.println("Error. " + e.getMessage());
			}
		}
		scanner.close();
	}
}
