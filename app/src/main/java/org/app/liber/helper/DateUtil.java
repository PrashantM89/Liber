package org.app.liber.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static String date;
    private static SimpleDateFormat df;
    private static String pattern = "dd/MM/yyyy";

    public static String getDate(Date d){
        df = new SimpleDateFormat(pattern);
        date = df.format(d);
        return date;
    }
}
