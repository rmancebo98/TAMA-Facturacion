package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class date {

    public static String today(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
       return today.format(formatter);
    }
}
