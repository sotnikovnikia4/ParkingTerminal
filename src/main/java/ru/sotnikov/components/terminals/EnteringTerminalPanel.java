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
    private final Label takeTicketLabel, stateOfGateLabel;

    private final Button takeTicketButton, driveToParkingButton;

    private final BackendLogic backendLogic;

    @Autowired
    public EnteringTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 1", applicationContext);

        backendLogic = applicationContext.getBean(BackendLogic.class);

        takeTicketLabel = new Label();
        takeTicketLabel.setSize(new Dimension((int)(getWidth() / 1.1), getIndent() * 2));
        takeTicketLabel.setFont(getFontOfLabels());
        takeTicketLabel.setText("Возьмите талон");
        takeTicketLabel.setAlignment(Label.CENTER);

        stateOfGateLabel = new Label();
        stateOfGateLabel.setSize(new Dimension(getWidth() / 2, getIndent() * 2));
        stateOfGateLabel.setFont(getFontOfLabels());
        stateOfGateLabel.setText("Шлагбаум закрыт");
        stateOfGateLabel.setAlignment(Label.CENTER);

        takeTicketButton = new Button();
        takeTicketButton.setSize(160, 50);
        takeTicketButton.setFont(getFontOfLabels());
        takeTicketButton.setLabel("Получить талон");
        takeTicketButton.addActionListener(this::onTakeTicket);

        driveToParkingButton = new Button();
        driveToParkingButton.setSize(160, 50);
        driveToParkingButton.setFont(getFontOfLabels());
        driveToParkingButton.setLabel("Заехать");
        driveToParkingButton.addActionListener(this::onDrivingToParing);
        driveToParkingButton.setVisible(false);

        takeTicketLabel.setLocation((getWidth() - takeTicketLabel.getWidth()) / 2, 50);
        stateOfGateLabel.setLocation((getWidth() - stateOfGateLabel.getWidth()) / 2, getIndent() + getHeightOfScreen() - stateOfGateLabel.getHeight() - 40);
        takeTicketButton.setLocation(
                (getWidth() - takeTicketButton.getWidth()) / 2,
                takeTicketLabel.getY() + takeTicketLabel.getHeight() + 1 + (stateOfGateLabel.getY() - (takeTicketLabel.getY() + takeTicketLabel.getHeight() + 1) - takeTicketButton.getHeight()) / 2
        );
        driveToParkingButton.setLocation(
                (getWidth() - driveToParkingButton.getWidth()) / 2,
                getHeight() - driveToParkingButton.getHeight() - 20
        );
        add(takeTicketLabel);
        add(stateOfGateLabel);
        add(takeTicketButton);
        add(driveToParkingButton);

        defaultState();
    }

    private void onTakeTicket(ActionEvent e){
        try{
            Ticket ticket = backendLogic.takeTicket();

            getTicketsBox().insertItemAt(ticket, 0);

            takeTicketButton.setVisible(false);
            takeTicketLabel.setText("Выдан талон " + ticket + ", проезжайте");
            stateOfGateLabel.setText("Шлагбаум открыт");
            driveToParkingButton.setVisible(true);
        }catch(TerminalException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }


    }

    private void onDrivingToParing(ActionEvent e){
        defaultState();
    }

    private void defaultState(){
        takeTicketButton.setVisible(true);
        takeTicketLabel.setText("Возьмите талон");
        stateOfGateLabel.setText("Шлагбаум закрыт");
        driveToParkingButton.setVisible(false);
    }
}
