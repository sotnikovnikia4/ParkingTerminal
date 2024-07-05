package ru.sotnikov.components.buttons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.ActionListener;

@Component("changeTerminalButton")
@Scope("prototype")
public class MyButton extends Button {
    public MyButton(String text, ActionListener listener){
        int size = 50;

        setBounds(0, 0, size, size);
        setLabel(text);

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, size);

        setFont(font);

        addActionListener(listener);
    }
}
