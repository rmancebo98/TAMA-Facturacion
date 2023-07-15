package util;

import javax.swing.*;

public class notifications {

    public static void showPopUpNotification(String message, String title) {
        ImageIcon icon = new ImageIcon("src/main/resources/icons/done.png");
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE, icon);
    }

    public static void showErrorNotification(String message) {
        JOptionPane.showMessageDialog(null, message, "Ocurrió un error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInformativeNotification(String message, String customMessage) {
        JOptionPane.showMessageDialog(null, message, customMessage, JOptionPane.INFORMATION_MESSAGE);
    }

}
