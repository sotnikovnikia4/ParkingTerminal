package ru.sotnikov.components.panels;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("terminal3")
public class ExitingTerminalPanel extends AbstractTerminalPanel {

    public ExitingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 3");
    }
}
