package org.totalit.sbms.Utils;

public class TwoDecimalPlaceUtil {

    public static String formatDouble(Double amt){

        String amount = "";
        if(amt!=null) {
            amount = String.format("%.2f", amt);
        }
        return "$ " +amount;
    }
    public static Double toDecimal(Double amt){

        Double amount = null;
        if(amt!=null) {
            amount = Double.parseDouble(String.format("%.2f", amt));
        }
        return amount;
    }
}
