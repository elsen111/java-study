package com.customer_crud.backend.services;

import com.customer_crud.backend.models.Customer;
import com.customer_crud.backend.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private final CustomerRepository  customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private Customer findOrThrow(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }


    public Customer getCustomer(Integer id) {
        return findOrThrow(id);
    }

    public void setCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void deleteCustomer(Integer id) {
        findOrThrow(id);
        customerRepository.delete(id);
    }

    public void updateCustomer(Integer id, Customer customer) {
        findOrThrow(id);
        customer.setId(id);
        customerRepository.save(customer);
    }

    public Customer updatePartialCustomer(Integer id, Map<String, Object> updates) {
        Customer customer = getCustomer(id);

        updates.forEach((key, value) -> {
            if (key.equals("id")) return;
            Field field = ReflectionUtils.findField(Customer.class, key);
            if (field != null) {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, customer, value);
            }
        });

        return customerRepository.save(customer);
    }
}
