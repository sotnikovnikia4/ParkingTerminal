package ru.sotnikov.components.panel1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sotnikov.api.BackendLogic;
import ru.sotnikov.util.TerminalException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component("terminal1")
public class TerminalPanel extends JPanel {
    private Label nameOfTerminal, takeTicketLabel, stateOfGateLabel;

    private Button takeTicketButton, driveToParkingButton;

    private final int indent;
    private final int heightOfScreen;

    private BackendLogic backendLogic;

    @Autowired
    public TerminalPanel(ApplicationContext applicationContext){
        backendLogic = applicationContext.getBean(BackendLogic.class);

        indent = 20;
        heightOfScreen = 400;
        setBounds(51, 0, 700 ,600);
        repaint();

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, indent);

        nameOfTerminal = new Label();
        nameOfTerminal.setSize(new Dimension(110, indent - 1));
        nameOfTerminal.setFont(font);
        nameOfTerminal.setText("Терминал 1");
        nameOfTerminal.setAlignment(Label.CENTER);

        takeTicketLabel = new Label();
        takeTicketLabel.setSize(new Dimension(getWidth() / 2, indent * 2));
        takeTicketLabel.setFont(font);
        takeTicketLabel.setText("Возьмите талон");
        takeTicketLabel.setAlignment(Label.CENTER);

        stateOfGateLabel = new Label();
        stateOfGateLabel.setSize(new Dimension(getWidth() / 2, indent * 2));
        stateOfGateLabel.setFont(font);
        stateOfGateLabel.setText("Шлагбаум закрыт");
        stateOfGateLabel.setAlignment(Label.CENTER);

        takeTicketButton = new Button();
        takeTicketButton.setSize(160, 50);
        takeTicketButton.setFont(font);
        takeTicketButton.setLabel("Получить талон");
        takeTicketButton.addActionListener(this::onTakeTicket);

        driveToParkingButton = new Button();
        driveToParkingButton.setSize(160, 50);
        driveToParkingButton.setFont(font);
        driveToParkingButton.setLabel("Заехать");
        driveToParkingButton.addActionListener(this::onDrivingToParing);

        nameOfTerminal.setLocation((getWidth() - nameOfTerminal.getWidth()) / 2, 1);
        takeTicketLabel.setLocation((getWidth() - takeTicketLabel.getWidth()) / 2, 50);
        stateOfGateLabel.setLocation((getWidth() - stateOfGateLabel.getWidth()) / 2, indent + heightOfScreen - stateOfGateLabel.getHeight() - 40);
        takeTicketButton.setLocation(
                (getWidth() - takeTicketButton.getWidth()) / 2,
                takeTicketLabel.getY() + takeTicketLabel.getHeight() + 1 + (stateOfGateLabel.getY() - (takeTicketLabel.getY() + takeTicketLabel.getHeight() + 1) - takeTicketButton.getHeight()) / 2
        );
        driveToParkingButton.setLocation(
                (getWidth() - driveToParkingButton.getWidth()) / 2,
                getHeight() - driveToParkingButton.getHeight() - 20
        );
        add(nameOfTerminal);
        add(takeTicketLabel);
        add(stateOfGateLabel);
        add(takeTicketButton);
        add(driveToParkingButton);
        setLayout(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.drawRect(indent, indent, getWidth() - indent * 2, heightOfScreen);
    }

    private void onTakeTicket(ActionEvent e){
        try{
            backendLogic.takeTicket();

            takeTicketButton.setVisible(false);
            takeTicketLabel.setText("Проезжайте");
            stateOfGateLabel.setText("Шлагбаум открыт");
        }catch(TerminalException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }


    }

    private void onDrivingToParing(ActionEvent e){
        takeTicketButton.setVisible(true);
        takeTicketLabel.setText("Возьмите талон");
        stateOfGateLabel.setText("Шлагбаум закрыт");
    }
}
