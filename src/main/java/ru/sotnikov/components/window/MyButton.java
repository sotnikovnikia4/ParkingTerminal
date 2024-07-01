package ru.sotnikov.components.window;

import java.awt.*;
import java.awt.event.ActionListener;

//@Component("buttonPrev")
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
