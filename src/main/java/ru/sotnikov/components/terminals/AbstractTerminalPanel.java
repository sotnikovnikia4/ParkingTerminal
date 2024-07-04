package ru.sotnikov.components.terminals;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import ru.sotnikov.api.BackendLogic;
import ru.sotnikov.configuration.Configuration;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractTerminalPanel extends JPanel {
    private final int indent;

    private final int heightOfScreen;

    private final Font fontOfLabels;

    private final JLabel nameOfTerminalLabel, mainLabel;

    private final JComboBox<Ticket> ticketsBox;

    private final BackendLogic backendLogic;

    private final JButton giveTicketButton;

    private final  Configuration.Properties properties;

    protected AbstractTerminalPanel(String nameOfTerminal, ApplicationContext applicationContext){
        properties = applicationContext.getBean("properties", Configuration.Properties.class);
        indent = properties.getIndent();
        heightOfScreen = properties.getHeightOfScreen();

        ticketsBox = applicationContext.getBean("ticketsBox", JComboBox.class);
        giveTicketButton = applicationContext.getBean("giveTicketButton", JButton.class);
        backendLogic = applicationContext.getBean(BackendLogic.class);

        setBounds(51, 0, properties.getWidthOfTerminal(),properties.getHeightOfTerminal());

        fontOfLabels = applicationContext.getBean("fontOfLabels", Font.class);

        this.nameOfTerminalLabel = new JLabel();
        this.nameOfTerminalLabel.setSize(new Dimension(110, indent - 1));
        this.nameOfTerminalLabel.setFont(fontOfLabels);
        this.nameOfTerminalLabel.setText(nameOfTerminal);
        this.nameOfTerminalLabel.setHorizontalAlignment(JLabel.CENTER);
        this.nameOfTerminalLabel.setVerticalAlignment(JLabel.CENTER);

        mainLabel = new JLabel();
        mainLabel.setSize(new Dimension((int)(getWidth() / 1.1), getIndent() * 6));
        mainLabel.setFont(getFontOfLabels());
        mainLabel.setHorizontalAlignment(JLabel.CENTER);
        mainLabel.setVerticalAlignment(JLabel.CENTER);

        nameOfTerminalLabel.setLocation((getWidth() - nameOfTerminalLabel.getWidth()) / 2, 1);
        mainLabel.setLocation(
                (getWidth() - mainLabel.getWidth()) / 2,
                50
        );

        add(nameOfTerminalLabel);
        add(ticketsBox);
        add(mainLabel);
        add(giveTicketButton);

        ticketsBox.setVisible(false);

        setLayout(null);
        setVisible(false);
        giveTicketButton.setVisible(false);

        backendLogic.loadAllTickets(ticketsBox);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.drawRect(indent, indent, getWidth() - indent * 2, heightOfScreen);
    }

    protected final void doSomeAndHandleExceptionInOtherThread(Runnable tryAction){
        try{
            tryAction.run();
        }catch(RuntimeException e){
            new Thread(() -> {
                StringBuilder sb = new StringBuilder();
                sb.append("<html><p style=\"text-align: center\">");
                int b = 50;
                for(int i = 0; i < e.getMessage().length(); i += b){
                    sb.append(e.getMessage(), i, Math.min(e.getMessage().length(), i + b));
                    sb.append("<br/>");
                }
                sb.append("</html>");

                getMainLabel().setText(sb.toString());

                try {
                    Thread.sleep(properties.getTimeOfVisibilityError());
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                defaultState();
            }).start();
        }
    }

    protected abstract void defaultState();
}
