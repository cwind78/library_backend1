package com.prj.home1.service;

import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class VertexAIService {

    private static final String VERTEX_AI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=";
    private static final String API_KEY = "";

    private final RestTemplate restTemplate;

    public VertexAIService() {
        this.restTemplate = new RestTemplate(); // RestTemplate 생성
    }

    public List<String> extractKeywords(String inputText) throws Exception {
        String payload = buildPayload(inputText+" 소설 추천 결과 책 제목을 ,을 구분자로 줘");
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + API_KEY);  // API Key 또는 OAuth2 Token을 Bearer로 추가
        headers.set("Content-Type", "application/json");

        // 요청 본문 설정
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        // RestTemplate을 사용하여 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                VERTEX_AI_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        // 응답 처리
        String responseString = response.getBody();
        log.debug("Vertex AI response: {}", responseString);
        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();

        JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
        if (candidates != null && candidates.size() > 0) {
            JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
            JsonObject content = firstCandidate.getAsJsonObject("content");

            // parts 배열에서 첫 번째 항목 선택
            JsonArray parts = content.getAsJsonArray("parts");
            if (parts != null && parts.size() > 0) {
                // 텍스트 가져오기
                String text = parts.get(0).getAsJsonObject().get("text").getAsString();
                return Arrays.asList(text.split(","));  // 원하는 텍스트를 반환
            }
        }

        return Collections.emptyList();
    }

    private String buildPayload(String inputText) {
        JsonObject jsonObject = new JsonObject();

        // "contents" 필드 안에 "parts"와 "text"를 설정
        JsonObject content = new JsonObject();
        JsonObject part = new JsonObject();
        part.addProperty("text", inputText);  // 입력 텍스트 설정

        // parts 배열에 추가
        JsonArray partsArray = new JsonArray();
        partsArray.add(part);

        // contents 배열에 추가
        content.add("parts", partsArray);
        JsonArray contentsArray = new JsonArray();
        contentsArray.add(content);

        // 전체 구조에 contents 추가
        jsonObject.add("contents", contentsArray);

        return jsonObject.toString();  // JSON 형태로 반환
    }
}
