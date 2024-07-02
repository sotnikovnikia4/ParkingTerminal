package ru.sotnikov.api;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.sotnikov.configuration.Configuration;
import ru.sotnikov.util.TerminalException;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component("backendLogic")
public class BackendLogic {
    private final JComboBox<Ticket> ticketsBox;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String url;

    @Autowired
    public BackendLogic(JComboBox<Ticket> ticketsBox, RestTemplate restTemplate, Configuration.Properties properties, ObjectMapper objectMapper) {
        this.ticketsBox = ticketsBox;
        this.restTemplate = restTemplate;

        url = properties.getUrlBackend();
        this.objectMapper = objectMapper;
        loadAllTickets();
    }

    public Ticket takeTicket() throws TerminalException{
        try{
            HttpEntity<Object> httpEntity = new HttpEntity<>("{}");
            String json = restTemplate.postForObject(url + "/ticket/enter", httpEntity, String.class);

            Ticket ticket = parseTicketFromJsonNode(objectMapper.readTree(json));
            ticketsBox.insertItemAt(ticket, 0);

            return ticket;
        }
        catch (JacksonException | RestClientException e) {
            throw new TerminalException(e.getMessage());
        }

    }

    public int getSumOfPayment(Ticket ticket) {
        try{
            if(ticket == null){
                throw new TerminalException("Не выбран талон, чтобы приложить");
            }

            return restTemplate.getForObject(url + "/ticket/cost/" + ticket.getNumber(), Integer.class);
        }catch (NullPointerException | RestClientException e){
            throw new TerminalException(e.getMessage());
        }

    }

    public void pay(int costOfParking, Ticket ticket, Object card) {
        try{
//            Map<String, Object> parameters = Map.of(
//                    "cost", costOfParking,
//                    "ticket", ticket,
//                    "card", card);
//
//            //String json = restTemplate.postForObject(url + "/ticket/pay/" + );
        }
        catch(NullPointerException | RestClientException e){
            throw new TerminalException(e.getMessage());
        }
    }

    public int getTimeOfLeaving() {
        return 10;
    }

    public int getFineCost() {
        try {
            return restTemplate.getForObject(url + "/ticket/fine", Integer.class);
        }
        catch (NullPointerException | RestClientException e){
            throw new TerminalException(e.getMessage());
        }
    }

    public Ticket getFineTicket() {
        return null;
    }

    public int getFreeTime() {
        return 10;
    }

    private void loadAllTickets() throws TerminalException{
        try{
            String json = restTemplate.getForObject(url + "/ticket", String.class);

            JsonNode node = objectMapper.readTree(json);
            for(int i = 0; i < node.size(); i++){
                ticketsBox.insertItemAt(parseTicketFromJsonNode(node.get(i)), 0);
            }
        }
        catch(RuntimeException | JacksonException ignored){

        }
    }
    private Ticket parseTicketFromJsonNode(JsonNode node) {
        return Ticket.builder().number(
                node.get("id").asInt()
        ).checkIn(
                LocalDateTime.parse(node.get("entryTime").asText())
        ).build();
    }


}
