package main;

import javax.swing.JDialog;

public class Main {

	public static void main(String[] args) {
		
		try {
			frmMenu menu = new frmMenu();
			menu.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			menu.setLocationRelativeTo(null);
			menu.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
