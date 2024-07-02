package ru.sotnikov.api;

import org.springframework.stereotype.Component;

@Component("backendLogic")
public class BackendLogic {

    public void takeTicket(){
        System.out.println("Был выдан билет");
    }
}
