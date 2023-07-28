package vanille.vocabe.global.util;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public class DateFormatter {

    public static LocalDateTime from(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public static LocalDateTime LocalDateWithZeroTime(String date) {
        LocalDateTime localDateTime = from(date);
        return localDateTime.withHour(0).withMinute(0).withSecond(0);
    }
}
