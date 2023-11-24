package com.ramon.provider.manager.user;

import com.ramon.provider.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {

}
