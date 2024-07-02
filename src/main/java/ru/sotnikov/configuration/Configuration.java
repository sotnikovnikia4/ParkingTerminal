package ru.sotnikov.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
@ComponentScan("ru.sotnikov")
@PropertySource("classpath:./configuration.properties")
public class Configuration {
    private final ApplicationContext applicationContext;

    private final int indent;

    private final int heightOfButton;
    private final int yOfGiveTicketButton;

    @Autowired
    public Configuration(ApplicationContext applicationContext, @Value("${indent}") int indent, @Value("${heightOfTerminal}") int heightOfTerminal){
        this.applicationContext = applicationContext;
        heightOfButton = 50;
        this.indent = indent;
        yOfGiveTicketButton = heightOfTerminal - heightOfButton - indent;
    }

    @Bean("terminals")
    public JPanel[] terminals(){
        JPanel[] terminals = new JPanel[3];

        terminals[0] = applicationContext.getBean("terminal1", JPanel.class);
        terminals[1] = applicationContext.getBean("terminal2", JPanel.class);
        terminals[2] = applicationContext.getBean("terminal3", JPanel.class);

        return terminals;
    }

    @Bean("fontOfLabels")
    public Font font(){
        return new Font(Font.SANS_SERIF, Font.PLAIN, indent);
    }

    @Bean("ticketsBox")
    @Scope("singleton")
    public JComboBox<Ticket> ticketsBox(
            @Qualifier("fontOfLabels") Font font){

        JComboBox<Ticket> ticketsBox = new JComboBox<>();
        ticketsBox.setSize(250, 50);
        ticketsBox.setFont(font);
        ticketsBox.setEditable(true);
        ticketsBox.setLocation(
                properties().indent + 1,
                yOfGiveTicketButton -  properties().indent - ticketsBox.getHeight()
        );

        return ticketsBox;
    }

    @Bean("giveTicketButton")
    @Scope("prototype")
    public Button giveTicketButton(
            @Qualifier("fontOfLabels") Font font
    ){
        Button giveTicketButton = new Button();
        giveTicketButton.setSize(250, heightOfButton);
        giveTicketButton.setFont(font);
        giveTicketButton.setLabel("Приложить талон");

        giveTicketButton.setLocation(
                properties().indent + 1,
                yOfGiveTicketButton
        );
        return giveTicketButton;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean("properties")
    public Properties properties(){
        return new Properties();
    }

    @Getter
    public static class Properties{
        @Value("${indent}")
        private int indent;
        @Value("${heightOfScreen}")
        private int heightOfScreen;
        @Value("${widthOfWindow}")
        private int widthOfWindow;
        @Value("${heightOfWindow}")
        private int heightOfWindow;
        @Value("${heightOfTerminal}")
        private int heightOfTerminal;
        @Value("${widthOfTerminal}")
        private int widthOfTerminal;
    }
}
