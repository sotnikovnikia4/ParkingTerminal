package ru.sotnikov.components.terminals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sotnikov.api.BackendLogic;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

@Component("terminal2")
public class PayingTerminalPanel extends AbstractTerminalPanel {
    private final Label askToPayLabel;
    private final Button askToTakeFineTicketButton, giveTicketButton, payButton;
    private final JComboBox<Ticket> ticketsBox;

    private final BackendLogic backendLogic;
    private final Map<Integer, Ticket> tickets;

    @Autowired
    public PayingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 2", applicationContext);

        backendLogic = applicationContext.getBean(BackendLogic.class);
        tickets = applicationContext.getBean("tickets", Map.class);

        askToPayLabel = new Label();
        askToPayLabel.setSize(new Dimension(getWidth() / 2, getIndent() * 2));
        askToPayLabel.setFont(getFontOfLabels());
        askToPayLabel.setText("Приложите талончик");
        askToPayLabel.setAlignment(Label.CENTER);

        askToTakeFineTicketButton = new Button();
        askToTakeFineTicketButton.setSize(160, 50);
        askToTakeFineTicketButton.setFont(getFontOfLabels());
        askToTakeFineTicketButton.setLabel("Потерял талон");
        askToTakeFineTicketButton.addActionListener(this::onTakingFineTicket);

        giveTicketButton = applicationContext.getBean("giveTicketButton", Button.class);
        giveTicketButton.addActionListener(this::onGivingTicket);

        payButton = new Button();
        payButton.setSize(300, 50);
        payButton.setFont(getFontOfLabels());
        payButton.setLabel("Приложить карту для оплаты");
        payButton.addActionListener(this::onPaying);
        payButton.setVisible(false);

        ticketsBox = applicationContext.getBean("ticketsBox", JComboBox.class);

        askToPayLabel.setLocation(
                (getWidth() - askToPayLabel.getWidth()) / 2,
                50
        );
        askToTakeFineTicketButton.setLocation(
                (getWidth() - askToTakeFineTicketButton.getWidth()) / 2,
                    askToPayLabel.getY() + askToPayLabel.getHeight() + getIndent() * 5
        );
        payButton.setLocation(
                getWidth() - getIndent() - payButton.getWidth(),
                getHeight() - getIndent() - payButton.getHeight()
        );

        add(askToPayLabel);
        add(askToTakeFineTicketButton);
        add(giveTicketButton);
        add(ticketsBox);
        add(payButton);
    }

    private void onTakingFineTicket(ActionEvent actionEvent) {

    }

    private void onGivingTicket(ActionEvent actionEvent) {

    }

    private void onPaying(ActionEvent actionEvent) {

    }
}
