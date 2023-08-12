package util;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

public class formatter {

    public static String    formatIntoMoney(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(amount);
    }

    public static Boolean isMoney(double amount) {
        try {
            double disposable = amount / 2;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setNumericOnly(JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume(); // ignore non-numeric characters
                }
            }
        });
    }

    public static void formatFieldAsNumber(JTextField field) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    double amount = Double.parseDouble(field.getText());

                    if (formatter.isMoney(amount)) {
                        field.setText(formatter.formatIntoMoney(Double.parseDouble(field.getText())));
                    }
                } catch (NumberFormatException ignore) {
                    System.out.println("Amount is already formatted");
                }
            }
        });
    }
}
