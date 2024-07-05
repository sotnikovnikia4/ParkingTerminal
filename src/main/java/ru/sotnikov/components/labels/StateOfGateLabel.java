package ru.sotnikov.components.labels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.sotnikov.configuration.Configuration;

import javax.swing.*;
import java.awt.*;

@Component("stateOfGateLabel")
@Scope("prototype")
public class StateOfGateLabel extends JLabel {

    @Autowired
    public StateOfGateLabel(Font font, Configuration.Properties properties){
        setSize(new Dimension(properties.getWidthOfTerminal() / 2, properties.getIndent() * 2));
        setFont(font);
        setText("Шлагбаум закрыт");
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);

        setLocation((properties.getWidthOfTerminal() - getWidth()) / 2, properties.getIndent() + properties.getHeightOfScreen() - getHeight() - properties.getIndent() * 2);
    }
}
