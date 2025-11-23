package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Color;
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

	/**
	 * Create the dialog.
	 */
	public frmMenu() {
		setBounds(100, 100, 530, 370);
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
		txtBaseCurrency.setBounds(118, 144, 115, 43);
		contentPanel.add(txtBaseCurrency);
		txtBaseCurrency.setColumns(10);
		
		txtTargetCurrency = new JTextField();
		txtTargetCurrency.setColumns(10);
		txtTargetCurrency.setBounds(118, 204, 115, 43);
		contentPanel.add(txtTargetCurrency);
		
		JComboBox jcBaseCurrency = new JComboBox();
		jcBaseCurrency.setBounds(243, 144, 168, 43);
		contentPanel.add(jcBaseCurrency);
		
		JComboBox jcTargetCurrency = new JComboBox();
		jcTargetCurrency.setBounds(243, 204, 168, 43);
		contentPanel.add(jcTargetCurrency);
	}
}
