package ru.sotnikov.components.terminals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sotnikov.util.TerminalException;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component("terminal3")
public class ExitingTerminalPanel extends AbstractTerminalPanel {
    private final JLabel stateOfGateLabel;

    private final Button driveOutToParkingButton;

    @Autowired
    public ExitingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 3", applicationContext);

        getMainLabel().setText("Приложите талончик");

        getGiveTicketButton().addActionListener(this::onGivingTicket);

        stateOfGateLabel = new JLabel();
        stateOfGateLabel.setSize(new Dimension(getWidth() / 2, getIndent() * 2));
        stateOfGateLabel.setFont(getFontOfLabels());
        stateOfGateLabel.setText("Шлагбаум закрыт");
        stateOfGateLabel.setHorizontalAlignment(JLabel.CENTER);
        stateOfGateLabel.setVerticalAlignment(JLabel.CENTER);

        driveOutToParkingButton = new Button();
        driveOutToParkingButton.setSize(160, 50);
        driveOutToParkingButton.setFont(getFontOfLabels());
        driveOutToParkingButton.setLabel("Выехать");
        driveOutToParkingButton.addActionListener(this::onDrivingOutToParking);
        driveOutToParkingButton.setVisible(false);

        driveOutToParkingButton.setLocation(
                (getWidth() - getIndent() - driveOutToParkingButton.getWidth()),
                getHeight() - driveOutToParkingButton.getHeight() - 20
        );
        stateOfGateLabel.setLocation((getWidth() - stateOfGateLabel.getWidth()) / 2, getIndent() + getHeightOfScreen() - stateOfGateLabel.getHeight() - 40);

        add(driveOutToParkingButton);
        add(stateOfGateLabel);

        defaultState();
    }

    private void onGivingTicket(ActionEvent actionEvent) {
        try{
            getGiveTicketButton().setVisible(false);
            getTicketsBox().setEnabled(false);

            getBackendLogic().checkPaymentOtherwiseThrowException((Ticket)getTicketsBox().getSelectedItem());
            getBackendLogic().removeTicketFromBox((Ticket)getTicketsBox().getSelectedItem());

            getMainLabel().setText("Талон оплачен, проезжайте");
            stateOfGateLabel.setText("Шлагбаум открыт");
            driveOutToParkingButton.setVisible(true);
        }catch(TerminalException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            defaultState();
        }
    }

    private void onDrivingOutToParking(ActionEvent actionEvent) {
        defaultState();
    }

    private void defaultState(){
        getTicketsBox().setVisible(true);
        getTicketsBox().setEnabled(true);
        driveOutToParkingButton.setVisible(false);
        stateOfGateLabel.setText("Шлагбаум закрыт");
        getMainLabel().setText("Приложите талончик");
        getGiveTicketButton().setVisible(true);
    }
}
