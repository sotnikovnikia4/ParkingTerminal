package ru.sotnikov.components.terminals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sotnikov.util.TerminalException;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

@Component("terminal2")
public class PayingTerminalPanel extends AbstractTerminalPanel {
    private final JButton askToTakeFineTicketButton, payButton;

    private int costOfParking;
    private Thread waitingPaymentThread;

    @Autowired
    public PayingTerminalPanel(ApplicationContext applicationContext){
        super("Терминал 2", applicationContext);

        getMainLabel().setText("Приложите талончик");

        askToTakeFineTicketButton = new JButton();
        askToTakeFineTicketButton.setSize(200, 50);
        askToTakeFineTicketButton.setFont(getFontOfLabels());
        askToTakeFineTicketButton.setText("Потерял талон");
        askToTakeFineTicketButton.addActionListener(this::onTakingFineTicket);

        getGiveTicketButton().addActionListener(this::onGivingTicket);

        payButton = new JButton();
        payButton.setSize(400, 50);
        payButton.setFont(getFontOfLabels());
        payButton.setText("Приложить карту для оплаты");
        payButton.addActionListener(this::onPaying);
        payButton.setVisible(false);

        askToTakeFineTicketButton.setLocation(
                (getWidth() - askToTakeFineTicketButton.getWidth()) / 2,
                    getMainLabel().getY() + getMainLabel().getHeight() + getIndent() * 5
        );
        payButton.setLocation(
                getWidth() - getIndent() - payButton.getWidth(),
                getHeight() - getIndent() - payButton.getHeight()
        );

        add(askToTakeFineTicketButton);
        add(payButton);

        defaultState();
    }

    private void onTakingFineTicket(ActionEvent actionEvent) {
        new Thread(() -> {
            try {
                Ticket ticket = getBackendLogic().getFineTicket();
                getBackendLogic().addTicketToBox(ticket);

                getMainLabel().setText("Выдан штрафной талон " + ticket);
                askToTakeFineTicketButton.setVisible(false);
                getTicketsBox().setEnabled(false);
                getGiveTicketButton().setVisible(false);
                Thread.sleep(2000);
                defaultState();
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void onGivingTicket(ActionEvent actionEvent) {
        try{
            getMainLabel().setText("Производится расчет стоимости оплаты, подождите...");
            getGiveTicketButton().setVisible(false);
            askToTakeFineTicketButton.setVisible(false);
            costOfParking = getBackendLogic().getSumOfPayment((Ticket)getTicketsBox().getSelectedItem());
            getTicketsBox().setEnabled(false);

            if(costOfParking == 0){
                if(((Ticket) Objects.requireNonNull(getTicketsBox().getSelectedItem())).isPaid()){
                    getMainLabel().setText("<html><p style=\"text-align:center\">Талон "+ getTicketsBox().getSelectedItem() +" уже оплачен<br/> пройдите к терминалу 3</p></html>");
                }
                else{
                    getMainLabel().setText("<html><p style=\"text-align:center\">Время бесплатной парковки " + getBackendLogic().getFreeTime() + " мин.,<br> оплата не требуется</p></html>");
                }

                waitingPaymentThread = new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        defaultState();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            else{
                getMainLabel().setText("<html>К оплате: <br>" + costOfParking + " руб.</html>");
                payButton.setVisible(true);

                waitingPaymentThread = new Thread(() -> {
                    try {
                        Thread.sleep(10000);
                        defaultState();
                    } catch (InterruptedException ignored) {}
                });
            }

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
                payButton.setVisible(false);
                getMainLabel().setText("Обработка платежа, подождите...");
                //Thread.sleep(2000);
                getBackendLogic().pay(costOfParking, (Ticket)getTicketsBox().getSelectedItem(), "null");
                getMainLabel().setText(
                        "<html>Талон " +
                                (Ticket)getTicketsBox().getSelectedItem() +
                                " оплачен,<br/> выезд возможен в течение " + getBackendLogic().getTimeOfLeaving() + " минут</html>"
                );
                Thread.sleep(3000);
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
        try{
            getMainLabel().setText("<html><p style=\"text-align:center\">Приложите талончик<br>За утерю талончика штраф " + getBackendLogic().getFineCost() +" рублей</p></html>");
        }
        catch (TerminalException e){
            getMainLabel().setText("Отсутствует подключение...");
        }

        askToTakeFineTicketButton.setVisible(true);
        getGiveTicketButton().setVisible(true);
        getTicketsBox().setEnabled(true);
        payButton.setVisible(false);
        costOfParking = 0;
        getTicketsBox().setVisible(true);
    }
}
