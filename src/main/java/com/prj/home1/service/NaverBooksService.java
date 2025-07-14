package com.prj.home1.service;

import com.google.gson.JsonParser;
import com.prj.home1.vo.BookInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@Service
public class NaverBooksService {

    private static final String NAVER_BOOKS_API_URL = "https://openapi.naver.com/v1/search/book.json";
    private static final String CLIENT_ID = "";  // 네이버 API Client ID
    private static final String CLIENT_SECRET = "";  // 네이버 API Client Secret

    private final RestTemplate restTemplate;

    public NaverBooksService() {
        this.restTemplate = new RestTemplate(); // RestTemplate 생성
    }

    // 키워드를 사용하여 네이버 책 API에서 책 정보를 가져오는 메소드
    public List<BookInfo> getBookInfo(String keyword) throws Exception {
        String url = NAVER_BOOKS_API_URL + "?query=" + keyword + "&display=5";  // 검색 결과는 5개로 제한

        // 요청 헤더에 클라이언트 ID와 클라이언트 시크릿 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", CLIENT_ID);
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);

        // HttpEntity 생성 (헤더와 요청 본문을 담음)
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate을 사용하여 GET 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        // 응답에서 JSON 문자열을 가져와서 파싱
        String responseString = response.getBody();
        log.debug("Naver API response: {}", responseString);
        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();

        // "items" 배열에서 책 정보 추출
        JsonArray itemsArray = jsonResponse.getAsJsonArray("items");
        List<BookInfo> books = new ArrayList<>();

        for (int i = 0; i < itemsArray.size(); i++) {
            JsonObject book = itemsArray.get(i).getAsJsonObject();
            String title = book.get("title").getAsString();
            String author = book.get("author").getAsString();
            String description = book.get("description").getAsString();

            // BookInfo 객체 생성 후 리스트에 추가
            BookInfo bookInfo = new BookInfo(title, author, description);
            books.add(bookInfo);
        }

        return books;
    }
}
