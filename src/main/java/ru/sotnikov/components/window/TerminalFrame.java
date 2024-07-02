package ru.sotnikov.components.window;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component("window")
public class TerminalFrame extends JFrame {


    @Autowired
    public TerminalFrame(@Qualifier("panel") JPanel panel){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        setTitle("Терминал оплаты парковки");

        add(panel);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
        setResizable(false);
        setVisible(true);
    }
}
