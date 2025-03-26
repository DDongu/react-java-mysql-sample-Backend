package com.hackforfun.coronatrackerbackend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CoronaTrackerController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(method = RequestMethod.GET, value = "/coronatracker/statisticsbycountry")
    public List<ObjectNode> getStatisticsByCountry() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://disease.sh/v3/covid-19/countries";

        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            // 전체 JSON 배열을 직접 스트림으로 변환
            List<ObjectNode> transformedData = objectMapper.createArrayNode();
            for (JsonNode countryNode : rootNode) {
                ObjectNode transformedNode = objectMapper.createObjectNode()
                    .put("country_code", countryNode.get("countryInfo").get("iso2").asText())
                    .put("location", countryNode.get("country").asText())
                    .put("confirmed", countryNode.get("cases").asInt())
                    .put("dead", countryNode.get("deaths").asInt())
                    .put("recovered", countryNode.get("recovered").asInt());
                
                ((List<ObjectNode>) transformedData).add(transformedNode);
            }

            return transformedData;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}