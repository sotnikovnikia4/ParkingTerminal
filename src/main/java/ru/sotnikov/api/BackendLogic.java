package ru.sotnikov.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sotnikov.util.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Component("backendLogic")
public class BackendLogic {
    private int id = 1;
    private final Map<Integer, Ticket> tickets;

    @Autowired
    public BackendLogic(Map<Integer, Ticket> tickets){
        this.tickets = tickets;
    }

    public void takeTicket(){
        tickets.put(id, Ticket.builder().number(id).checkIn(LocalDateTime.now()).build());
        System.out.println("Был выдан билет " + tickets.get(id));
        id++;
    }
}
