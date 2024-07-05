package ru.sotnikov.components.terminals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sotnikov.api.BackendLogic;
import ru.sotnikov.util.TerminalException;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component("terminal1")
public class EnteringTerminalPanel extends AbstractTerminalPanel {
    private final JLabel stateOfGateLabel;

    private final JButton takeTicketButton, driveToParkingButton;

    @Autowired
    public EnteringTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 1", applicationContext);

        getMainLabel().setText("Возьмите талон");

        stateOfGateLabel = new JLabel();
        stateOfGateLabel.setSize(new Dimension(getWidth() / 2, getIndent() * 2));
        stateOfGateLabel.setFont(getFontOfLabels());
        stateOfGateLabel.setText("Шлагбаум закрыт");
        stateOfGateLabel.setVerticalAlignment(JLabel.CENTER);
        stateOfGateLabel.setHorizontalAlignment(JLabel.CENTER);

        takeTicketButton = new JButton();
        takeTicketButton.setSize(250, 50);
        takeTicketButton.setFont(getFontOfLabels());
        takeTicketButton.setText("Получить талон");
        takeTicketButton.addActionListener(this::onTakeTicket);

        driveToParkingButton = new JButton();
        driveToParkingButton.setSize(250, 50);
        driveToParkingButton.setFont(getFontOfLabels());
        driveToParkingButton.setText("Заехать");
        driveToParkingButton.addActionListener(this::onDrivingToParking);
        driveToParkingButton.setVisible(false);

        stateOfGateLabel.setLocation((getWidth() - stateOfGateLabel.getWidth()) / 2, getIndent() + getHeightOfScreen() - stateOfGateLabel.getHeight() - 40);
        takeTicketButton.setLocation(
                (getWidth() - takeTicketButton.getWidth()) / 2,
                getMainLabel().getY() + getMainLabel().getHeight() + 1 + (stateOfGateLabel.getY() - (getMainLabel().getY() + getMainLabel().getHeight() + 1) - takeTicketButton.getHeight()) / 2
        );
        driveToParkingButton.setLocation(
                (getWidth() - driveToParkingButton.getWidth()) / 2,
                getHeight() - driveToParkingButton.getHeight() - getIndent() * 2
        );
        add(stateOfGateLabel);
        add(takeTicketButton);
        add(driveToParkingButton);

        defaultState();
    }

    private void onTakeTicket(ActionEvent e){
        doSomeAndHandleExceptionInOtherThread(() -> {
            takeTicketButton.setVisible(false);

            Ticket ticket = getBackendLogic().takeTicket();
            getBackendLogic().addTicketToBox(ticket);

            getMainLabel().setText("Выдан талон " + ticket + ", проезжайте");
            stateOfGateLabel.setText("Шлагбаум открыт");
            driveToParkingButton.setVisible(true);
            stateOfGateLabel.setForeground(new Color(75, 201, 96));
        });
    }

    private void onDrivingToParking(ActionEvent e){
        defaultState();
    }

    protected void defaultState(){
        takeTicketButton.setVisible(true);
        getMainLabel().setText("Возьмите талон");
        stateOfGateLabel.setText("Шлагбаум закрыт");
        driveToParkingButton.setVisible(false);
        stateOfGateLabel.setForeground(Color.red);
    }
}
