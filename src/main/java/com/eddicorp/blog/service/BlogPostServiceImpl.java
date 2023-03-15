package com.eddicorp.blog.service;

import com.eddicorp.blog.Post;
import com.eddicorp.blog.repository.BlogPostRepository;
import com.eddicorp.blog.repository.BlogPostRepositoryImpl;

import java.util.List;

public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository repository = BlogPostRepositoryImpl.getInstance();

    @Override
    public void write(String title, String content, String author) {
        final Post newPost = new Post(title, content, author);
        repository.save(newPost);
    }

    @Override
    public List<Post> getAllPosts() {
        return repository.findAll();
    }
}