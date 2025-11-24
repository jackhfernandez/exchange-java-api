package util;

import java.util.Map;

import exception.ConversionException;

public class ConversionCalculator {

	private static final double ZERO = 0.0;
	
	public double convert(double amount, Map<String, Double> rates, String targetCurrency)
			throws ConversionException {
		
		validateAmount(amount);
		validateRates(rates);
		validateTargetCurrency(targetCurrency);
		
		if (!rates.containsKey(targetCurrency)) {
			throw new ConversionException(
					"No se encontro la tasa de conversion para " + targetCurrency
				);
		}
		
		double conversionRate = rates.get(targetCurrency);
		validateConversionRate(conversionRate);
		
		return amount * conversionRate;
	}
	
	private void validateAmount(double amount) throws ConversionException {
		if (amount < ZERO) {
			throw new ConversionException("El monto no puede ser negativo");
		}
	}
	
	private void validateRates(Map<String, Double> rates) throws ConversionException {
		if (rates == null || rates.isEmpty()) {
			throw new ConversionException("Las tasas de conversion no estan disponibles");
		}
	}
	
	private void validateTargetCurrency(String targetCurrency) throws ConversionException {
		if (targetCurrency == null || targetCurrency.isEmpty()) {
			throw new ConversionException("La moneda no puede estar vacia");
		}
	}
	
	private void validateConversionRate(double rate) throws ConversionException {
		if (rate <= ZERO) {
			throw new ConversionException("Tasa de conversion invalida" + rate);
		}
	}
}
