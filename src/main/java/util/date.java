package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class date {

    static String datePattern = "dd-MM-yyyy";

    public static String today() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return today.format(formatter);
    }

    public static String getLastDayOfYear() {
        LocalDate lastDayOfYear = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return lastDayOfYear.plusYears(0).format(formatter);
    }
}
