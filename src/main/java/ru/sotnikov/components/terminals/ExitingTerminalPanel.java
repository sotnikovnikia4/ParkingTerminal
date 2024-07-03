package ru.sotnikov.components.terminals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component("terminal3")
public class ExitingTerminalPanel extends AbstractTerminalPanel {
    private final JLabel askToPayLabel, stateOfGateLabel;

    private final Button giveTicketButton, driveOutToParkingButton;

    @Autowired
    public ExitingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 3", applicationContext);

        askToPayLabel = new JLabel();
        askToPayLabel.setSize(new Dimension((int)(getWidth() / 1.1), getIndent() * 4));
        askToPayLabel.setFont(getFontOfLabels());
        askToPayLabel.setText("Приложите талончик");
        askToPayLabel.setHorizontalAlignment(JLabel.CENTER);
        askToPayLabel.setVerticalAlignment(JLabel.CENTER);

        giveTicketButton = applicationContext.getBean("giveTicketButton", Button.class);
        giveTicketButton.addActionListener(this::onGivingTicket);

        stateOfGateLabel = new JLabel();
        stateOfGateLabel.setSize(new Dimension(getWidth() / 2, getIndent() * 2));
        stateOfGateLabel.setFont(getFontOfLabels());
        stateOfGateLabel.setText("Шлагбаум закрыт");
        stateOfGateLabel.setHorizontalAlignment(JLabel.CENTER);
        stateOfGateLabel.setVerticalAlignment(JLabel.CENTER);

        driveOutToParkingButton = new Button();
        driveOutToParkingButton.setSize(160, 50);
        driveOutToParkingButton.setFont(getFontOfLabels());
        driveOutToParkingButton.setLabel("Заехать");
        driveOutToParkingButton.addActionListener(this::onDrivingToParing);
        driveOutToParkingButton.setVisible(false);

        askToPayLabel.setLocation(
                (getWidth() - askToPayLabel.getWidth()) / 2,
                50
        );

        add(askToPayLabel);
        add(giveTicketButton);

        defaultState();
    }

    private void onDrivingToParing(ActionEvent actionEvent) {

    }

    private void onGivingTicket(ActionEvent actionEvent) {
        
    }

    private void defaultState(){
        getTicketsBox().setVisible(true);
    }
}
