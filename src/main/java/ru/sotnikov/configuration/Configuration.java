package ru.sotnikov.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@org.springframework.context.annotation.Configuration
@ComponentScan("ru.sotnikov")
@RequiredArgsConstructor
public class Configuration {
    private final ApplicationContext applicationContext;



    @Bean("terminals")
    public JPanel[] terminals(){
        JPanel[] terminals = new JPanel[3];

        terminals[0] = applicationContext.getBean("terminal1", JPanel.class);
        terminals[1] = applicationContext.getBean("terminal2", JPanel.class);
        terminals[2] = applicationContext.getBean("terminal3", JPanel.class);

        return terminals;
    }

    @Bean("tickets")
    public Map<Integer, Ticket> tickets(){
        return new HashMap<>();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
