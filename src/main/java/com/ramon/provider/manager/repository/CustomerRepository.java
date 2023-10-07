package com.ramon.provider.manager.repository;

import com.ramon.provider.model.Customer;
import com.ramon.provider.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository  extends MongoRepository<Customer,String> {
}
