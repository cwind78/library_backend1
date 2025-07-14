package com.prj.home1.ctrl;

import com.prj.home1.service.NaverBooksService;
import com.prj.home1.service.VertexAIService;
import com.prj.home1.vo.BookInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private VertexAIService vertexAIService;

    @Autowired
    private NaverBooksService naverBooksService;

    @PostMapping("/extractBooks")
    public List<BookInfo> extractBooks(@RequestBody String inputText) {
        try {
            // 1. 키워드 추출
            List<String> keywords = vertexAIService.extractKeywords(inputText);

            // 2. 네이버 책 API로 책 정보 조회
            List<BookInfo> books = naverBooksService.getBookInfo(keywords.get(0));  // 첫 번째 키워드만 사용

            return books;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}