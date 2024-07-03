package ru.sotnikov.components.terminals;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import ru.sotnikov.api.BackendLogic;
import ru.sotnikov.configuration.Configuration;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractTerminalPanel extends JPanel {
    private final int indent;

    private final int heightOfScreen;

    private final Font fontOfLabels;

    private final Label nameOfTerminalLabel;

    private final JComboBox<Ticket> ticketsBox;

    private final BackendLogic backendLogic;

    protected AbstractTerminalPanel(String nameOfTerminal, ApplicationContext applicationContext){
        Configuration.Properties properties = applicationContext.getBean("properties", Configuration.Properties.class);
        indent = properties.getIndent();
        heightOfScreen = properties.getHeightOfScreen();

        ticketsBox = applicationContext.getBean("ticketsBox", JComboBox.class);
        backendLogic = applicationContext.getBean(BackendLogic.class);

        setBounds(51, 0, properties.getWidthOfTerminal(),properties.getHeightOfTerminal());

        fontOfLabels = applicationContext.getBean("fontOfLabels", Font.class);

        this.nameOfTerminalLabel = new Label();
        this.nameOfTerminalLabel.setSize(new Dimension(110, indent - 1));
        this.nameOfTerminalLabel.setFont(fontOfLabels);
        this.nameOfTerminalLabel.setText(nameOfTerminal);
        this.nameOfTerminalLabel.setAlignment(Label.CENTER);

        nameOfTerminalLabel.setLocation((getWidth() - nameOfTerminalLabel.getWidth()) / 2, 1);
        add(nameOfTerminalLabel);
        add(ticketsBox);

        ticketsBox.setVisible(false);

        setLayout(null);
        setVisible(false);

        backendLogic.loadAllTickets(ticketsBox);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.drawRect(indent, indent, getWidth() - indent * 2, heightOfScreen);
    }
}
