package util;

public class format {
    public static String formatAsMoney(String amount) {
        String formattedAmount;

        if (amount.contains("$") && !amount.contains(".")) {
            amount = amount.replace("$", "");
            formattedAmount = "$" + String.format("%.2f", Double.parseDouble(amount));
        } else if (!amount.contains("$") && !amount.contains(".")) {
            formattedAmount = "$" + String.format("%.2f", Double.parseDouble(amount));
        } else if (!amount.contains("$") && amount.contains(".")) {
            formattedAmount = "$" + amount;
        } else {
            formattedAmount = amount;
        }
        return formattedAmount;
    }
}
