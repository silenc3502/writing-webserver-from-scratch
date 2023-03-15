package com.eddicorp.blog.repository;

import com.eddicorp.blog.Post;

import java.util.List;

public interface BlogPostRepository {

    void save(Post post);
    List<Post> findAll();
}
