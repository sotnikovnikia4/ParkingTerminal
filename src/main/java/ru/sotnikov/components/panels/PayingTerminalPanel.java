package ru.sotnikov.components.panels;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("terminal2")
public class PayingTerminalPanel extends AbstractTerminalPanel {

    public PayingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 2");
    }
}
