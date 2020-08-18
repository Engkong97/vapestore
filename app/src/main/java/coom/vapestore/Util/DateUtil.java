package coom.vapestore.Util;

import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String indonesiaFormat(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy");
        String tanggalStr = formatter.format(new Date());
        return tanggalStr;
    }
}
