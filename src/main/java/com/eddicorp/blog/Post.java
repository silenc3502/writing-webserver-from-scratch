package com.eddicorp.blog;

public class Post {
    private Long id;
    private String title;
    private String content;
    private String author;

    public Post(Long id, String title, String content, String author) {
        this.id = id + 1L;
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
