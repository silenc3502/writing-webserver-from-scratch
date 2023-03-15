package com.eddicorp.blog.service;

import com.eddicorp.blog.Post;

import java.util.List;

public interface BlogPostService {

    void write(String title, String content, String author);

    List<Post> getAllPosts();
}
