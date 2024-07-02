package ru.sotnikov.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sotnikov.util.TerminalException;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component("backendLogic")
@RequiredArgsConstructor
public class BackendLogic {
    private int id = 1;

    private final JComboBox<Ticket> ticketsBox;

    public Ticket takeTicket(){
        Ticket ticket = Ticket.builder().number(id).checkIn(LocalDateTime.now()).build();
        ticketsBox.insertItemAt(ticket, 0);
        System.out.println("Был выдан билет " + ticket);
        id++;

        return ticket;
    }

    public int getSumOfPayment(Ticket selectedItem) {
        if(selectedItem == null){
            throw new TerminalException("Не выбран талон, чтобы приложить");
        }

        System.out.println("Расчет оплаты..." + selectedItem);
        return 2500;
    }

    public void pay(int costOfParking, Ticket selectedItem, Object card) {

    }

    public int getTimeOfLeaving() {
        return 0;
    }
}
