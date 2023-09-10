package com.ramon.provider.manager;

import com.ramon.provider.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PostRepository extends MongoRepository<Post,String>
{
    @Query("{name:'?0'}")
    Post findItemByName(String name);
}
