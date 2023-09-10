package com.ramon.provider.manager;

import com.ramon.provider.model.Post;

import java.util.List;

public interface SearchRepository {

    List<Post> findByText(String text);

}
