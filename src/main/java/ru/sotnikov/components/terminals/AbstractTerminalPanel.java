package ru.sotnikov.components.terminals;

import lombok.AccessLevel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractTerminalPanel extends JPanel {
    private final int indent;

    private final int heightOfScreen;

    private final Font fontOfLabels;

    private final Label nameOfTerminalLabel;

    protected AbstractTerminalPanel(String nameOfTerminal){
        indent = 20;
        heightOfScreen = 400;

        setBounds(51, 0, 700 ,600);

        fontOfLabels = new Font(Font.SANS_SERIF, Font.PLAIN, indent);

        this.nameOfTerminalLabel = new Label();
        this.nameOfTerminalLabel.setSize(new Dimension(110, indent - 1));
        this.nameOfTerminalLabel.setFont(fontOfLabels);
        this.nameOfTerminalLabel.setText(nameOfTerminal);
        this.nameOfTerminalLabel.setAlignment(Label.CENTER);

        nameOfTerminalLabel.setLocation((getWidth() - nameOfTerminalLabel.getWidth()) / 2, 1);
        add(nameOfTerminalLabel);

        setLayout(null);
        setVisible(false);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.drawRect(indent, indent, getWidth() - indent * 2, heightOfScreen);
    }
}
