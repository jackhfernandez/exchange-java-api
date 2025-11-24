package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import config.AppConfig;
import model.ConversionRecord;
import service.ExchangeRateService;
import util.ConversionCalculator;
import util.ConversionLogger;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import javax.swing.Box;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class frmMenu extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtBaseCurrency;
	private JTextField txtTargetCurrency;
	private JComboBox<String> jcBaseCurrency;
	private JComboBox<String> jcTargetCurrency;
	private ConversionLogger conversionLogger;
	private ExchangeRateService exchangeRateService;
	private ConversionCalculator conversionCalculator;
	private Map<String, Double> conversionRates;

	/**
	 * Create the dialog.
	 */
	public frmMenu() {
		setTitle(".: Fernandez Exchange Rate :.");
		setResizable(false);

		exchangeRateService = new ExchangeRateService(AppConfig.getApiKey());
		conversionCalculator = new ConversionCalculator();
		conversionLogger = new ConversionLogger();

		setBounds(100, 100, AppConfig.UI.WINDOW_WITH, AppConfig.UI.WINDOW_HEIGHT);
		contentPanel.setBounds(0, 0, 514, 331);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);

		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBounds(118, 24, 0, 0);
		contentPanel.add(verticalBox);
		{
			JLabel lblNewLabel = new JLabel("Fernandez Exchange Rate");
			lblNewLabel.setBounds(96, 50, 332, 43);
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
			lblNewLabel.setForeground(new Color(0, 128, 255));
			contentPanel.add(lblNewLabel);
		}
		getContentPane().setLayout(null);
		getContentPane().add(contentPanel);

		txtBaseCurrency = new JTextField();
		txtBaseCurrency.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtBaseCurrency.setBounds(118, 144, 115, 43);
		contentPanel.add(txtBaseCurrency);
		txtBaseCurrency.setColumns(10);

		txtTargetCurrency = new JTextField();
		txtTargetCurrency.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtTargetCurrency.setColumns(10);
		txtTargetCurrency.setBounds(118, 204, 115, 43);
		contentPanel.add(txtTargetCurrency);

		jcBaseCurrency = new JComboBox<>();
		jcBaseCurrency.setBounds(243, 144, 168, 43);
		contentPanel.add(jcBaseCurrency);

		jcTargetCurrency = new JComboBox<>();
		jcTargetCurrency.setBounds(243, 204, 168, 43);
		contentPanel.add(jcTargetCurrency);

		loadBaseCurrencies();
		setupListeners();
	}

	private void loadBaseCurrencies() {
		try {
			conversionRates = exchangeRateService.getAvailableCurrencies("USD");
			jcBaseCurrency.removeAllItems();
			for (String currency : conversionRates.keySet()) {
				jcBaseCurrency.addItem(currency);
			}

			if (jcBaseCurrency.getItemCount() > 0) {
				String firstCurrency = (String) jcBaseCurrency.getItemAt(0);
				loadTargetCurrencies(firstCurrency);
			}
		} catch (Exception e) {
			System.out.println("Error al cargar monedas " + e.getMessage());
		}
	}

	private void setupListeners() {
		jcBaseCurrency.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedCurrency = (String) jcBaseCurrency.getSelectedItem();
				if (selectedCurrency != null) {
					loadTargetCurrencies(selectedCurrency);
				}
			}
		});

		jcTargetCurrency.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				performConversion();
			}
		});

		txtBaseCurrency.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				performConversion();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				performConversion();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				performConversion();
			}
		});
	}

	private void loadTargetCurrencies(String baseCurrency) {
		try {
			conversionRates = exchangeRateService.getAvailableCurrencies(baseCurrency);
			jcTargetCurrency.removeAllItems();

			for (String currency : conversionRates.keySet()) {
				jcTargetCurrency.addItem(currency);
			}

			if (conversionRates.containsKey("PEN")) {
				jcTargetCurrency.setSelectedItem("PEN");
			}

			performConversion();

		} catch (Exception e) {
			System.out.println("Error al cargar monedas destino " + e.getMessage());
		}
	}

	private void performConversion() {
		try {
			String baseCurrency = (String) jcBaseCurrency.getSelectedItem();
			String targetCurrency = (String) jcTargetCurrency.getSelectedItem();
			String amountText = txtBaseCurrency.getText().trim();

			if (baseCurrency == null || targetCurrency == null || amountText.isEmpty()) {
				txtTargetCurrency.setText("");
				return;
			}

			double amount = Double.parseDouble(amountText);
			double convertedAmount = conversionCalculator.convert(amount, conversionRates, targetCurrency);

			txtTargetCurrency.setText(String.format("%.2f", convertedAmount));

			double exchangeRate = conversionRates.get(targetCurrency);
			ConversionRecord record = new ConversionRecord(
					LocalDateTime.now(),
					baseCurrency,
					targetCurrency,
					amount,
					convertedAmount,
					exchangeRate
				);
			conversionLogger.logConversion(record);

		} catch (NumberFormatException ex) {
			txtTargetCurrency.setText("");
		} catch (Exception ex) {
			txtTargetCurrency.setText("Error.");
		}
	}
}