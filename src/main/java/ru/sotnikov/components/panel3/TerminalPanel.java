package ru.sotnikov.components.panel3;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component("terminal3")
public class TerminalPanel extends JPanel {

    public TerminalPanel(){
        setLocation(100, 10);
        setSize(new Dimension(400, 400));
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 100, 200);
    }
}
