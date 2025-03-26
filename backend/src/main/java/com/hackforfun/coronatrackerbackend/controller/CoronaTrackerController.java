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

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CoronaTrackerController {

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(method = RequestMethod.GET, value = "/coronatracker/statisticsbycountry")
	public List<Object> getStatisticsByCountry() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(headers);

		// ✅ 새로운 API URL (disease.sh)
		String url = "https://disease.sh/v3/covid-19/countries";

		// API 응답 데이터 가져오기
		String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

		try {
			// JSON 파싱
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response);

			// 🔹 JSON 데이터를 변환하여 반환
			List<Object> transformedData = rootNode.findValues("").stream()
				.map(countryNode -> objectMapper.createObjectNode()
					.put("country_code", countryNode.get("countryInfo").get("iso2").asText()) // 국가 코드
					.put("location", countryNode.get("country").asText()) // 국가명
					.put("confirmed", countryNode.get("cases").asInt()) // 확진자 수
					.put("dead", countryNode.get("deaths").asInt()) // 사망자 수
					.put("recovered", countryNode.get("recovered").asInt()) // 회복자 수
				).collect(Collectors.toList());

			return transformedData;

		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}
}
