package ru.sotnikov.components.terminals;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import ru.sotnikov.configuration.Configuration;

import javax.swing.*;
import java.awt.*;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractTerminalPanel extends JPanel {
    private final int indent;

    private final int heightOfScreen;

    private final Font fontOfLabels;

    private final Label nameOfTerminalLabel;

    protected AbstractTerminalPanel(String nameOfTerminal, ApplicationContext applicationContext){
        Configuration.Properties properties = applicationContext.getBean("properties", Configuration.Properties.class);
        indent = properties.getIndent();
        heightOfScreen = properties.getHeightOfScreen();

        setBounds(51, 0, properties.getWidthOfTerminal(),properties.getHeightOfTerminal());

        fontOfLabels = applicationContext.getBean("fontOfLabels", Font.class);

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
