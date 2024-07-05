package ru.sotnikov.components.terminals;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import ru.sotnikov.api.BackendLogic;
import ru.sotnikov.configuration.Configuration;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;

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
        this.nameOfTerminalLabel.setSize(new Dimension(getWidth(), indent - 1));
        this.nameOfTerminalLabel.setFont(fontOfLabels);
        this.nameOfTerminalLabel.setText(nameOfTerminal);
        this.nameOfTerminalLabel.setHorizontalAlignment(JLabel.CENTER);
        this.nameOfTerminalLabel.setVerticalAlignment(JLabel.CENTER);
        this.nameOfTerminalLabel.setForeground(new Color(230, 247, 233));


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

        ticketsBox.setVisible(false);

        setLayout(null);
        setVisible(false);
        giveTicketButton.setVisible(false);

        backendLogic.loadAllTickets(ticketsBox);
        add(ticketsBox);
        add(mainLabel);
        add(giveTicketButton);
        add(nameOfTerminalLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(173, 72, 9));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(new Color(43, 46, 44));
        g.fillRect(indent, indent * 2 + heightOfScreen, getWidth() - indent * 2, getHeight() - indent * 3 - heightOfScreen);

        g.setColor(new Color(209, 234, 237));
        g.fillRect(indent, indent, getWidth() - indent * 2, getHeightOfScreen());

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.drawRect(indent, indent, getWidth() - indent * 2, heightOfScreen);
        g.drawRect(indent, indent * 2 + heightOfScreen, getWidth() - indent * 2, getHeight() - indent * 3 - heightOfScreen);
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
