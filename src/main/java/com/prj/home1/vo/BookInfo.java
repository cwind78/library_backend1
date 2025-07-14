package com.prj.home1.vo;

public class BookInfo {

    private String title;       // 책 제목
    private String author;      // 책 작가
    private String description; // 책 줄거리

    // 생성자
    public BookInfo(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    // Getter와 Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}