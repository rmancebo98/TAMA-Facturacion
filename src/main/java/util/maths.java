package util;

import documentGenerator.word;

import java.util.ArrayList;
import java.util.Map;

public class maths {


    public static ArrayList<Double> amounts = new ArrayList<>();
    public static ArrayList<Double> ITBIS = new ArrayList<>();
    public static ArrayList<Double> amountWithITBIS = new ArrayList<>();

    public static void addAmount(double amount) {
        amounts.add(amount);
    }


    public static void addITBIS(double ITBISFromAmount) {
        ITBIS.add(ITBISFromAmount);
    }


    public static String getITBIS(String amount) {
        double amountFromJson;

        amount = amount.replace("$", "");
        amount = amount.replace(",", "");
        amount = amount.replace(".00", "");
        amountFromJson = Double.parseDouble(amount);
        //Here we add the amount specified
        addAmount(amountFromJson);
        amountFromJson = (amountFromJson * 18) / 100;
        //Here we add the calculated ITBIS
        addITBIS(amountFromJson);
        return formatter.formatIntoMoney(amountFromJson);
    }

    public static String getAmountWithITBIS(String amount) {
        double amountFromJson;

        amount = amount.replace("$", "");
        amount = amount.replace(",", "");
        amount = amount.replace(".00", "");
        amountFromJson = Double.parseDouble(amount);
        amountFromJson = (amountFromJson * 18) / 100 + amountFromJson;
        amountWithITBIS.add(amountFromJson);
        return formatter.formatIntoMoney(amountFromJson);
    }


    private static String sumAllValues(ArrayList<Double> listToSum) {
        double total = 0;
        for (double amount : listToSum) {
            total = total + amount;
        }
        return formatter.formatIntoMoney(total);
    }

    public static String calculateTotalWithITBIS(Map<String, Map<String, String>> receiptJson) {
        amountWithITBIS.clear();
        Map<String, String> firstFee = receiptJson.get("PRIMER HONORARIO:");
        getAmountWithITBIS(firstFee.get("Monto: "));
        Map<String, String> secondFee = receiptJson.get("SEGUNDO HONORARIO:");
        if (secondFee != null) {
            getAmountWithITBIS(secondFee.get("Monto: "));
        }
        Map<String, String> thirdFee = receiptJson.get("TERCER HONORARIO:");
        if (thirdFee != null) {
            getAmountWithITBIS(thirdFee.get("Monto: "));
        }
        Map<String, String> forthFee = receiptJson.get("CUARTO HONORARIO:");
        if (forthFee != null) {
            getAmountWithITBIS(forthFee.get("Monto: "));
        }
        Map<String, String> fifthFee = receiptJson.get("QUINTO HONORARIO:");
        if (fifthFee != null) {
            getAmountWithITBIS(fifthFee.get("Monto: "));
        }
        Map<String, String> sixthFee = receiptJson.get("SEXTO HONORARIO:");
        if (sixthFee != null) {
            getAmountWithITBIS(sixthFee.get("Monto: "));
        }
        Map<String, String> seventhFee = receiptJson.get("SEPTIMO HONORARIO:");
        if (seventhFee != null) {
            getAmountWithITBIS(seventhFee.get("Monto: "));
        }
        Map<String, String> eighthFee = receiptJson.get("OCTAVO HONORARIO:");
        if (eighthFee != null) {
            getAmountWithITBIS(eighthFee.get("Monto: "));
        }
        Map<String, String> ninethFee = receiptJson.get("NOVENO HONORARIO:");
        if (ninethFee != null) {
            getAmountWithITBIS(ninethFee.get("Monto: "));
        }
        Map<String, String> tenthFee = receiptJson.get("DECIMO HONORARIO:");
        if (tenthFee != null) {
            getAmountWithITBIS(tenthFee.get("Monto: "));
        }
        return sumAllValues(amountWithITBIS);
    }
}
