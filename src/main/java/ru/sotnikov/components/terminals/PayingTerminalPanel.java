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

@Component("terminal2")
public class PayingTerminalPanel extends AbstractTerminalPanel {
    private final JLabel askToPayLabel;
    private final Button askToTakeFineTicketButton, giveTicketButton, payButton;
    private final JComboBox<Ticket> ticketsBox;

    private final BackendLogic backendLogic;

    private int costOfParking;
    private Thread waitingPaymentThread;

    @Autowired
    public PayingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 2", applicationContext);

        backendLogic = applicationContext.getBean(BackendLogic.class);

        askToPayLabel = new JLabel();
        askToPayLabel.setSize(new Dimension((int)(getWidth() / 1.1), getIndent() * 4));
        askToPayLabel.setFont(getFontOfLabels());
        askToPayLabel.setText("Приложите талончик");
        askToPayLabel.setHorizontalAlignment(JLabel.CENTER);
        askToPayLabel.setVerticalAlignment(JLabel.CENTER);

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

        defaultState();
    }

    private void onTakingFineTicket(ActionEvent actionEvent) {
        new Thread(() -> {
            try {
                Ticket ticket = backendLogic.getFineTicket();
                askToPayLabel.setText("Выдан штрафной талон " + ticket);
                askToTakeFineTicketButton.setVisible(false);
                ticketsBox.setEnabled(false);
                giveTicketButton.setVisible(false);
                Thread.sleep(2000);
                defaultState();
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void onGivingTicket(ActionEvent actionEvent) {
        try{
            askToPayLabel.setText("Производится расчет стоимости оплаты, подождите...");
            giveTicketButton.setVisible(false);
            askToTakeFineTicketButton.setVisible(false);
            costOfParking = backendLogic.getSumOfPayment((Ticket)ticketsBox.getSelectedItem());
            askToPayLabel.setText("<html>К оплате: <br>" + costOfParking + " руб.</html>");
            payButton.setVisible(true);
            ticketsBox.setEnabled(false);

            waitingPaymentThread = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    defaultState();
                } catch (InterruptedException ignored) {}
            });
            waitingPaymentThread.start();
        }
        catch(TerminalException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            defaultState();
        }
    }

    private void onPaying(ActionEvent actionEvent) {
        new Thread(() -> {
            try{
                waitingPaymentThread.interrupt();
                askToPayLabel.setText("Обработка платежа, подождите...");
                Thread.sleep(2000);
                backendLogic.pay(costOfParking, (Ticket)ticketsBox.getSelectedItem(), null);
                askToPayLabel.setText(
                        "<html>Талон " +
                                (Ticket)ticketsBox.getSelectedItem() +
                                " оплачен,<br/> выезд возможен в течение " + backendLogic.getTimeOfLeaving() + " минут</html>"
                );
                payButton.setVisible(false);
                Thread.sleep(5000);
                defaultState();
            }
            catch(TerminalException e){
                JOptionPane.showMessageDialog(null, e.getMessage());
                defaultState();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void defaultState(){
        askToPayLabel.setText("Приложите талончик");
        askToTakeFineTicketButton.setVisible(true);
        giveTicketButton.setVisible(true);
        ticketsBox.setEnabled(true);
        payButton.setVisible(false);
        costOfParking = 0;
    }
}
