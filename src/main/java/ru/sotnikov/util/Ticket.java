package ru.sotnikov.util;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Ticket {
    private int number;
    private LocalDate checkIn;
}
