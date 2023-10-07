package com.ramon.provider.manager.repository;

import com.ramon.provider.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository  extends MongoRepository<User,String> {

    List<User> findByCustomer(String customer);
}
