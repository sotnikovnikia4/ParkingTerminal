package ru.sotnikov.components.terminals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("terminal3")
public class ExitingTerminalPanel extends AbstractTerminalPanel {

    @Autowired
    public ExitingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 3");


    }
}
