package com.ramon.provider.repository;

import com.ramon.provider.model.Post;

import java.util.List;

public interface SearchRepository {

    List<Post> findByText(String text);

}