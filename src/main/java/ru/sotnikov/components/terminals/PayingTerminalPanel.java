package ru.sotnikov.components.terminals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sotnikov.api.BackendLogic;

import java.awt.*;
import java.awt.event.ActionEvent;

@Component("terminal2")
public class PayingTerminalPanel extends AbstractTerminalPanel {
    private final Label askToPayLabel;

    private final Button askToTakeFineTicketButton, giveTicketButton, chooseTicketButton, payButton;

    private final BackendLogic backendLogic;

    @Autowired
    public PayingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 2");

        backendLogic = applicationContext.getBean(BackendLogic.class);

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

        giveTicketButton = new Button();
        giveTicketButton.setSize(170, 50);
        giveTicketButton.setFont(getFontOfLabels());
        giveTicketButton.setLabel("Приложить талон");
        giveTicketButton.addActionListener(this::onGivingTicket);

        chooseTicketButton = new Button();
        chooseTicketButton.setSize(170, 50);
        chooseTicketButton.setFont(getFontOfLabels());
        chooseTicketButton.setLabel("Выбрать талон");
        chooseTicketButton.addActionListener(this::onChoosingTicket);

        payButton = new Button();
        payButton.setSize(300, 50);
        payButton.setFont(getFontOfLabels());
        payButton.setLabel("Приложить карту для оплаты");
        payButton.addActionListener(this::onPaying);


        askToPayLabel.setLocation(
                (getWidth() - askToPayLabel.getWidth()) / 2,
                50
        );
        askToTakeFineTicketButton.setLocation(
                (getWidth() - askToTakeFineTicketButton.getWidth()) / 2,
                    askToPayLabel.getY() + askToPayLabel.getHeight() + getIndent() * 5
        );
        giveTicketButton.setLocation(
                getIndent() + 1,
                getHeight() - giveTicketButton.getHeight() - getIndent()
        );
        chooseTicketButton.setLocation(
                getIndent() + 1,
                giveTicketButton.getY() - getIndent() - chooseTicketButton.getHeight()
        );
        payButton.setLocation(
                getWidth() - getIndent() - payButton.getWidth(),
                getHeight() - getIndent() - payButton.getHeight()
        );

        add(askToPayLabel);
        add(askToTakeFineTicketButton);
        add(giveTicketButton);
        add(chooseTicketButton);
        add(payButton);
    }

    private void onTakingFineTicket(ActionEvent actionEvent) {

    }

    private void onGivingTicket(ActionEvent actionEvent) {

    }

    private void onChoosingTicket(ActionEvent actionEvent) {
    }

    private void onPaying(ActionEvent actionEvent) {

    }
}
