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
        return String.format("Талон %d: %s", number, checkIn.toString());
    }
}
