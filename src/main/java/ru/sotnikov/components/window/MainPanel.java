package ru.sotnikov.components.window;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sotnikov.configuration.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

@Component("panel")
public class MainPanel extends JPanel {

    private Button buttonNext, buttonPrev;
    private JPanel[] terminals;
    private int currentTerminal;

    @Autowired
    public MainPanel(ApplicationContext applicationContext){
        Configuration.Properties properties = applicationContext.getBean("properties", Configuration.Properties.class);
        Dimension sizeOfPanel = new Dimension(properties.getWidthOfWindow(), properties.getHeightOfWindow());

        setPreferredSize(sizeOfPanel);
        setSize(sizeOfPanel);

        this.buttonNext = new MyButton(">", this::nextTerminal);
        this.buttonPrev = new MyButton("<", this::prevTerminal);
        buttonNext.setLocation(this.getWidth() - buttonNext.getWidth(), (this.getHeight() - buttonNext.getHeight()) / 2);
        buttonPrev.setLocation(0, (this.getHeight() - buttonPrev.getHeight()) / 2);


        this.terminals = applicationContext.getBean("terminals", JPanel[].class);
        this.currentTerminal = 1;

        Arrays.stream(terminals).forEach(this::add);
        add(buttonNext);
        add(buttonPrev);
        terminals[currentTerminal].setVisible(true);

        this.setLayout(null);

    }

    private void nextTerminal(ActionEvent e){
        if(currentTerminal + 1 < terminals.length){
            terminals[currentTerminal].setVisible(false);
            currentTerminal++;
            terminals[currentTerminal].setVisible(true);
            repaint();
        }
    }

    private void prevTerminal(ActionEvent e) {
        if(currentTerminal - 1 >= 0){
            terminals[currentTerminal].setVisible(false);
            currentTerminal--;
            terminals[currentTerminal].setVisible(true);
            repaint();
        }
    }

}