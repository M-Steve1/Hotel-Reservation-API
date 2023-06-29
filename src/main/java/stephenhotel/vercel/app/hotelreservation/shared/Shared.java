package stephenhotel.vercel.app.hotelreservation.shared;

import java.util.Calendar;
import java.util.Date;

public class Shared {

    public Shared() {}
    /**
     *
     * @param date Date should be written in format of yyyy/mm/dd
     * @return Date
     * @description It returns String converted to Date
     */
    public Date getDate(String date) {
        try{
            String[] formatDate = date.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(formatDate[0]), Integer.parseInt(formatDate[1]) - 1, Integer.parseInt(formatDate[2]));
            return calendar.getTime();
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
}
