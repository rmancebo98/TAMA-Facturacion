package util;

import javax.swing.*;

public class notifications {

    public static void showPopUpNotification(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void showErrorNotification(String message) {
        JOptionPane.showMessageDialog(null, message, "Ocurrió un error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInformativeNotification(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

}
