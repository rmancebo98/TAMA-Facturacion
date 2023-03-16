import javax.swing.*;

public class notifications {

    public static void showPopUpNotification(String message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
}
