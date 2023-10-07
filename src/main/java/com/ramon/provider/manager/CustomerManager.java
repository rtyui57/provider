package com.ramon.provider.manager;

import com.ramon.provider.manager.repository.CustomerRepository;
import com.ramon.provider.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class CustomerManager {

    @Autowired
    protected CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer find(String id) {
        return customerRepository.findById(id).get();
    }

    public void delete(String id) {
        customerRepository.delete(find(id));
    }

    public void deleteAll() {
        for (Customer customer : new ArrayList<>(customerRepository.findAll())) {
            customerRepository.delete(customer);
        }
    }

    public void importCustomer(Customer newCustomer) {
        if (ObjectUtils.isEmpty(newCustomer.getName())) {
            throw new RuntimeException("Customer must have name");
        }
        Customer customer;
        Optional<Customer> opt = customerRepository.findById(newCustomer.getName());
        if (opt.isEmpty()) {
            customer = new Customer();
            customer.setCreationDate(new Date());
            customer.setName(newCustomer.getName());
        } else {
            customer = opt.get();
        }
        customer.setModificationDate(new Date());
        customer.setIcon(newCustomer.getIcon());
        customer.setUiProperties(newCustomer.getUiProperties());
        customer.setDescription(newCustomer.getDescription());
        customer.setDevices(newCustomer.getDevices());
        customer.setUsers(newCustomer.getUsers());
        customerRepository.save(customer);
    }
}
