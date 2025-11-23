package model;

import java.util.Map;

public class ExchangeRateResponse {

	private String result;
	private String base_code;
	private Map<String, Double> conversion_rates;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getBaseCode() {
		return base_code;
	}
	public void setBaseCode(String baseCode) {
		this.base_code = baseCode;
	}
	public Map<String, Double> getConversion_rates() {
		return conversion_rates;
	}
	public void setConversion_rates(Map<String, Double> conversionRates) {
		this.conversion_rates = conversionRates;
	}
}
