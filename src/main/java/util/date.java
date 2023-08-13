package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class date {

    public static String today() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return today.format(formatter);
    }

    public static String getLastDayOfYear() {
        LocalDate lastDayOfYear = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return lastDayOfYear.plusYears(1).format(formatter);
    }
}
