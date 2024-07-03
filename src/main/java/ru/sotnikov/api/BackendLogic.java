package ru.sotnikov.api;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.sotnikov.configuration.Configuration;
import ru.sotnikov.util.TerminalException;
import ru.sotnikov.util.Ticket;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component("backendLogic")
public class BackendLogic {
    private final List<JComboBox<Ticket>> boxes;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String url;

    @Autowired
    public BackendLogic(List<JComboBox<Ticket>> boxes, RestTemplate restTemplate, Configuration.Properties properties, ObjectMapper objectMapper) {
        this.boxes = boxes;
        this.restTemplate = restTemplate;

        url = properties.getUrlBackend();
        this.objectMapper = objectMapper;
    }

    public Ticket takeTicket() throws TerminalException{
        try{
            HttpEntity<Object> httpEntity = new HttpEntity<>("{}");
            String json = restTemplate.postForObject(url + "/ticket/enter", httpEntity, String.class);

            return parseTicketFromJsonNode(objectMapper.readTree(json));
        }
        catch (JacksonException | RestClientException e) {
            throw new TerminalException(e.getMessage());
        }

    }

    public int getSumOfPayment(Ticket ticket) {
        if(ticket == null){
            throw new TerminalException("Не выбран талон, чтобы приложить");
        }

        return getIntegerFromUrl("/ticket/cost/" + ticket.getNumber());
    }

    public void pay(int costOfParking, Ticket ticket, String card) throws TerminalException {
        try{
            Map<String, Object> parameters = Map.of(
                    "cost", costOfParking,
                    "ticket", ticket.getNumber(),
                    "card", card);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(parameters);

            Boolean response = restTemplate.postForObject(url + "/ticket/pay", entity, Boolean.class);
            if(response == null || !response){
                throw new TerminalException("Оплата прошла неуспешно");
            }

            refreshTicket(ticket);
        }
        catch(NullPointerException | RestClientException e){
            throw new TerminalException(e.getMessage());
        }
    }

    private void refreshTicket(Ticket ticket){
        removeTicketFromBox(ticket);

        try{
            String json = restTemplate.getForObject(url + "/ticket/"+ticket.getNumber(), String.class);
            JsonNode jsonNode = objectMapper.readTree(json);
            ticket = parseTicketFromJsonNode(jsonNode);

            addTicketToBox(ticket);
        }
        catch(NullPointerException | RestClientException | JacksonException e){
            throw new TerminalException(e.getMessage());
        }
    }

    public int getTimeOfLeaving() {
        return getIntegerFromUrl("/ticket/exit-time");
    }

    public int getFineCost() {
        return getIntegerFromUrl("/ticket/fine");
    }

    public Ticket getFineTicket() {

        try {
            String json = restTemplate.postForObject(url + "/ticket/fine-ticket", "{}", String.class);
            JsonNode node = objectMapper.readTree(json);

            return parseTicketFromJsonNode(node);
        } catch (NullPointerException | RestClientException | JsonProcessingException e) {
            throw new TerminalException(e.getMessage());
        }
    }

    public int getFreeTime() {
        return getIntegerFromUrl("/ticket/free-time");
    }

    public void loadAllTickets(JComboBox<Ticket> box) throws TerminalException{
        try{
            String json = restTemplate.getForObject(url + "/ticket", String.class);

            JsonNode node = objectMapper.readTree(json);
            for(int i = 0; i < node.size(); i++){
                box.insertItemAt(parseTicketFromJsonNode(node.get(i)), 0);
            }
        }
        catch(RuntimeException | JacksonException ignored){

        }
    }

    public void addTicketToBox(Ticket ticket){
        for(JComboBox<Ticket> box : boxes){
            box.insertItemAt(ticket, 0);
        }
    }
    public void removeTicketFromBox(Ticket ticket){
        for(JComboBox<Ticket> box : boxes){
            box.removeItem(ticket);
        }
    }

    private Ticket parseTicketFromJsonNode(JsonNode node) {
        return Ticket.builder().number(
                node.get("id").asInt()
        ).checkIn(
                LocalDateTime.parse(node.get("entryTime").asText())
        ).build();
    }

    private int getIntegerFromUrl(String url){
        try {
            return Objects.requireNonNull(restTemplate.getForObject(this.url + url, Integer.class));
        }
        catch (NullPointerException | RestClientException e){
            throw new TerminalException(e.getMessage());
        }
    }
}
