package com.eddicorp.blog.repository;

import com.eddicorp.blog.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogPostRepositoryImpl implements BlogPostRepository {

    private static final BlogPostRepository INSTANCE = new BlogPostRepositoryImpl();

    public static BlogPostRepository getInstance() {
        return INSTANCE;
    }

    private BlogPostRepositoryImpl() {
    }

    private final Map<String, Post> postDb = new HashMap<>();

    @Override
    public void save(Post post) {
        postDb.put(post.getId(), post);
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(postDb.values());
    }
}
