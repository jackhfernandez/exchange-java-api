package model;

import java.time.LocalDateTime;

public class ConversionRecord {
	
	private LocalDateTime timeStamp;
	private String baseCurrency;
	private String targetCurrency;
	private double originalAmount;
	private double convertedAmount;
	private double exchangeRate;
	
	public ConversionRecord(LocalDateTime timeStamp, String baseCurrency,
				String targetCurrency, double originalAmount, double
				convertedAmount, double exchangeRate) {
		this.timeStamp = timeStamp;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.originalAmount = originalAmount;
		this.convertedAmount = convertedAmount;
		this.exchangeRate = exchangeRate;
	}

	public LocalDateTime getTimestamp() {
		return timeStamp;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public double getOriginalAmount() {
		return originalAmount;
	}

	public double getConvertedAmount() {
		return convertedAmount;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}
	
}
