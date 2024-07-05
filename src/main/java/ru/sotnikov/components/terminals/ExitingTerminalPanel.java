package ru.sotnikov.components.terminals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component("terminal3")
public class ExitingTerminalPanel extends AbstractTerminalPanel {
    private final JLabel stateOfGateLabel;

    private final JButton driveOutToParkingButton;

    @Autowired
    public ExitingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 3", applicationContext);

        getMainLabel().setText("Приложите талончик");

        getGiveTicketButton().addActionListener(this::onGivingTicket);

        stateOfGateLabel = applicationContext.getBean("stateOfGateLabel", JLabel.class);

        driveOutToParkingButton = new JButton();
        driveOutToParkingButton.setSize(160, 50);
        driveOutToParkingButton.setFont(getFontOfLabels());
        driveOutToParkingButton.setText("Выехать");
        driveOutToParkingButton.addActionListener(this::onDrivingOutToParking);
        driveOutToParkingButton.setVisible(false);

        driveOutToParkingButton.setLocation(
                (getWidth() - getIndent() * 2 - driveOutToParkingButton.getWidth()),
                getHeight() - driveOutToParkingButton.getHeight() - getIndent() * 2
        );

        add(driveOutToParkingButton);
        add(stateOfGateLabel);

        defaultState();
    }

    private void onGivingTicket(ActionEvent actionEvent) {
        doSomeAndHandleExceptionInOtherThread(() -> {
            getGiveTicketButton().setVisible(false);
            getTicketsBox().setEnabled(false);

            getBackendLogic().checkPaymentOtherwiseThrowException((Ticket)getTicketsBox().getSelectedItem());
            getBackendLogic().removeTicketFromBox((Ticket)getTicketsBox().getSelectedItem());

            getMainLabel().setText("Талон оплачен, проезжайте");
            stateOfGateLabel.setText("Шлагбаум открыт");
            driveOutToParkingButton.setVisible(true);
            stateOfGateLabel.setForeground(new Color(75, 201, 96));
        });
    }

    private void onDrivingOutToParking(ActionEvent actionEvent) {
        defaultState();
    }

    protected void defaultState(){
        getTicketsBox().setVisible(true);
        getTicketsBox().setEnabled(true);
        driveOutToParkingButton.setVisible(false);
        stateOfGateLabel.setText("Шлагбаум закрыт");
        getMainLabel().setText("Приложите талончик");
        getGiveTicketButton().setVisible(true);
        stateOfGateLabel.setForeground(Color.red);
    }
}
