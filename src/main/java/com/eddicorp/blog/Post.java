package com.eddicorp.blog;

import java.util.UUID;

public class Post {
    private String id;
    private String title;
    private String content;
    private String author;

    public Post(String title, String content, String author) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
