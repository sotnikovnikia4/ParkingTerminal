package ru.sotnikov.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.context.annotation.Configuration
@ComponentScan("ru.sotnikov")
@PropertySource("classpath:./configuration.properties")
public class Configuration {
    private final ApplicationContext applicationContext;

    private final int indent;

    private final int heightOfButton;
    private final int yOfGiveTicketButton;

    public final static List<JComboBox<Ticket>> boxes = new ArrayList<>();

    @Autowired
    public Configuration(ApplicationContext applicationContext, @Value("${indent}") int indent, @Value("${heightOfTerminal}") int heightOfTerminal){
        this.applicationContext = applicationContext;
        heightOfButton = 50;
        this.indent = indent;
        yOfGiveTicketButton = heightOfTerminal - heightOfButton - indent * 2;
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
    @Scope("prototype")
    public JComboBox<Ticket> ticketsBox(
            @Qualifier("fontOfLabels") Font font){

        JComboBox<Ticket> ticketsBox = new JComboBox<>();
        ticketsBox.setSize(250, 50);
        ticketsBox.setFont(font);
        ticketsBox.setEditable(true);
        ticketsBox.setLocation(
                properties().indent * 2 + 1,
                yOfGiveTicketButton -  properties().indent - ticketsBox.getHeight()
        );

        boxes.add(ticketsBox);

        return ticketsBox;
    }

    @Bean("giveTicketButton")
    @Scope("prototype")
    public JButton giveTicketButton(
            @Qualifier("fontOfLabels") Font font
    ){
        JButton giveTicketButton = new JButton();
        giveTicketButton.setSize(250, heightOfButton);
        giveTicketButton.setFont(font);
        giveTicketButton.setText("Приложить талон");

        giveTicketButton.setLocation(
                properties().indent * 2 + 1,
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

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
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
        @Value("${startTerminal}")
        private int startTerminal;
        @Value("${urlBackend}")
        private String urlBackend;
        @Value("${timeOfVisibilityError}")
        private int timeOfVisibilityError;
    }
}
