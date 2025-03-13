package com.mrjwx.weixin.util;

import java.text.DecimalFormat;

public class MathUtil {
    public static String calculateCommission(String realPostFee, String commissionRate) {
        double fee = Double.parseDouble(realPostFee);
        double rate = Double.parseDouble(commissionRate) / 100;
        double commission = fee * rate;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(commission);
    }
    
}
