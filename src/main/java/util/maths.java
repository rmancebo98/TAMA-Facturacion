package util;

import documentGenerator.word;

public class maths {

    public static String getITBIS(String amount) {
        double amountFromJson;

        amount = amount.replace("$", "");
        amount = amount.replace(",", "");
        amount = amount.replace(".00", "");
        amountFromJson = Double.parseDouble(amount);
        //Here we add the amount specified
        word.addAmount(amountFromJson);
        amountFromJson = (amountFromJson * 18) / 100;
        //Here we add the calculated ITBIS
        word.addITBIS(amountFromJson);
        return formatter.formatIntoMoney(amountFromJson);
    }

    public static String getAmountWithITBIS(String amount) {
        double amountFromJson;
        double originalAmount;

        amount = amount.replace("$", "");
        amount = amount.replace(",", "");
        amount = amount.replace(".00", "");
        amountFromJson = Double.parseDouble(amount);
        amountFromJson = (amountFromJson * 18) / 100 + amountFromJson;

        return formatter.formatIntoMoney(amountFromJson);
    }
}
