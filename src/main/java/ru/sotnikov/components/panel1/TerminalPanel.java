package ru.sotnikov.components.panel1;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component("terminal1")
public class TerminalPanel extends JPanel {
    private Label nameOfTerminal;
    private Label takeTicket;
    private Label stateOfGate;

    public TerminalPanel(){
        setBounds(51, 0, 700 ,600);
        repaint();

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

        nameOfTerminal = new Label();
        nameOfTerminal.setSize(new Dimension(getWidth(), 20));
        nameOfTerminal.setFont(font);
        nameOfTerminal.setText("Терминал 1");
        nameOfTerminal.setAlignment(Label.CENTER);

        takeTicket = new Label();
        takeTicket.setSize(new Dimension(getWidth(), 40));
        takeTicket.setFont(font);
        takeTicket.setText("Возьмите талон");
        takeTicket.setAlignment(Label.CENTER);

        stateOfGate = new Label();
        stateOfGate.setSize(new Dimension(getWidth(), 40));
        stateOfGate.setFont(font);
        stateOfGate.setText("Шлагбаум закрыт");
        stateOfGate.setAlignment(Label.CENTER);

        nameOfTerminal.setLocation(0, 0);
        takeTicket.setLocation(0, nameOfTerminal.getY() + nameOfTerminal.getHeight() + 2);
        add(nameOfTerminal);
        add(takeTicket);
        add(stateOfGate);
        setLayout(null);
    }

    @Override
    public void paint(Graphics g) {
        System.out.println(getSize());
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.drawRect(10, 10, getWidth());
    }
}
