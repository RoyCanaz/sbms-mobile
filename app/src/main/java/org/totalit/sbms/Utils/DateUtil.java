package org.totalit.sbms.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    public static Date getDateFromString(String date) {
        try {
            return fmt.parse(date);
        } catch (ParseException ex) {
            System.out.println("Error occurred");
        }
        throw new IllegalArgumentException("Un expected parameter provided :" + date);
    }

}
