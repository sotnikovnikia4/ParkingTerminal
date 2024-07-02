package ru.sotnikov.util;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class Ticket {
    private int number;
    private LocalDateTime checkIn;

    public String toString(){
        return String.format("%d: %d-%d-%d, %d:%s", number,
                checkIn.getYear(),
                checkIn.getMonthValue(),
                checkIn.getDayOfMonth(),
                checkIn.getHour(),
                checkIn.getMinute() < 10 ? "0" + checkIn.getMinute() : checkIn.getMinute() + "");
    }
}
